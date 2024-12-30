package impress.weasp.service.serviceImpl;

import com.stripe.Stripe;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import impress.weasp.infra.exception.OrderException;
import impress.weasp.infra.exception.PaymentException;
import impress.weasp.model.Order;
import impress.weasp.model.PaymentOrder;
import impress.weasp.model.User;
import impress.weasp.model.domain.OrderStatus;
import impress.weasp.model.domain.PaymentStatus;
import impress.weasp.repository.OrderRepository;
import impress.weasp.repository.PaymentOrderRepository;
import impress.weasp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final PaymentOrderRepository paymentOrderRepository;
    private  final OrderRepository orderRepository;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;
    @Override
    public PaymentOrder createOrder(User user, Order order) {
        BigDecimal amount = order.getTotalAmount();

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);
        paymentOrder.setOrder(order);
        paymentOrder.setPaymentIntentId(null);
        // Associa o pedido ao pagamento

        PaymentOrder savedPaymentOrder = paymentOrderRepository.save(paymentOrder);

        // Atualize o pedido com o pagamento associado
        order.setPaymentOrder(savedPaymentOrder);
        orderRepository.save(order);

        return savedPaymentOrder;
    }




    @Override
    public PaymentOrder getPaymentOrderById(String orderId) {

        return paymentOrderRepository.findById(Long.valueOf(orderId)).orElseThrow(() -> new PaymentException("Payment Order not found"));
    }

    @Override
    public PaymentOrder getPaymentOrderBYPaymentId(String paymentId) {
        PaymentOrder paymentOrder = paymentOrderRepository.findByPaymentLinkId(paymentId);
        if (paymentOrder == null) {
            throw new PaymentException("Payment Order not found");
        }
        return paymentOrder;
    }

//    @Override
//    public Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentlinkId) {
//        return null;
//    }

    @Override
    public String createStrypePaymentLinkById(User user, BigDecimal amount, Long paymentId) {
        log.info("Iniciando criação do link de pagamento. Payment ID: {}, Amount: {}", paymentId, amount);

        Stripe.apiKey = stripeSecretKey;

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }

        // Calcular tempo de expiração (30 minutos)
        long expiresAt = Instant.now().plus(30, ChronoUnit.MINUTES).getEpochSecond();

        // Criar parâmetros para a sessão
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/" + paymentId)
                .setCancelUrl("http://localhost:3000/payment-cancel/" + paymentId)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("eur")
                                .setUnitAmount(amount.multiply(BigDecimal.valueOf(100)).longValue())
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("EcomerciMultiVender")
                                        .build())
                                .build())
                        .build())
                .setExpiresAt(expiresAt)
                .build();

        try {
            // Criar sessão de pagamento no Stripe
            Session session = Session.create(params);
            log.info("Sessão de pagamento criada com sucesso. Session ID: {}", session.getId());

            // Atualizar o PaymentOrder com os dados do Stripe
            PaymentOrder paymentOrder = paymentOrderRepository.findById(paymentId)
                    .orElseThrow(() -> new PaymentException("Pedido de pagamento não encontrado"));
            paymentOrder.setPaymentIntentId(session.getPaymentIntent());
            paymentOrder.setPaymentLinkId(session.getId());
            paymentOrderRepository.save(paymentOrder);

            return session.getUrl();
        } catch (Exception e) {
            log.error("Erro ao criar o link de pagamento no Stripe. Detalhes: {}", e.getMessage());
            throw new PaymentException("Erro ao criar o link de pagamento no Stripe.");
        }
    }



    @Override
    public Order approveCancellation(Long orderId) {
       PaymentOrder paymentOrder= paymentOrderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderException("Payment Order not found"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found"));

        if (!order.isCancelRequest()) {
            throw new OrderException("No cancellation request found for this order");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setPaymentStatus(PaymentStatus.REFUNDED);

        // Integração com o Stripe
        try {
            RefundCreateParams params = RefundCreateParams.builder()
                    .setPaymentIntent(paymentOrder.getPaymentIntentId()) // Obtém o PaymentIntent associado ao pedido
                    .setAmount(order.getTotalAmount().multiply(BigDecimal.valueOf(100)).longValue()) // Valor do reembolso
                    .build();

            Refund.create(params);
        } catch (Exception e) {
            throw new PaymentException("Failed to process refund: " + e.getMessage());
        }

        return orderRepository.save(order);
    }

}



