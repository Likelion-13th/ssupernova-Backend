package likelion13th.shop.login.auth.jwt;

import jakarta.persistence.*;
import likelion13th.shop.domain.User;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String token; // 실제 저장되는 Refresh 토큰 문자열

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // 토큰 갱신 메서드
    public void updateToken(String newToken) {
        this.token = newToken;
    }
}
