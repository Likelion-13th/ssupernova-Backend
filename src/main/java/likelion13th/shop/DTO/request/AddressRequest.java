package likelion13th.shop.DTO.request;

import lombok.Getter;

/**
 * 사용자 주소 정보 수정을 위한 요청 DTO
 * - User 엔티티 내 임베디드 Address 필드 갱신에 사용됩니다.
 */
@Getter
public class AddressRequest {
    private String zipcode;  // 우편번호
    private String address;  // 기본 주소
    private String addressDetail; // 상세 주소
}
