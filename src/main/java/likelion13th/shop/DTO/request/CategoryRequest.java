package likelion13th.shop.DTO.request;

import lombok.Getter;

@Getter
public class CategoryRequest {

    private String name; // 생성 또는 수정할 카테고리 이름

}
// 클라이언트가 보낸 카테고리 이름을 전달받는 DTO
// 서비스 계층에서 Category 엔티티로 변환해 처리할 때 사용
