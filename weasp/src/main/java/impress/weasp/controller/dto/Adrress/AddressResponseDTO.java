package impress.weasp.controller.dto.Adrress;

public record AddressResponseDTO(
        String street,
        String number,
        String complement,
        String city,
        String state,
        String country,
        String zipCode
) {

}
