package impress.weasp.service;

import impress.weasp.model.Category;

import java.util.List;

public interface CategoriaService {

    public Category createCategory(Category category);

    public Category updateCategory(Long id, Category updatedCategory);
    public void deleteCategory(Long id);

    public List<Category> getAllCategories();
    public Category getCategoryBySlug(String slug);

}
