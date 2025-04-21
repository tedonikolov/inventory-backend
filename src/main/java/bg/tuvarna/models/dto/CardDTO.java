package bg.tuvarna.models.dto;

import bg.tuvarna.models.entities.Card;
import bg.tuvarna.services.converters.impl.ItemConverter;

import java.time.LocalDate;

public record CardDTO(
        Long id,
        LocalDate borrowDate,
        LocalDate returnDate,
        ItemDTO item,
        Long employeeId
) {
    static ItemConverter itemConverter;

    public CardDTO(Card card) {
        this(card.id, card.getBorrowDate(), card.getReturnDate(), itemConverter.convertToDto(card.getItem()), card.getEmployee().id);
    }
}