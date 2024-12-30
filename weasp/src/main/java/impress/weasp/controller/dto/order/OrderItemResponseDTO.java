package impress.weasp.controller.dto.order;

import java.math.BigDecimal;

public record OrderItemResponseDTO(
        Long id,
        String productName,
        Integer quantity,
        BigDecimal price
) {

    public OrderItemResponseDTO(Long id, String name, BigDecimal price, Integer quantity) {
        this(id,name, quantity, price);
    }
}

