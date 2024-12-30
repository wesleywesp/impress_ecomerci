package impress.weasp.repository;

import impress.weasp.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByStreetAndCityAndStateAndZipCodeAndNumber(String street, String city, String state, String zipCode, String number);

    Address findByStreetAndCityAndStateAndZipCodeAndNumberAndComplement(String street, String city, String state, String zipCode, String complement, String number);
}
