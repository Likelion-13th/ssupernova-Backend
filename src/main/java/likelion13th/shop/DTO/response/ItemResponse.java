package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemResponse {
    private Long id;
    private String itemName;
    private int price;
    private String imagePath;
    private String brand;
    private boolean isNew;

    /**
     * Item 엔티티를 ItemResponse DTO로 변환하는 정적 메서드
     */
    public static ItemResponse from(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getItem_name(),
                item.getPrice(),
                item.getImagePath(),
                item.getBrand(),
                item.isNew()
        );
    }
}

// Item 엔티티에서 필요한 정보만 추려 응답할 때 사용하는 DTO
// 정적 메서드 from()으로 변환 로직을 통일함

