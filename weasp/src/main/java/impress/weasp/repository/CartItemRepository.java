package impress.weasp.repository;


import impress.weasp.model.CartItem;
import impress.weasp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.product = :product")
    CartItem findByCartIdAndProduct(@Param("cartId") Long cartId, @Param("product") Product product);
}

