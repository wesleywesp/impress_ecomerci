package impress.weasp.service.serviceImpl;

import impress.weasp.infra.exception.CartException;
import impress.weasp.model.Cart;
import impress.weasp.model.CartItem;
import impress.weasp.model.Product;
import impress.weasp.model.User;
import impress.weasp.repository.CartItemRepository;
import impress.weasp.repository.CartRepository;
import impress.weasp.repository.ProductRepository;
import impress.weasp.service.CartService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;


    @Override
    public CartItem addProductToCart(User user, Product product, Integer quantity) {
        Cart cart = findUserCard(user); // Busca o carrinho do usuário ou cria um novo

        CartItem isPresent = cartItemRepository.findByCartIdAndProduct(cart.getId(), product);
        if (isPresent == null) {
            // Adicionando novo item ao carrinho
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setCart(cart);
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

            cart.getItems().add(cartItem);
            cartItemRepository.save(cartItem);
        } else {
            // Atualizando item existente no carrinho
            int oldQuantity = isPresent.getQuantity();
            isPresent.setQuantity(oldQuantity + quantity);
            isPresent.setPrice(product.getPrice().multiply(BigDecimal.valueOf(isPresent.getQuantity())));
            cartItemRepository.save(isPresent);
        }

        // Recalcular totais
        cart.updateTotalAmount(); // Atualiza o totalAmount
        cart.setTotalItems(cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum()); // Soma todas as quantidades de itens no carrinho

        // Salvar carrinho atualizado
        cartRepository.save(cart);

        // Retorna o item atualizado
        return cart.getItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Erro ao atualizar item no carrinho."));
    }


    @Override
    public Cart findUserCard(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            cart = Cart.builder()
                    .user(user)
                    .items(new ArrayList<>())
                    .totalAmount(BigDecimal.ZERO)
                    .totalItems(0)
                    .build();
            cart = cartRepository.save(cart);
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalItems = 0;

        for (CartItem cartItem : cart.getItems()) {
            totalItems += cartItem.getQuantity();
            totalPrice = totalPrice.add(cartItem.getPrice());
        }

        cart.setTotalAmount(totalPrice);
        cart.setTotalItems(totalItems);

        return cartRepository.save(cart);
    }

}
