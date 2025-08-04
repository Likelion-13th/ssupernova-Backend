// CategoryRequest.java
// 클라이언트에서 전달하는 카테고리 생성/수정 요청을 받을 DTO

package likelion13th.shop.DTO.request;

import lombok.Getter;

@Getter
public class CategoryRequest {

    private String name; // 생성 또는 수정할 카테고리 이름
}
