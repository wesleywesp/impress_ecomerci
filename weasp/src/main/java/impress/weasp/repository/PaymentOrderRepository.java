package impress.weasp.repository;

import impress.weasp.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    PaymentOrder findByPaymentLinkId(String paymentId);

    Optional<PaymentOrder> findByOrderId(Long orderId);
}
