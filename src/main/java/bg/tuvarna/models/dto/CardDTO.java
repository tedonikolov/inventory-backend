package bg.tuvarna.models.dto;

import bg.tuvarna.models.entities.Card;

import java.time.LocalDate;

public record CardDTO(
        Long id,
        LocalDate borrowDate,
        LocalDate returnDate,
        ItemDTO item,
        Long employeeId
) {
    public CardDTO(Card card, ItemDTO item) {
        this(card.id, card.getBorrowDate(), card.getReturnDate(), item, card.getEmployee().id);
    }
}