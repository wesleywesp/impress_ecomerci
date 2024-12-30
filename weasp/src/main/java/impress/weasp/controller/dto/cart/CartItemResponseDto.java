package impress.weasp.controller.dto.cart;

import java.math.BigDecimal;

public record CartItemResponseDto(
        Long id,
        String productName,
        Integer quantity,
        BigDecimal price
) {}