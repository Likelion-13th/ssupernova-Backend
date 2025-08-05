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

    /** 사용자 정보 조회 **/
    public UserInfoResponse getUserInfo(User user) {
        // → User 엔티티에서 프로필 정보만 추출해서 응답용 DTO로 변환
        return UserInfoResponse.from(user);
    }

    /** 마일리지 조회 **/
    public UserMileageResponse getUserMileage(User user) {
        // → User 엔티티에서 마일리지 값만 추출
        return UserMileageResponse.from(user);
    }

    // → 인증된 사용자 정보를 기반으로 필요한 정보만 가공해서 반환
    // → 도메인 객체 대신 응답 전용 DTO를 사용해 API 응답 형식 통일
}
// 사용자 프로필과 마일리지 정보를 조회하는 서비스
// 엔티티에서 필요한 정보만 추출해 DTO로 변환

