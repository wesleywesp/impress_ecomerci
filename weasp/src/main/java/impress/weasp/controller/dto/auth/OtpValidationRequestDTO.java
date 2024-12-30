package impress.weasp.controller.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OtpValidationRequestDTO(
        @NotBlank String email,
        @NotBlank String otp
) {}


