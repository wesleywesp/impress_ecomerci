package impress.weasp.controller.dto.categoria;

import java.time.LocalDateTime;

public record CategoryResponseDto(
        Long id,
        String name,
        String slug,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}