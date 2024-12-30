package impress.weasp.service;

import impress.weasp.controller.dto.cart.CartItemUpdateQuantityDto;
import impress.weasp.model.CartItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public interface CaritemService {
    CartItem updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity);
    void removeCartItem(Long userId, Long cartItemId);
    CartItem findCartItemById(Long cartItemId);

}
