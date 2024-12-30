package impress.weasp.controller;

import impress.weasp.controller.dto.Adrress.AddressRequestDTO;
import impress.weasp.controller.dto.Adrress.AddressResponseDTO;
import impress.weasp.controller.dto.order.OrderItemResponseDTO;
import impress.weasp.controller.dto.order.OrderResponse;
import impress.weasp.controller.dto.order.OrderResponseDTO;
import impress.weasp.infra.exception.AdminException;
import impress.weasp.infra.exception.CustumerException;
import impress.weasp.infra.security.JwtTokenProvider;
import impress.weasp.model.Admin;
import impress.weasp.model.Order;
import impress.weasp.model.OrderItem;
import impress.weasp.model.User;
import impress.weasp.model.domain.OrderStatus;
import impress.weasp.service.AdminService;
import impress.weasp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/order")
public class OrderControllerAdmin {

    private static final Logger log = LoggerFactory.getLogger(OrderControllerAdmin.class);

    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrdersHandler(
            @RequestHeader("Authorization") String token) throws AdminException {
        String email = jwtTokenProvider.validateToken(token);
        Admin admin = adminService.getAdminProfile(email);

        List<Order> orders = orderService.getAllOrder();

        // Converte as entidades em DTOs
        List<OrderResponseDTO> response = orders.stream()
                .map(order -> new OrderResponseDTO(
                        order.getId(),
                        order.getUser().getEmail(),
                        mapToOrderItemResponse(order.getOrderItems()),
                        order.getStatus(),
                        order.getPaymentStatus(),
                        new AddressResponseDTO(
                                order.getDeliveryAddress().getStreet(),
                                order.getDeliveryAddress().getNumber(),
                                order.getDeliveryAddress().getComplement(),
                                order.getDeliveryAddress().getCity(),
                                order.getDeliveryAddress().getState(),
                                order.getDeliveryAddress().getCountry(),
                                order.getDeliveryAddress().getZipCode()
                        ),
                        order.getTotalAmount(),
                        order.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }




    @PatchMapping("/{orderId}/status/{newStatus}")
    public ResponseEntity<OrderResponseDTO> updateOrderStatusHandler(
            @PathVariable Long orderId,
            @PathVariable OrderStatus newStatus,
            @RequestHeader("Authorization") String token) {
        // Valida o token e obtém o perfil do administrador
        String email = jwtTokenProvider.validateToken(token);
        Admin admin = adminService.getAdminProfile(email);

        // Atualiza o status do pedido
        Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus, admin);

        // Mapeia o pedido atualizado para OrderResponseDTO
        OrderResponseDTO response = new OrderResponseDTO(
                updatedOrder.getId(),
                updatedOrder.getUser().getEmail(),
                updatedOrder.getOrderItems().stream()
                        .map(item -> new OrderItemResponseDTO(
                                item.getId(),
                                item.getProduct().getName(),
                                item.getQuantity(),
                                item.getPrice()
                        ))
                        .collect(Collectors.toList()),
                updatedOrder.getStatus(),
                updatedOrder.getPaymentStatus(),
                new AddressResponseDTO(
                        updatedOrder.getDeliveryAddress().getStreet(),
                        updatedOrder.getDeliveryAddress().getNumber(),
                        updatedOrder.getDeliveryAddress().getComplement(),
                        updatedOrder.getDeliveryAddress().getCity(),
                        updatedOrder.getDeliveryAddress().getState(),
                        updatedOrder.getDeliveryAddress().getCountry(),
                        updatedOrder.getDeliveryAddress().getZipCode()
                ),
                updatedOrder.getTotalAmount(),
                updatedOrder.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{orderId}/approve-cancel")
    public ResponseEntity<OrderResponse> approveCancellation(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token) {

        String email = jwtTokenProvider.validateToken(token);
        Admin admin = adminService.getAdminProfile(email);

        log.info("Aprovação de cancelamento iniciada pelo Admin: {} para o pedido ID: {}", admin.getEmail(), orderId);

        Order order = orderService.approveCancellation(orderId);

        log.info("Aprovação de cancelamento concluída pelo Admin: {} para o pedido ID: {}", admin.getEmail(), orderId);
        return ResponseEntity.ok(new OrderResponse(order));
    }



    @PatchMapping("/{orderId}/reject-cancel")
    public ResponseEntity<OrderResponse> rejectCancellation(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token) {

        Order order = orderService.rejectCancellation(orderId);
        return ResponseEntity.ok(new OrderResponse(order));
    }

    private List<OrderItemResponseDTO> mapToOrderItemResponse(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> new OrderItemResponseDTO(
                        orderItem.getId(),
                        orderItem.getProduct().getName(),
                        orderItem.getProduct().getPrice(),
                        orderItem.getQuantity()
                ))
                .collect(Collectors.toList());
    }


}
