package likelion13th.shop.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressResponse {
    private String zipcode;
    private String address;
    private String addressDetail;
}
