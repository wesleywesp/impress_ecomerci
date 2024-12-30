package impress.weasp.repository;

import impress.weasp.model.Category;
import impress.weasp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    @Query("SELECT p FROM Product p " +
            "WHERE (:query IS NULL OR lower(p.name) LIKE lower(concat('%', :query, '%'))) " +
            "OR (:query IS NOT NULL AND lower(p.category.name) LIKE lower(concat('%', :query, '%')))")
    List<Product> searchProducts(@Param("query") String query);

}

