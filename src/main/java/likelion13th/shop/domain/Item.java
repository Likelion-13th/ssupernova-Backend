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
    private String item_name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    @Setter
    private boolean isNew= false;

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
}