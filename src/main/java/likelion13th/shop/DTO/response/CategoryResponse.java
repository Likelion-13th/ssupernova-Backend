package likelion13th.shop.DTO.response;

import jakarta.validation.constraints.NotBlank;
import likelion13th.shop.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {

    private Long id;     // 카테고리 ID

    @NotBlank(message = "카테고리 이름은 필수입니다.")
    private String name; // 카테고리 이름

    /**
     * Category 엔티티를 CategoryResponse DTO로 변환하는 정적 메서드
     * → 서비스 로직에서 반복되는 변환 코드 줄이기 위해 사용
     */
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }

    // → 클라이언트에 응답할 때 필요한 정보만 전달하도록 구성
    // → Entity와의 결합을 줄이고, 필요한 필드만 선택적으로 노출
}
// Category 엔티티에서 필요한 정보만 추려 클라이언트에 전달하는 DTO
// from() 메서드로 변환 로직을 재사용 가능하게 구성

