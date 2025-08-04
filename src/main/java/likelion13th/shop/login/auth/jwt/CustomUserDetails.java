package likelion13th.shop.login.auth.jwt;

import likelion13th.shop.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security에서 사용되는 사용자 정보 객체.
 * 로그인한 사용자 정보를 보관하며, @AuthenticationPrincipal로 주입 가능
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;           // 🔹 User 객체 전체 보관
    private final Long id;             // User 엔티티의 id
    private final String providerId;   // 카카오 고유 ID

    public CustomUserDetails(User user) {
        this.user = user;
        this.id = user.getId();
        this.providerId = user.getProviderId();
    }

    /** 🔹 User 객체 반환 */
    public User getUser() {
        return this.user;
    }

    /** 인증 시 사용되는 username → 우리는 providerId 사용 **/
    @Override
    public String getUsername() {
        return this.providerId;
    }

    /** 패스워드는 사용하지 않음 (OAuth 기반) **/
    @Override
    public String getPassword() {
        return null;
    }

    /** 기본 권한 (필요 시 Role 로직 확장 가능) **/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // ex) ROLE_USER 등
    }

    /** 계정 만료 여부 (true = 만료되지 않음) **/
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** 계정 잠김 여부 **/
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /** 자격 증명 만료 여부 **/
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** 계정 활성화 여부 **/
    @Override
    public boolean isEnabled() {
        return true;
    }
}
