package impress.weasp.controller.dto.auth;

import impress.weasp.model.domain.USER_ROLE;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignupRequestDTO(
        @NotBlank String email,
        @NotBlank String password,
        USER_ROLE role
) {}

