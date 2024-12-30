package impress.weasp.controller.dto.order;




import impress.weasp.controller.dto.Adrress.AddressRequestDTO;
import impress.weasp.controller.dto.Adrress.AddressResponseDTO;
import impress.weasp.model.domain.OrderStatus;
import impress.weasp.model.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        String email,
        List<OrderItemResponseDTO> items,
        OrderStatus status,
        PaymentStatus paymentStatus,
        AddressResponseDTO address,
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {

    public OrderResponseDTO(Long id, List<OrderItemResponseDTO> orderItemResponseDTOS, String name, String name1, AddressResponseDTO addressResponseDTO, BigDecimal totalAmount, LocalDateTime createdAt) {
        this(id, name, orderItemResponseDTOS, OrderStatus.PENDING, PaymentStatus.PENDING, addressResponseDTO, totalAmount, createdAt);
    }

}


