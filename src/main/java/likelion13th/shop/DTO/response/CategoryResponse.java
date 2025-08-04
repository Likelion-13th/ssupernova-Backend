// CategoryResponse.java
// 클라이언트에게 전달할 카테고리 응답 DTO

package likelion13th.shop.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {

    private Long id;     // 카테고리 ID
    private String name; // 카테고리 이름
}
