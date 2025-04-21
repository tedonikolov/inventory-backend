package bg.tuvarna.services.impl;

import bg.tuvarna.models.dto.CategoryDTO;
import bg.tuvarna.models.entities.Category;
import bg.tuvarna.reporsitory.CategoryRepository;
import bg.tuvarna.resources.execptions.CustomException;
import bg.tuvarna.resources.execptions.ErrorCode;
import bg.tuvarna.services.CategoryService;
import bg.tuvarna.services.converters.impl.CategoryConverter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryConverter converter;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryConverter categoryConverter) {
        repository = categoryRepository;
        converter = categoryConverter;
    }

    @Override
    @Transactional
    public void save(CategoryDTO categoryDTO) {
        if (categoryDTO.id() == null) {
            repository.persist(converter.convertToEntity(categoryDTO));
        } else {
            Category category = findCategoryById(categoryDTO.id());
            repository.persist(converter.updateEntity(category, categoryDTO));
        }
    }

    @Override
    public Category findCategoryById(Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new CustomException("Category not found", ErrorCode.EntityNotFound));
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        return converter.convertToDto(findCategoryById(id));
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return repository.findAll()
                .stream()
                .map(converter::convertToDto)
                .collect(Collectors.toList());
    }
}
