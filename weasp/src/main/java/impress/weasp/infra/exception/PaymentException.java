package impress.weasp.infra.exception;

public class PaymentException  extends RuntimeException{
    public PaymentException(String message) {
        super(message);
    }
}
