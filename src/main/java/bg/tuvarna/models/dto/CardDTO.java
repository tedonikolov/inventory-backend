package bg.tuvarna.models.dto;

import bg.tuvarna.models.entities.Card;
import jakarta.json.bind.annotation.JsonbDateFormat;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;

public record CardDTO(
        Long id,
        @JsonbDateFormat("dd-MM-yyyy")
        @Schema(type = SchemaType.STRING, format = "dd-MM-yyyy", example = "11-01-2025")
        LocalDate borrowDate,
        @JsonbDateFormat("dd-MM-yyyy")
        @Schema(type = SchemaType.STRING, format = "dd-MM-yyyy", example = "11-01-2025")
        LocalDate returnDate,
        ItemDTO item,
        Long employeeId
) {
    public CardDTO(Card card, ItemDTO item) {
        this(card.id, card.getBorrowDate(), card.getReturnDate(), item, card.getEmployee().id);
    }
}