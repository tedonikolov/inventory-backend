package bg.tuvarna.services.impl;

import bg.tuvarna.models.PageListing;
import bg.tuvarna.models.dto.ItemDTO;
import bg.tuvarna.models.dto.requests.ItemFilter;
import bg.tuvarna.models.dto.requests.ItemWithImageDTO;
import bg.tuvarna.models.entities.Item;
import bg.tuvarna.reporsitory.ItemRepository;
import bg.tuvarna.resources.execptions.CustomException;
import bg.tuvarna.resources.execptions.ErrorCode;
import bg.tuvarna.services.ItemService;
import bg.tuvarna.services.S3Service;
import bg.tuvarna.services.converters.impl.ItemConverter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.Objects;

@ApplicationScoped
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final ItemConverter converter;
    private final S3Service s3Service;


    public ItemServiceImpl(ItemRepository repository, ItemConverter converter, S3Service s3Service) {
        this.repository = repository;
        this.converter = converter;
        this.s3Service = s3Service;
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

            repository.persist(converter.updateEntity(entity, dto));

            setImage(entity, request);
            repository.persist(entity);
        } else {
            if (findItemByNumber(dto.number()) != null) {
                throw new CustomException("Item already exists", ErrorCode.AlreadyExists);
            }

            Item entity = converter.convertToEntity(dto);

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
}
