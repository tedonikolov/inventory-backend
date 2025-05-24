package bg.tuvarna.services.impl;

import bg.tuvarna.enums.DepreciationType;
import bg.tuvarna.enums.ItemStatus;
import bg.tuvarna.enums.ItemType;
import bg.tuvarna.enums.NotificationType;
import bg.tuvarna.models.PageListing;
import bg.tuvarna.models.dto.ItemDTO;
import bg.tuvarna.models.dto.NotificationDTO;
import bg.tuvarna.models.dto.requests.ItemFilter;
import bg.tuvarna.models.dto.requests.ItemWithImageDTO;
import bg.tuvarna.models.entities.Item;
import bg.tuvarna.reporsitory.ItemRepository;
import bg.tuvarna.resources.execptions.CustomException;
import bg.tuvarna.resources.execptions.ErrorCode;
import bg.tuvarna.services.CategoryService;
import bg.tuvarna.services.ItemService;
import bg.tuvarna.services.NotificationService;
import bg.tuvarna.services.S3Service;
import bg.tuvarna.services.converters.impl.ItemConverter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final ItemConverter converter;
    private final S3Service s3Service;
    private final CategoryService categoryService;
    private final NotificationService notificationService;
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository repository, ItemConverter converter, S3Service s3Service, CategoryService categoryService, NotificationService notificationService, ItemRepository itemRepository) {
        this.repository = repository;
        this.converter = converter;
        this.s3Service = s3Service;
        this.categoryService = categoryService;
        this.notificationService = notificationService;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public void save(ItemWithImageDTO request) {
        ItemDTO dto = request.getDto();

        if (dto.id() != null) {
            Item entity = findItemById(dto.id());

            if (!Objects.equals(entity.getNumber(), dto.number()) && findItemByNumber(dto.number()) != null) {
                throw new CustomException("Item already exists", ErrorCode.AlreadyExists);
            }

            if (dto.categoryId() != null) {
                entity.setCategory(categoryService.findCategoryById(dto.categoryId()));
            }

            repository.persist(converter.updateEntity(entity, dto));

            setImage(entity, request);
            repository.persist(entity);
        } else {
            if (findItemByNumber(dto.number()) != null) {
                throw new CustomException("Item already exists", ErrorCode.AlreadyExists);
            }

            Item entity = converter.convertToEntity(dto);

            if (dto.categoryId() != null) {
                entity.setCategory(categoryService.findCategoryById(dto.categoryId()));
            }

            setImage(entity, request);
            repository.persist(entity);
        }
    }

    private void setImage(Item item, ItemWithImageDTO request) {
        if (request.getImage() != null) {
            try {
                s3Service.uploadToS3("item-images", request.getImage(), "image_" + item.getName());

                item.setImagePath("image_" + item.getName());
            } catch (IOException e) {

            }
        }
    }

    @Override
    public Item findItemById(Long id) {
        return repository.findByIdOptional(id).orElseThrow(
                () -> new CustomException("Item not found", ErrorCode.EntityNotFound)
        );
    }

    @Override
    public ItemDTO getItemById(Long id) {
        return converter.convertToDto(findItemById(id));
    }

    @Override
    public PageListing<ItemDTO> getAllItems(ItemFilter filter) {
        PageListing<Item> pageListing = repository.findByFilter(filter);
        return new PageListing<>(
                pageListing.getItems().stream()
                        .map(converter::convertToDto)
                        .toList(),
                pageListing.getCurrentPage(),
                pageListing.getPageSize(),
                pageListing.getTotalPage()
        );
    }

    private Item findItemByNumber(String number) {
        return repository.find("LOWER(number)", number.toLowerCase()).firstResult();
    }

    @Override
    @Transactional
    //TODO need a fix
    public void changeAmortization() {
        List<Item> activeFixedAssets = repository.activeItems();

        LocalDate today = LocalDate.now();

        for (Item item : activeFixedAssets) {
            if (item.getExploitationDate() == null || item.getPrice() == null || item.getAmortization() == null) {
                continue;
            }

            long yearsPassed = ChronoUnit.YEARS.between(item.getToDate()!=null ? item.getToDate() : item.getExploitationDate(), today);

            if (item.getCategory().getDepreciationField() == DepreciationType.LINEAR) {
                double annualRate = item.getCategory().getReductionStep() / 100;
                double addedPercent = annualRate * yearsPassed * 100;

                double updatedPercent = item.getAmortization() + addedPercent;

                item.setAmortization(updatedPercent);
                item.setToDate(today);
                itemRepository.persist(item);
            }
        }
    }

    @Override
    @Transactional
    public void transferItemsToMaterial() {
        List<Item> activeFixedAssets = repository.activeItems();

        for (Item item : activeFixedAssets) {
            if(item.getCategory().getDmaStep() >= item.getAmortization()) {
                item.setType(ItemType.MA);
                repository.persist(item);
                item.getCards().stream().filter(card -> card.getReturnDate()==null).findFirst().ifPresent(card -> {
                    notificationService.createNotify(new NotificationDTO(
                            null,
                            "Преобразуване на актив",
                            "Актив с номер" + item.getNumber() + " беше преобразуван в материал",
                            NotificationType.ASSET_TRANSFORMATION,
                            card.getEmployee().id,
                            false,
                            null
                    ));
                });
            }
        }
    }

    @Override
    @Transactional
    public void performAutomaticScrapping() {
        List<Item> activeFixedAssets = repository.activeItems();

        LocalDate currentDate = LocalDate.now();

        for (Item item : activeFixedAssets) {
            if (item.getCategory() == null) {
                continue;
            }

            Double maxAmortizationBeforeScrap = item.getCategory().getMaxAmortizationBeforeScrap();
            Integer maxYearsInUse = item.getCategory().getMaxYearsInUse();

            boolean shouldScrap = false;

            if (maxAmortizationBeforeScrap != null) {
                if (item.getAmortization() >= maxAmortizationBeforeScrap) {
                    shouldScrap = true;
                }
            }

            if (!shouldScrap && maxYearsInUse != null && item.getAcquisitionDate() != null) {
                int yearsInUse = Period.between(item.getAcquisitionDate(), currentDate).getYears();
                if (yearsInUse >= maxYearsInUse) {
                    shouldScrap = true;
                }
            }

            if (shouldScrap) {
                item.setStatus(ItemStatus.SCRAPED);
                item.setDeregistrationDate(currentDate);
                repository.persist(item);
                item.getCards().stream().filter(card -> card.getReturnDate()==null).findFirst().ifPresent(card -> {
                    notificationService.createNotify(new NotificationDTO(
                            null,
                            "Бракуване на актив",
                            "Актив с номер" + item.getNumber() + " беше автоматично бракуван",
                            NotificationType.ASSET_TRANSFORMATION,
                            card.getEmployee().id,
                            false,
                            null
                    ));
                });
            }
        }
    }
}