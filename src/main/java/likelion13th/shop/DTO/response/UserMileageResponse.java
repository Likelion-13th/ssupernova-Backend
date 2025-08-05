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
     *  → 마일리지 정보만 클라이언트에 전달
     */
    public static UserMileageResponse from(User user) {
        return new UserMileageResponse(user.getMaxMileage());
    }
    // → 전체 사용자 정보 대신 마일리지만 응답할 때 사용
    // → 재사용 가능한 변환 메서드로 응답 구조 통일
}

// User 엔티티에서 마일리지만 추출해 응답하는 DTO

