package impress.weasp.service;

import impress.weasp.model.Category;
import impress.weasp.model.Product;

import java.util.List;

public interface ProductService {
    public Product createProduct(Product product);
    public Product updateProduct(Long id, Product updatedProduct);
    public void deleteProduct(Long id);
    public List<Product> getAllProducts();
    public Product getProductById(Long id);
    public List<Product> getProductsByCategory(Category category);
    public List<Product> searchProducts(String query);

}
