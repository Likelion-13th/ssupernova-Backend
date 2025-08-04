// 클라이언트에게 전달할 카테고리 응답 DTO

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
     */
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
