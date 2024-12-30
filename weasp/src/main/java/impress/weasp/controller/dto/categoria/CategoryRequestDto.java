package impress.weasp.controller.dto.categoria;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(@NotBlank String name,
                                 @NotBlank String slug) {
}
