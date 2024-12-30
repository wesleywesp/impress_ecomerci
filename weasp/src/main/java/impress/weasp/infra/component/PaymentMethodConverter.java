package impress.weasp.infra.component;




import impress.weasp.model.domain.PaymentMethod;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodConverter implements Converter<String, PaymentMethod> {

    @Override
    public PaymentMethod convert(String source) {
        switch (source.toUpperCase()) {
            case "CARD":
                return PaymentMethod.CARD;
            case "PAYPAL":
                return PaymentMethod.PAYPAL;
            case "KLARNA":
                return PaymentMethod.KLARNA;
            default:
                throw new IllegalArgumentException("Método de pagamento não suportado: " + source);
        }
    }
}

