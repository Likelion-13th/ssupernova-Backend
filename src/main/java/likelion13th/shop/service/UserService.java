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
        return UserInfoResponse.from(user);
    }

    // 마일리지 응답 생성
    public UserMileageResponse getUserMileage(User user) {
        return UserMileageResponse.from(user);
    }


}
