package impress.weasp.controller.dto.order;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        List<OrderItemResponseDTO> items,
        String status,
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {

}


