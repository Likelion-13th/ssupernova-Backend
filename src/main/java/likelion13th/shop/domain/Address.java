package likelion13th.shop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@Getter
public class Address {

    @Column(nullable = false)
    private String zipcode; // 우편번호

    @Column(nullable = false)
    private String address; // 기본 주소

    @Column(nullable = false)
    private String addressDetail; // 상세 주소

    /**
     * 기본 생성자
     * → 회원 가입 시 초기 주소 설정용
     */
    public Address() {
        this.zipcode = "10540";
        this.address = "경기도 고양시 덕양구 항공대학로 76";
        this.addressDetail = "한국항공대학교";
    }

    // → @Embeddable: User 엔티티에 내장되어 하나의 객체처럼 사용됨
}
// 사용자 주소 정보를 하나의 값 객체로 묶은 Embeddable 클래스
// User 엔티티에서 내장 필드로 활용되며, 기본값 포함
