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
    private List<Long> categoryIds;  // 상품이 속한 카테고리 ID 목록 (연관 관계 설정용)
    // → 상품 등록/수정 시 클라이언트가 보내는 필드 구조
    // → categoryIds는 다대다 연관관계 매핑을 위한 키값 전달용
}

// 상품 등록/수정 요청을 받을 때 사용하는 DTO
// 연관 카테고리 ID 목록도 함께 전달받아 관계 설정에 사용
