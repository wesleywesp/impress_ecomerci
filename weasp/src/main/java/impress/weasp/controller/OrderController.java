package impress.weasp.controller;




import impress.weasp.controller.dto.Adrress.AddressRequestDTO;
import impress.weasp.controller.dto.order.OrderItemResponseDTO;
import impress.weasp.controller.dto.order.OrderResponse;
import impress.weasp.controller.dto.order.OrderResponseDTO;
import impress.weasp.controller.dto.payment.PaymentLinkResponse;
import impress.weasp.infra.exception.CartException;
import impress.weasp.infra.exception.CustumerException;
import impress.weasp.infra.security.JwtTokenProvider;
import impress.weasp.model.*;
import impress.weasp.model.domain.PaymentMethod;
import impress.weasp.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class OrderController {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final CartService cartService;

    @Transactional
    @PostMapping("/order")
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(
            @RequestBody @Valid AddressRequestDTO addressDto,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization") String token) throws CustumerException {
        String email = jwtTokenProvider.validateToken(token);
        User user = userService.getUserProfile(email);
        Cart cart = cartService.findUserCard(user);
        if (cart.getItems().isEmpty()) {
            throw new CartException("Carrinho vazio. Adicione itens antes de criar o pedido.");
        }

        Address address = new Address(addressDto, user);
        Order orders = orderService.createOrder(user, address, cart);

        PaymentOrder paymentOrder =paymentService.createOrder(user, orders);
//
        PaymentLinkResponse response = new PaymentLinkResponse();

        String paymentUrl = paymentService.createStrypePaymentLinkById(user, paymentOrder.getAmount()
                ,paymentOrder.getId());
        response.setPayment_link_url(paymentUrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/user")
    public ResponseEntity<List<OrderResponseDTO>> getOrderHistory(@RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.validateToken(token);
        User user = userService.getUserProfile(email);

        List<OrderResponseDTO> response = orderService.userOrderHistory(user.getId()).stream()
                .map(order -> new OrderResponseDTO(
                        order.getId(),
                        mapToOrderItemResponse(order.getOrderItems()),
                        order.getStatus().name(),
                        order.getTotalAmount(),
                        order.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> findOrderById(@PathVariable Long orderId,
                                                          @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.validateToken(token);
        User user = userService.getUserProfile(email);

        Order order = orderService.findOrderById(orderId);

        OrderResponseDTO response = new OrderResponseDTO(
                order.getId(),
                mapToOrderItemResponse(order.getOrderItems()),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }



    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> findOrderItemById(@PathVariable Long orderItemId,
                                                       @RequestHeader("Authorization") String token) throws CustumerException {
        String email = jwtTokenProvider.validateToken(token);
        User user = userService.getUserProfile(email);
        return ResponseEntity.ok(orderService.getOrderItemById(orderItemId));
    }
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> requestCancellation(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token) {

        String email = jwtTokenProvider.validateToken(token);
        User user = userService.getUserProfile(email);

        Order order = orderService.requestOrderCancellation(orderId, user);
        return ResponseEntity.ok(new OrderResponse(order));
    }

    private List<OrderItemResponseDTO> mapToOrderItemResponse(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> new OrderItemResponseDTO(
                        orderItem.getProduct().getName(),
                        orderItem.getProduct().getPrice(),
                        orderItem.getQuantity()
                ))
                .collect(Collectors.toList());
    }

}



