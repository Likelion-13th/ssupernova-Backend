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

    // → 카테고리는 상품 분류 기준이라 단순 구조로 설계
    // → 다대다 관계이므로 중간 테이블 직접 지정
    // → List<Item>으로 연관 상품 쉽게 조회 가능

}

// 카테고리 정보를 저장하는 JPA 엔티티. 상품과 다대다 관계를 맺고 중간 테이블로 연결함.
// 상품 분류 기능 구현을 위한 핵심 도메인.

