// ItemRequest.java
// 상품 생성 및 수정 요청 시 사용하는 DTO

package likelion13th.shop.DTO.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ItemRequest {
    private String itemName;
    private int price;
    private String imagePath;
    private String brand;
    private boolean isNew;
    private List<Long> categoryIds; // 연관 카테고리 ID 리스트
}
