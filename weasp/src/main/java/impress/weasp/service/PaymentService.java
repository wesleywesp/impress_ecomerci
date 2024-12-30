package impress.weasp.service;


import impress.weasp.model.Order;
import impress.weasp.model.PaymentOrder;
import impress.weasp.model.User;

import java.math.BigDecimal;
import java.util.Set;

public interface PaymentService {
     PaymentOrder createOrder(User user, Order order);
    PaymentOrder getPaymentOrderById(String orderId);
    PaymentOrder getPaymentOrderBYPaymentId(String paymentId);
//    Boolean ProceedPaymentOrder(PaymentOrder paymentOrder,
//                                String paymentId,
//                                String paymentlinkId);


    String createStrypePaymentLinkById(User user, BigDecimal amount, Long paymentId);
    public Order approveCancellation(Long orderId);
//    PaymentLink createMBWayPaymentLink(User user, BigDecimal amount, Long paymentId);
}
