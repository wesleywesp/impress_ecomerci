package impress.weasp.controller;

import impress.weasp.controller.dto.order.OrderResponse;
import impress.weasp.controller.dto.order.OrderResponseDTO;
import impress.weasp.infra.exception.AdminException;
import impress.weasp.infra.exception.CustumerException;
import impress.weasp.infra.security.JwtTokenProvider;
import impress.weasp.model.Admin;
import impress.weasp.model.Order;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/order")
public class OrderControllerAdmin {

    private static final Logger log = LoggerFactory.getLogger(OrderControllerAdmin.class);

    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrdersHandler(@RequestHeader("Authorization") String token) throws AdminException {
        String email = jwtTokenProvider.validateToken(token);
        Admin admin =adminService.getAdminProfile(email);
        List<Order> orders = orderService.getAllOrder();
        return ResponseEntity.ok(orders);
    }



    @PatchMapping("/{orderId}/status/{newStatus}")
    public ResponseEntity<Order> updateOrderStatusHandler(@PathVariable Long orderId,
                                                          @PathVariable OrderStatus newStatus,
                                                          @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.validateToken(token);
        Admin admin = adminService.getAdminProfile(email);

        Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus, admin);
        return ResponseEntity.ok(updatedOrder);
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


}
