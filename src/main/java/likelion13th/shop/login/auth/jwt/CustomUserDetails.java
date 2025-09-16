package likelion13th.shop.login.auth.jwt;

import likelion13th.shop.domain.Address;
import likelion13th.shop.domain.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {
    private Long userId;
    private String providerId;
    private String usernickname;
    private Address address;

    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.userId = user.getId();
        this.providerId = user.getProviderId();
        this.usernickname = user.getUsernickname();
        this.address = user.getAddress();
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public CustomUserDetails(String providerId, String usernickname,
                             Collection<? extends GrantedAuthority> authorities) {
        this.userId = null;
        this.providerId = providerId;
        this.usernickname = null;
        this.address = null;
        this.authorities = authorities;
    }

    public static CustomUserDetails fromEntity(User entity) {
        return CustomUserDetails.builder()
                .userId(entity.getId())
                .providerId(entity.getProviderId())
                .usernickname(entity.getUsernickname())
                .address(entity.getAddress())
                .build();
    }

    public User toEntity() {
        return User.builder()
                .id(this.userId)
                .providerId(this.providerId)
                .usernickname(this.usernickname)
                .address(this.address)
                .build();
    }

    @Override
    public String getUsername() {
        return this.providerId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.authorities != null && this.authorities.isEmpty()) {
            return this.authorities;
        }
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        // 소셜 로그인은 비밀번호를 사용하지 않음
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 정책 사용 시 실제 값으로 교체
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 잠금 정책 사용 시 실제 값으로 교체
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 자격 증명(비밀번호) 만료 정책 사용 시 실제 값으로 교체
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 활성/비활성 정책 사용 시 실제 값으로 교체 (예: 탈퇴/정지 사용자)
        return true;
    }
}

/*
* [CustomUserDetails가 하는 일]
* - Spring Security(인증, 인가, 공격 방어 담당) 표준 사용자 모델 (로그인한 사용자를 표현, 조회, 검증할 때 기대하는 표준 형태)
* -> User Entity에 맞게 연결(adapting)하는 클래스
*
* [왜 필요한가?]
* - Spring Security는 내부 규약상 UserDetails 형태로 사용자 정보를 다룸.
* - but, 현재 User Entity(id, providerId, usernickname, address 등)를 사용 중...
* - 두 체계를 이어서, 현재 사용자 정보와 권한을 표준 방식으로 사용하기 위해 CustomUserDetails 필요
*
* [없으면/틀리면?]
* - 컨트롤러에서 현재 사용자 받기 어려움 → 매번 토큰/속성 Map을 직접 파싱해야 함.
*/