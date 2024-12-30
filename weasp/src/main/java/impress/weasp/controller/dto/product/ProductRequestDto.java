package impress.weasp.controller.dto.product;

import java.math.BigDecimal;

public record ProductRequestDto(
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String categorySlug, // Identifica a categoria pelo slug
        String imageUrl
) {}
