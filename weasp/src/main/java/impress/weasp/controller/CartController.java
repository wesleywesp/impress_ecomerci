package impress.weasp.controller;

import impress.weasp.controller.dto.auth.ApiResponse;
import impress.weasp.controller.dto.cart.CartItemRequestDto;
import impress.weasp.controller.dto.cart.CartItemResponseDto;
import impress.weasp.controller.dto.cart.CartItemUpdateQuantityDto;
import impress.weasp.controller.dto.cart.CartResponseDto;
import impress.weasp.infra.security.JwtTokenProvider;
import impress.weasp.model.Cart;
import impress.weasp.model.CartItem;
import impress.weasp.model.Product;
import impress.weasp.model.User;
import impress.weasp.service.CaritemService;
import impress.weasp.service.CartService;
import impress.weasp.service.ProductService;
import impress.weasp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CaritemService cartItemService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ProductService productService;

    /**
     * Obtém o carrinho do usuário logado.
     */
    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.validateToken(token);
        User user = userService.getUserProfile(email);

        Cart cart = cartService.findUserCard(user);
        CartResponseDto response = mapToCartResponseDto(cart);
        return ResponseEntity.ok(response);
    }

    /**
     * Adiciona um item ao carrinho do usuário logado.
     */
    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addItemToCart(@RequestBody @Valid CartItemRequestDto request,
                                                         @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.validateToken(token);
        User user = userService.getUserProfile(email);
        Product product = productService.getProductById(request.productId());

        CartItem cartItem = cartService.addProductToCart(user, product, request.quantity());
        CartResponseDto response = mapToCartResponseDto(cartItem.getCart());
        return ResponseEntity.ok(response);
    }

    /**
     * Remove um item do carrinho do usuário logado.
     */
    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartItemId,
                                                          @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.validateToken(token);
        User user = userService.getUserProfile(email);

        cartItemService.removeCartItem(user.getId(), cartItemId);
        ApiResponse response = new ApiResponse("Product Removed From Cart Successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza um item no carrinho do usuário logado.
     */
    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItemResponseDto> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid CartItemUpdateQuantityDto updateDto) {

        // Valida o token e obtém o email do cliente
        String email = jwtTokenProvider.validateToken(token);
        User user = userService.getUserProfile(email);

        // Atualiza a quantidade do item no carrinho
        CartItem updatedCartItem = cartItemService.updateCartItemQuantity(user.getId(), cartItemId, updateDto.quantity());

        // Retorna o DTO de resposta
        CartItemResponseDto response = mapToCartItemResponseDto(updatedCartItem);
        return ResponseEntity.ok(response);
    }



    // Mapeia Cart para CartResponseDto
    private CartResponseDto mapToCartResponseDto(Cart cart) {
        List<CartItemResponseDto> items = cart.getItems().stream()
                .map(this::mapToCartItemResponseDto)
                .collect(Collectors.toList());

        return new CartResponseDto(cart.getId(), items, cart.getTotalAmount());
    }

    // Mapeia CartItem para CartItemResponseDto
    private CartItemResponseDto mapToCartItemResponseDto(CartItem cartItem) {
        return new CartItemResponseDto(
                cartItem.getId(),
                cartItem.getProduct().getName(),
                cartItem.getQuantity(),
                cartItem.getPrice()
        );
    }
}

