package bg.tuvarna.models.dto;

import bg.tuvarna.enums.DepreciationType;

public record CategoryDTO(
        Long id,
        String name,
        DepreciationType depreciationField,
        Double maxAmortizationForTypeChange,
        Double reductionStep,
        Double maxAmortizationBeforeScrap,
        Integer maxYearsInUse
) {
}