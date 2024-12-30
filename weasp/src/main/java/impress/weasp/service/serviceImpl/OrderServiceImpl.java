package impress.weasp.service.serviceImpl;


import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;
import impress.weasp.infra.exception.OrderException;
import impress.weasp.infra.exception.PaymentException;
import impress.weasp.model.*;
import impress.weasp.model.domain.OrderStatus;
import impress.weasp.model.domain.PaymentStatus;
import impress.weasp.repository.AddressRepository;
import impress.weasp.repository.OrderItemRepository;
import impress.weasp.repository.OrderRepository;
import impress.weasp.repository.ProductRepository;
import impress.weasp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;

    @Override
    public Order createOrder(User user, Address shippingAddress, Cart cart) {
        if (!user.getAddresses().contains(shippingAddress)) {
            user.getAddresses().add(shippingAddress);
        }

        Address address = addressRepository.save(shippingAddress);

        // Calcula o total do carrinho
        BigDecimal totalOrderPrice = cart.getItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        // Cria o pedido
        Order createdOrder = new Order();
        createdOrder.setUser(user);
        createdOrder.setTotalAmount(totalOrderPrice);
        createdOrder.setDeliveryAddress(address);
        createdOrder.setTotalItems(totalItems);
        createdOrder.setStatus(OrderStatus.PENDING);
        createdOrder.setPaymentStatus(PaymentStatus.PENDING);

        Order savedOrder = orderRepository.save(createdOrder);

        // Adiciona os itens do carrinho ao pedido
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setUserId(cartItem.getUserId());

            orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
        }

        savedOrder.setOrderItems(orderItems); // Associa os itens ao pedido

        return savedOrder;
    }


    @Override
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()-> new OrderException("Order not found"));
    }

    @Override
    public List<Order> userOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);

    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus, Admin admin) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found"));

        if (!admin.getRole().equals("ROLE_ADMIN")) {
            throw new OrderException("Only admins can update order status");
        }

        // Validação de transições de estado
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderException("Cannot update status of a cancelled order");
        }

        if (newStatus == OrderStatus.CANCELLED && order.getStatus() != OrderStatus.PENDING) {
            throw new OrderException("Only PENDING orders can be cancelled");
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }



    @Override
    public OrderItem getOrderItemById(Long orderItemId) {
        return orderItemRepository.findById(orderItemId).orElseThrow(()-> new OrderException("Order Item not exist"));
    }

    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public Order requestOrderCancellation(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new OrderException("User not authorized to request cancellation for this order");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new OrderException("Only pending orders can be cancelled");
        }

        order.setCancelRequest(true);
        return orderRepository.save(order);
    }

    @Override
    public Order approveCancellation(Long orderId) {
        log.info("Iniciando aprovação de cancelamento para o pedido ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Pedido não encontrado"));

        if (!order.isCancelRequest()) {
            log.warn("Nenhuma solicitação de cancelamento encontrada para o pedido ID: {}", orderId);
            throw new OrderException("Nenhuma solicitação de cancelamento encontrada para este pedido");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            log.error("Somente pedidos pendentes podem ser cancelados. Pedido ID: {}, Status Atual: {}", orderId, order.getStatus());
            throw new OrderException("Somente pedidos pendentes podem ser cancelados.");
        }

        // Atualiza o status do pedido
        order.setStatus(OrderStatus.CANCELLED);
        order.setPaymentStatus(PaymentStatus.REFUNDED);

        // Processa reembolso no Stripe
        try {
            RefundCreateParams params = RefundCreateParams.builder()
                    .setAmount(order.getTotalAmount().multiply(BigDecimal.valueOf(100)).longValue())
                    .build();

            Refund refund = Refund.create(params);
            log.info("Reembolso processado com sucesso. Pedido ID: {}, Refund ID: {}", orderId, refund.getId());
        } catch (StripeException e) {
            log.error("Erro ao processar o reembolso para o pedido ID: {}. Erro: {}", orderId, e.getMessage());
            throw new PaymentException("Erro ao processar o reembolso no Stripe.");
        }

        return orderRepository.save(order);
    }


    @Override
    public Order rejectCancellation(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found"));

        if (!order.isCancelRequest()) {
            throw new OrderException("No cancellation request found for this order");
        }

        order.setCancelRequest(false); // Remove a solicitação de cancelamento
        return orderRepository.save(order);
    }



}
