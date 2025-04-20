package bg.tuvarna.services.converters.impl;

import bg.tuvarna.models.dto.CategoryDTO;
import bg.tuvarna.models.entities.Category;
import bg.tuvarna.services.converters.Converter;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryConverter implements Converter<CategoryDTO, Category> {
    @Override
    public CategoryDTO convertToDto(Category entity) {
        return new CategoryDTO(entity.id,
                entity.getName(),
                entity.getDepreciationField(),
                entity.getReductionStep(),
                entity.getMaxAmortizationBeforeScrap(),
                entity.getMaxYearsInUse()
        );
    }

    @Override
    public Category convertToEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.name());
        category.setDepreciationField(dto.depreciationField());
        category.setReductionStep(dto.reductionStep());
        category.setMaxAmortizationBeforeScrap(dto.maxAmortizationBeforeScrap());
        category.setMaxYearsInUse(dto.maxYearsInUse());
        return category;
    }

    @Override
    public Category updateEntity(Category entity, CategoryDTO dto) {
        entity.setName(dto.name());
        entity.setDepreciationField(dto.depreciationField());
        entity.setReductionStep(dto.reductionStep());
        entity.setMaxAmortizationBeforeScrap(dto.maxAmortizationBeforeScrap());
        entity.setMaxYearsInUse(dto.maxYearsInUse());
        return entity;
    }
}
