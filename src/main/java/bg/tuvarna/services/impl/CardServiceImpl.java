package bg.tuvarna.services.impl;

import bg.tuvarna.models.PageListing;
import bg.tuvarna.models.dto.CardDTO;
import bg.tuvarna.models.dto.requests.AddItemDTO;
import bg.tuvarna.models.dto.requests.CardFilter;
import bg.tuvarna.models.entities.Card;
import bg.tuvarna.models.entities.Item;
import bg.tuvarna.reporsitory.CardRepository;
import bg.tuvarna.resources.execptions.CustomException;
import bg.tuvarna.resources.execptions.ErrorCode;
import bg.tuvarna.services.CardService;
import bg.tuvarna.services.EmployeeServices;
import bg.tuvarna.services.ItemService;
import bg.tuvarna.services.converters.impl.ItemConverter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDate;

@ApplicationScoped
public class CardServiceImpl implements CardService {
    private final CardRepository repository;
    private final ItemService itemService;
    private final EmployeeServices employeeService;
    private final ItemConverter itemConverter;

    public CardServiceImpl(CardRepository repository, ItemService itemService, EmployeeServices employeeService, ItemConverter itemConverter) {
        this.repository = repository;
        this.itemService = itemService;
        this.employeeService = employeeService;
        this.itemConverter = itemConverter;
    }

    @Override
    @Transactional
    public void addItemToCard(AddItemDTO dto) {
        Item item = itemService.findItemById(dto.itemId());

        if (repository.isItemFree(dto.itemId(), LocalDate.now())) {
            Card card = new Card();
            card.setBorrowDate(LocalDate.now());
            card.setItem(item);

            card.setEmployee(employeeService.findEmployeeById(dto.employeeId()));

            repository.persist(card);
        } else {
            throw new CustomException("Item is already in use", ErrorCode.AlreadyExists);
        }

    }

    @Override
    @Transactional
    public void returnItem(Long cardId) {
        Card card = findCardById(cardId);
        card.setReturnDate(LocalDate.now());
        repository.persist(card);
    }

    @Override
    public PageListing<CardDTO> getAllCards(CardFilter filter) {
        PageListing<Card> pageListing = repository.findByFilter(filter);
        return new PageListing<CardDTO>(
                pageListing.getItems().stream()
                        .map(card -> new CardDTO(card, itemConverter.convertToDto(card.getItem())))
                        .toList(),
                pageListing.getCurrentPage(),
                pageListing.getPageSize(),
                pageListing.getTotalPage()
        );
    }

    @Override
    public Card findCardById(Long id) {
        return repository.findByIdOptional(id).orElseThrow(() -> new CustomException("Card not found", ErrorCode.EntityNotFound));
    }

    @Override
    public CardDTO getItemById(Long id) {
        Card card = findCardById(id);
        return new CardDTO(card, itemConverter.convertToDto(card.getItem()));
    }
}
