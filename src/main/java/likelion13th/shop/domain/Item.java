package likelion13th.shop.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import likelion13th.shop.domain.entity.BaseEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(nullable = false)
    private String item_name; // 상품 이름

    @Column(nullable = false)
    private int price; // 상품 가격

    @Column(nullable = false)
    private String imagePath; // 이미지 경로

    @Column(nullable = false)
    private String brand; // 브랜드명

    @Column(nullable = false)
    @Setter
    private boolean isNew= false; // 신상품 여부

    //Category와 다대다 연관관계 설정
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    /**
     * 주문과의 일대다 관계 설정
     * - 하나의 상품은 여러 주문에 포함될 수 있음
     * - Order.item 필드에 의해 매핑
     * - cascade = ALL: 상품 삭제 시 연결된 주문도 삭제됨
     * - @JsonIgnore: 순환참조 방지
     */
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    /**
     * 커스텀 생성자
     * - 일부 필드만 받아 초기화할 때 사용
     */
    public Item(String item_name, int price, String imagePath, String brand, boolean isNew) {
        this.item_name = item_name;
        this.price = price;
        this.imagePath = imagePath;
        this.brand = brand;
        this.isNew= false;
    }

    // → 상품 정보, 카테고리 및 주문 연관관계를 포함한 도메인 엔티티
    // → 상품 등록/조회 시 필요한 기본 구조를 제공함
}
// 상품 정보와 관련 연관관계를 정의한 JPA 엔티티
// 카테고리와 다대다, 주문과 일대다 관계 설정
