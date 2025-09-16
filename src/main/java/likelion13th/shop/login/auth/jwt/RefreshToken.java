package likelion13th.shop.login.auth.jwt;

import jakarta.persistence.*;
import likelion13th.shop.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * RefreshToken 엔티티
 * - 한 명의 사용자(User)당 Refresh Token 1개를 보관하는 테이블
 * - Shared PK(공유 PK) 대신 "별도 PK(id) + users_id UNIQUE" 방식으로 설계하여
 *   식별자 null 문제(null identifier) 및 연관관계 초기화 이슈를 피함
 */
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ 자체 PK 사용 (AUTO_INCREMENT)
    private Long id;

    /**
     * 사용자와 1:1 관계 (FK: users_id)
     * - 기본적으로 @OneToOne는 EAGER 지연로딩이 기본값이지만, 성능을 위해 LAZY로 명시
     * - users_id에는 UNIQUE 제약을 걸어 "사용자당 1행"만 허용
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", unique = true) // ✅ FK + UNIQUE 제약(사용자당 1개)
    private User user;

    /**
     * 실제 Refresh Token 문자열
     * - 보안을 위해 절대 로그에 원문 출력 금지
     * - 필요 시 길이 제한(@Column(length=...)) 및 NOT NULL 제약을 추가할 수 있음
     */
    private String refreshToken;

    /**
     * 만료 시각(예: epoch millis)
     * - 이름은 '유효기간'이지만 '남은 기간'이 아닌 '만료 시각'으로 사용 중
     * - 혼동을 줄이려면 expiresAt/expiryEpochMillis 같은 명칭을 고려
     */
    private Long ttl;

    /** 새 토큰으로 교체할 때 사용 */
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /** 만료 시각 갱신 */
    public void updateTtl(Long ttl) {
        this.ttl = ttl;
    }

    // (선택) 가독성 향상을 위한 헬퍼 메서드 예시
    // public boolean isExpired() { return ttl != null && System.currentTimeMillis() >= ttl; }
}

/*
* [이 코드의 역할?]
* - JPA 엔티티로서 DB에 “사용자별 리프레시 토큰 1개”를 저장/교체 -> 안전한 자동 재로그인을 가능하게 만드는 Entity
* - User와 1:1 관계 + ttl(만료 시각, epoch millis)로 유효기간을 관리
*
* [왜 필요한가?]
* - Access 토큰은 짧게 만료되어 안전하지만 불편 -> Refresh 토큰을 서버가 보관하면 사용자는 다시 로그인 없이 새 Access를 받을 수 있음
* - 클라이언트의 Refresh와 DB 저장값 비교 -> 정말 우리 서버가 발급했는지 점검
* - 사용자 당 1개의 RefreshToken -> 로그인 운영 정책 단순화 가능
*
* [없으면?]
* - 클라이언트가 들고온 Refresh가 진짜 우리 서버가 발급한 것인지 확인 불가
* - 강제 로그아웃/권한 회수를 즉시 못함 -> RefreshToken 만료 시까지 방치됨
*/