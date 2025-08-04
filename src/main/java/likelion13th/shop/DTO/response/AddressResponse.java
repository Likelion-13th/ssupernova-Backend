package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressResponse {
    private String zipcode;
    private String address;
    private String addressDetail;

    /**
     * Address 엔티티를 AddressResponse DTO로 변환하는 정적 메서드
     */
    public static AddressResponse from(Address address) {
        return new AddressResponse(
                address.getZipcode(),
                address.getAddress(),
                address.getAddressDetail()
        );
    }
}
