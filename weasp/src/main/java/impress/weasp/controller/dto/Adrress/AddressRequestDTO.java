package impress.weasp.controller.dto.Adrress;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequestDTO(
        @NotBlank(message = "Street cannot be blank")
        @Size(max = 100, message = "Street cannot exceed 100 characters")
        String street,

        @NotBlank(message = "Number cannot be blank")
        @Size(max = 10, message = "Number cannot exceed 10 characters")
        String number,

        @Size(max = 50, message = "Complement cannot exceed 50 characters")
        String complement,

        @NotBlank(message = "City cannot be blank")
        @Size(max = 50, message = "City cannot exceed 50 characters")
        String city,

        @NotBlank(message = "State cannot be blank")
        @Size(max = 50, message = "State cannot exceed 50 characters")
        String state,

        @NotBlank(message = "Country cannot be blank")
        @Size(max = 50, message = "Country cannot exceed 50 characters")
        String country,

        @NotBlank(message = "Zip code cannot be blank")
        @Pattern(regexp = "\\d{5}(-\\d{4})?", message = "Zip code must be in the format 12345 or 12345-6789")
        String zipCode
) {}

