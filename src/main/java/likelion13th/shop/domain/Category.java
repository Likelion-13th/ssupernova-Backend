// Category.java
// 카테고리 정보를 저장하는 JPA Entity 클래스
// 상품 분류 목적의 단일 테이블 구조이며, item-category 연관관계는 별도 테이블로 관리

package likelion13th.shop.domain;

import jakarta.persistence.*;
import likelion13th.shop.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Category extends BaseEntity {

    @Id // 기본 키 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 설정
    private Long id;

    @Column(nullable = false) // null 불가 설정
    private String name; // 카테고리 이름

    @ManyToMany // Item과 다대다 관계 설정
    @JoinTable(
            name = "category_item", // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "category_id"), // 현재 엔티티의 외래 키
            inverseJoinColumns = @JoinColumn(name = "item_id") // 상대 엔티티의 외래 키
    )
    private List<Item> items = new ArrayList<>(); // 연관된 상품 목록
}
