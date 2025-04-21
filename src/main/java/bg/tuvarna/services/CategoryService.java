package bg.tuvarna.services;

import bg.tuvarna.models.dto.CategoryDTO;
import bg.tuvarna.models.entities.Category;

import java.util.List;

public interface CategoryService {
    void save(CategoryDTO categoryDTO);
    Category findCategoryById(Long id);
    CategoryDTO getCategoryById(Long id);
    List<CategoryDTO> getAllCategories();
}
