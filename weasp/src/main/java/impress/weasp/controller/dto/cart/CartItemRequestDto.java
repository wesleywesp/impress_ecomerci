package impress.weasp.controller.dto.cart;

public record CartItemRequestDto(
        Long productId,
        Integer quantity
) {}
