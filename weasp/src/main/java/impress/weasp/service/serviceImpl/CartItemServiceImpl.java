package impress.weasp.service.serviceImpl;

import impress.weasp.controller.dto.cart.CartItemUpdateQuantityDto;
import impress.weasp.infra.exception.CartException;
import impress.weasp.model.Cart;
import impress.weasp.model.CartItem;
import impress.weasp.repository.CartItemRepository;
import impress.weasp.repository.CartRepository;
import impress.weasp.service.CaritemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CaritemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Override
    public CartItem updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity) {
        // Busca e valida o item do carrinho
        CartItem cartItem = findAndValidateCartItem(userId, cartItemId);

        // Atualiza a quantidade e recalcula o preço
        cartItem.setQuantity(quantity);
        BigDecimal newPrice = cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(quantity));
        cartItem.setPrice(newPrice);

        return cartItemRepository.save(cartItem);
    }


    @Override
    public void removeCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = findAndValidateCartItem(userId, cartItemId);
        Cart cart = cartItem.getCart();

        // Remove o item do carrinho
        cartItemRepository.delete(cartItem);

        // Atualiza o carrinho
        cart.getItems().remove(cartItem); // Remove o item da lista no carrinho
        cart.updateTotalAmount();

        // Se o carrinho estiver vazio, remova ou resete o carrinho
        if (cart.getItems().isEmpty()) {
            cartRepository.delete(cart); // Remove o carrinho completamente
        } else {
            cartRepository.save(cart); // Atualiza o carrinho com novos totais
        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartException("Cart Item not found with id: " + cartItemId));
    }

    private CartItem findAndValidateCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartException("Cart Item not found with ID " + cartItemId));
        if (!cartItem.getCart().getUser().getId().equals(userId)) {
            throw new CartException("User with ID " + userId + " is not authorized for this cart item");
        }
        return cartItem;
    }
}

