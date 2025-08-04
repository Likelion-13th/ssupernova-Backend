package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
/**
 * 사용자의 마일리지를 응답하는 DTO
 */
public class UserMileageResponse {
    private int mileage; // maxMileage 값

    /**
     * User 엔티티를 기반으로 마일리지 응답 DTO를 생성하는 정적 메서드
     */
    public static UserMileageResponse from(User user) {
        return new UserMileageResponse(user.getMaxMileage());
    }
}
