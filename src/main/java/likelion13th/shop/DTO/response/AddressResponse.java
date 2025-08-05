package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressResponse {
    private String zipcode; // 우편 번호
    private String address; // 기본 주소
    private String addressDetail; // 상세 주소

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
    // → 사용자 주소 정보를 깔끔하게 응답할 수 있도록 구성
    // → Entity에 종속되지 않고 응답에 필요한 구조만 유지
}
// Address 엔티티를 응답용 구조로 변환하는 DTO
// from() 메서드로 클라이언트 응답 시 일관된 데이터 제공
