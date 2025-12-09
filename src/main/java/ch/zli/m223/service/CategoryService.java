package ch.zli.m223.service;

import ch.zli.m223.model.Category;
import ch.zli.m223.repository.CategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CategoryService {

    @Inject
    CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.listAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public Category createCategory(Category category) {
        categoryRepository.persist(category);
        return category;
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id);
        if (category != null) {
            categoryRepository.delete(category);
        }
    }

    @Transactional
    public Category updateCategory(Long id, Category updatedCategory) {
        Category category = categoryRepository.findById(id);

        if (category == null) {
            return null;
        }

        category.setTitle(updatedCategory.getTitle());
        categoryRepository.persist(category);
        return category;
    }
}
