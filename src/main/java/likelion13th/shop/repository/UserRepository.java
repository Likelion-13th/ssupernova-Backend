package likelion13th.shop.repository;

// 사용자 데이터 접근
import likelion13th.shop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // user_id 기반 사용자 찾기 (feature/4)
    Optional<User> findById(Long userId);

    // user_id 존재 여부 확인
    boolean existsById(Long userId);

    // providerId(카카오 고유 ID) 기반 조회 (feature/4)
    // → OAuth 로그인 식별자로 사용됨
    Optional<User> findByProviderId(String providerId);

    boolean existsByProviderId(String providerId);

    // usernickname(닉네임) 기반 사용자 찾기 (develop)
    List<User> findByUsernickname(String usernickname);

    // 향후 필요 시 사용할 수 있도록 주석 유지
    //Optional<User> findByKakaoId(String kakaoId);

}

// 사용자 인증 및 조회를 위한 Repository
// providerId, nickname 등 다양한 조건으로 사용자 검색 가능

