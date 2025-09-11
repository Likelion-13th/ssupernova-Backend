package likelion13th.shop.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// 비밀번호를 암호화/검증하는 역할
@Configuration
// 빈 주입 상태로 코드
public class PasswordEncoderConfig {
    @Bean
    // 비밀번호 암호화를 위한 Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
