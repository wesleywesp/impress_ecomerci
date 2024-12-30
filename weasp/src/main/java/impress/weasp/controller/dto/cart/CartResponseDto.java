package impress.weasp.controller.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponseDto(
        Long id,
        List<CartItemResponseDto> items,
        BigDecimal totalAmount
) {}
