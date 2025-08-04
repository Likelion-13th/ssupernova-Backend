// UserService.java
// 사용자 정보, 마일리지, 주소 조회 로직을 담당하는 서비스 계층

package likelion13th.shop.service;

import likelion13th.shop.DTO.response.AddressResponse;
import likelion13th.shop.DTO.response.UserInfoResponse;
import likelion13th.shop.DTO.response.UserMileageResponse;
import likelion13th.shop.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    // 사용자 정보 응답 생성
    public UserInfoResponse getUserInfo(User user) {
        return new UserInfoResponse(
                user.getUsernickname(),                                  // 닉네임
                user.getAddress().getZipcode(),                          // 우편번호
                user.getAddress().getAddress(),                          // 주소
                user.getAddress().getAddressDetail(),                    // 상세주소
                user.getRecentTotal()                                    // 최근 총 구매액
        );
    }

    // 마일리지 응답 생성
    public UserMileageResponse getUserMileage(User user) {
        return new UserMileageResponse(user.getMaxMileage());
    }


}
