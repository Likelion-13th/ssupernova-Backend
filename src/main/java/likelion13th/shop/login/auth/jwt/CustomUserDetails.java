package likelion13th.shop.login.auth.jwt;

import likelion13th.shop.domain.Address;
import likelion13th.shop.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    public CustomUserDetails(String providerId, String password, Collection<? extends GrantedAuthority> authorities) {
        this.providerId = providerId;
        this.userId = null;
        this.usernickname = null;
        this.authorities = authorities;
        this.address = null;
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
        if (this.authorities != null && !this.authorities.isEmpty()) {
            return this.authorities;
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
