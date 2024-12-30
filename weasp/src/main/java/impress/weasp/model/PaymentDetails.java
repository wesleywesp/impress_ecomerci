package impress.weasp.model;

import impress.weasp.model.domain.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {

    private String paymentId; // Identificador no sistema
    private String mbWayPaymentId; // Identificador do gateway
    private String paymentStatus; // Ex.: PENDING, PAID
    private String mbWayPaymentStatus; // Ex.: WAITING_FOR_PAYMENT, EXPIRED
    private BigDecimal amount; // Valor do pagamento
    private String currency; // Moeda (ex.: EUR)

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // Enumeração com estados do pagamento
}