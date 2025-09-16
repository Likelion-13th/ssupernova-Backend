package likelion13th.shop.login.auth.service;

import io.swagger.v3.oas.annotations.servers.Server;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Slf4j
@Server
@Component
public class JpaUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public JpaUserDetailsManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String providerId) throws UsernameNotFoundException {
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> {
                    log.warn("사용자 정보 없음");
                    throw new GeneralException(ErrorCode.USER_NOT_FOUND);
                });

        return CustomUserDetails.fromEntity(user);
    }

    @Override
    public void createUser(UserDetails user) {
        if (userExists(user.getUsername())) {
            throw new GeneralException(ErrorCode.ALREADY_USED_NICKNAME);
        }

        try {
            User newUser = ((CustomUserDetails) user).toEntity();

            userRepository.save(newUser);
            log.info("사용자 생성 완료");
        } catch (ClassCastException e) {
            log.error("UserDetails -> CustomUserDetails로 변환 실패");
            throw new GeneralException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean userExists(String providerId) {
        return userRepository.existsByProviderId(providerId);
    }

    /**
     * // 사용자 정보 업데이트 (현재 미구현)
     * // - 소셜 로그인 시 서버에서 직접 갱신할 데이터 범위가 명확해진 뒤 구현 권장
     */
    @Override
    public void updateUser(UserDetails user) {
        log.error("사용자 정보 업데이트는 지원되지 않음 (provider_id): {}", user.getUsername());
        throw new UnsupportedOperationException("사용자 업데이트 기능은 아직 지원되지 않습니다.");
    }

    /**
     * // 사용자 삭제 (현재 미구현)
     * // - 실제 삭제 대신 '탈퇴 플래그'로 관리하는 소프트 삭제 전략을 권장
     */
    @Override
    public void deleteUser(String providerId) {
        log.error("사용자 삭제는 지원되지 않음 (provider_id): {}", providerId);
        throw new UnsupportedOperationException("사용자 삭제 기능은 아직 지원되지 않습니다.");
    }

    /**
     * // 비밀번호 변경 (소셜 로그인은 비밀번호를 사용하지 않음)
     * // - 자체 회원 가입/로그인 기능을 추가할 때 구현
     */
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        log.error("비밀번호 변경은 지원되지 않음.");
        throw new UnsupportedOperationException("비밀번호 변경 기능은 아직 지원되지 않습니다.");
    }
}
/*
* [이 코드의 역할]
* - 스프링 시큐리티가 이해하는 방식(UserDetailsManager) 으로 우리 DB의 사용자(User) 를 읽고 만들게 해주는 연결 어댑터
* - loadUserByUsername(providerId)로 DB에서 사용자를 찾음
* - 소셜 최초 로그인 시 DB에 사용자가 없으면, createUser(UserDetails)로 엔티티를 생성/저장
* - userExists(providerId)로 “중복/존재”를 빠르게 판단
*
* [왜 필요할까?]
* - 컨트롤러/필터/메서드 보안이 항상 같은 타입(UserDetails) 으로 사용자 정보를 받음.
* - 토큰만 믿고 끝내지 x ->  필요하면 DB에서 최신 사용자 상태(정지/잠금/권한 변경)를 다시 확인
* - 소셜 로그인 최초 진입(온보딩)을 안전하게 (createUser(UserDetails)로 중복 체크 → 변환 → 저장을 한 번에 처리)
* - 사용자 접근을 이 매니저로 캡슐화 ->  유지, 보수, 테스트가 쉬움
*
* [없으면 어떻게 될까?]
* - 보안 계층이 기대하는 UserDetails 흐름이 없어짐 -> 메서드 보안이 오작동
* - 최초 로그인 시 여기저기서 임의로 저장/중복 확인 -> 중복 계정,식별자 충돌 가능성 존재
* - 유지, 보수, 테스트 비용 증가
 */
