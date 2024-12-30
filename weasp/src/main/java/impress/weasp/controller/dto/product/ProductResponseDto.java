package impress.weasp.controller.dto.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String categoryName, // Nome da categoria no retorno
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
