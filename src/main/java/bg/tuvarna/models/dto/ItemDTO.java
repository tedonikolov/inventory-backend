package bg.tuvarna.models.dto;

import bg.tuvarna.enums.ItemStatus;
import bg.tuvarna.enums.ItemType;
import jakarta.json.bind.annotation.JsonbDateFormat;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;

public record ItemDTO(
        Long id,
        String number,
        String name,
        Double price,
        Double noAmortizationPrice,
        Double amortizationPrice,
        ItemType type,
        @JsonbDateFormat("dd-MM-yyyy")
        @Schema(type = SchemaType.STRING, format = "dd-MM-yyyy", example = "11-01-2025")
        LocalDate acquisitionDate,
        @JsonbDateFormat("dd-MM-yyyy")
        @Schema(type = SchemaType.STRING, format = "dd-MM-yyyy", example = "11-01-2025")
        LocalDate exploitationDate,
        ItemStatus status,
        Double amortization,
        @JsonbDateFormat("dd-MM-yyyy")
        @Schema(type = SchemaType.STRING, format = "dd-MM-yyyy", example = "11-01-2025")
        LocalDate toDate,
        @JsonbDateFormat("dd-MM-yyyy")
        @Schema(type = SchemaType.STRING, format = "dd-MM-yyyy", example = "11-01-2025")
        LocalDate deregistrationDate,
        Long categoryId,
        String imageUrl
) {
}
