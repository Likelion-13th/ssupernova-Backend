package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

/**
 * 사용자 정보를 클라이언트에게 응답할 때 사용하는 DTO
 * - 닉네임, 주소 정보, 총 결제 금액 등 표시
 */
public class UserInfoResponse {
    private String nickname;     // 사용자 닉네임 (usernickname)
    private String zipcode;      // 우편번호
    private String address;      // 기본 주소
    private String addressDetail;   // 상세 주소
    private int recentTotal;        // 최근 총 결제 금액

    /**
     * User 엔티티를 UserInfoResponse DTO로 변환하는 정적 메서드
     * → 사용자 정보 중 필요한 필드만 추려 클라이언트에 전달
     */
    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
                user.getUsernickname(),
                user.getAddress().getZipcode(),
                user.getAddress().getAddress(),
                user.getAddress().getAddressDetail(),
                user.getRecentTotal()
        );
    }

    // → User 엔티티로부터 응답에 필요한 데이터만 구성
}
// User 엔티티를 클라이언트 응답 형태로 변환하는 DTO
// 닉네임, 주소, 결제 금액 등 사용자 핵심 정보만 전달

