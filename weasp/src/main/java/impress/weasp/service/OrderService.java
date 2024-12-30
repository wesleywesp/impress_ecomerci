package impress.weasp.service;

import impress.weasp.controller.dto.Adrress.AddressRequestDTO;
import impress.weasp.controller.dto.order.OrderItemRequest;
import impress.weasp.model.*;
import impress.weasp.model.domain.OrderStatus;

import java.util.List;
import java.util.Set;


public interface OrderService {
    Order createOrder(User user, Address shippingAddress, Cart cart);
    Order findOrderById(Long orderId);
    List<Order>userOrderHistory(Long userId);
    Order updateOrderStatus(Long orderId, OrderStatus orderStatus, Admin admin);
    OrderItem getOrderItemById(Long orderItemId);

    List<Order> getAllOrder();
     Order requestOrderCancellation(Long orderId, User user);
    public Order approveCancellation(Long orderId);
    public Order rejectCancellation(Long orderId);
}
