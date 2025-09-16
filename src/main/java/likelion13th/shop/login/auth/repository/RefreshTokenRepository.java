package likelion13th.shop.login.auth.repository;

import likelion13th.shop.domain.User;
import likelion13th.shop.login.auth.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * RefreshToken 저장소
 * - 사용자(User)와 1:1로 매핑된 RefreshToken을 조회/삭제한다.
 * - Spring Data JPA의 파생 쿼리와 @Query(JPQL)를 혼용.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // 사용자 엔티티로 RefreshToken 한 건을 조회
    // - 존재하지 않을 수 있으므로 Optional로 감싼다.
    Optional<RefreshToken> findByUser(User user);

    // 사용자 기준으로 RefreshToken을 삭제 (JPQL 직접 정의)
    // - @Modifying: DML(DELETE/UPDATE) 쿼리임을 명시
    // - 트랜잭션 경계(@Transactional)는 서비스 레이어에서 감싸는 것을 권장
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user = :user")
    void deleteByUser(@Param("user") User user);
}

/*
* [이 코드의 역할]
* - JPA 리포지토리: RefreshToken 엔티티를 DB에서 조회/삭제하는 repository
* - findByUser(User user): 사용자와 1:1로 연결된 리프레시 토큰 한 건 조회
* - deleteByUser(User user): 해당 사용자의 리프레시 토큰 삭제
*
* [왜 필요한가?]
* - 클라이언트가 보낸 refresh 토큰을 검증하려면 DB의 저장값과 사용자 기준으로 매칭해야 함 → findByUser로 점검
* - 사용자가 로그아웃하거나, 보안 사유로 토큰을 폐기할 때 행을 즉시 삭제 -> deleteByUser로...
* - 재발급 때 기존 행을 읽어와 값을 교체하고 저장.
*
* [없으면?]
* - 서비스 계층이 직접 EntityManager를 다루거나, 토큰을 메모리에만 저장하게 됨 -> 보안 bad, 지속성 bad
* - 강제 로그아웃·탈취 대응(행 삭제)이 불가능해지고, 만료까지 기다려야 함.
*
* [핵슴 로직/간단히]
* - 조회: Optional<RefreshToken> findByUser(User user) -> “이 사용자에게 저장된 리프레시 토큰 한 건을 가져와라”
* - 삭제: DELETE FROM RefreshToken rt WHERE rt.user = :user -> “이 사용자에게 매핑된 리프레시 토큰 행을 지워라”
*
*
*
*/