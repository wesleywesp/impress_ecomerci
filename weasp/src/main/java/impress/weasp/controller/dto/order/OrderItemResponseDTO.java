package impress.weasp.controller.dto.order;

import java.math.BigDecimal;

public record OrderItemResponseDTO(
        String productName,
        Integer quantity,
        BigDecimal price
) {
    public OrderItemResponseDTO(String name, BigDecimal price, Integer quantity) {
        this(name, quantity, price);
    }
}

