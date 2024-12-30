package impress.weasp.controller.dto.order;

import impress.weasp.model.Address;

public record OrderItemRequest(
        Long productId,
        Integer quantity
)
{
}

