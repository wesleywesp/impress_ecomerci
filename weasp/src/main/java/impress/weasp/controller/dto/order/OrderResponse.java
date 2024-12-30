package impress.weasp.controller.dto.order;

import impress.weasp.model.Order;

import java.math.BigDecimal;

public record OrderResponse(Long id, String status, BigDecimal totalAmount, boolean cancelRequest) {
    public OrderResponse(Order order) {
        this(order.getId(), order.getStatus().name(), order.getTotalAmount(), order.isCancelRequest());
    }
}
