// ItemResponse.java
// 상품 정보를 응답할 때 사용하는 DTO

package likelion13th.shop.DTO.response;

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
}
