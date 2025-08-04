// 사용자 정보 응답 DTO
package likelion13th.shop.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private String nickname;         // usernickname
    private String zipcode;
    private String address;
    private String addressDetail;
    private int recentTotal;
}
