package bg.tuvarna.models.dto;

import bg.tuvarna.enums.ItemStatus;
import bg.tuvarna.enums.ItemType;

import java.time.LocalDate;

public record ItemDTO(
        Long id,
        String number,
        String name,
        Double price,
        Double noAmortizationPrice,
        Double amortizationPrice,
        ItemType type,
        LocalDate acquisitionDate,
        LocalDate exploitationDate,
        ItemStatus status,
        Double amortization,
        LocalDate toDate,
        LocalDate deregistrationDate,
        Long categoryId,
        String imageUrl
) {
}
