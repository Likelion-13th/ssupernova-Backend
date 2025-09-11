package likelion13th.shop.global.config;

import likelion13th.shop.login.auth.jwt.AuthCreationFilter;
import likelion13th.shop.login.auth.jwt.JwtValidationFilter;
import likelion13th.shop.login.auth.utils.OAuth2SuccessHandler;
import likelion13th.shop.login.auth.utils.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final AuthCreationFilter authCreationFilter;
    private final JwtValidationFilter jwtValidationFilter;
    private final OAuth2UserServiceImpl oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 인증 및 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/health", // health check

                                "/swagger-ui/**",         // Swagger
                                "/v3/api-docs/**",

                                "/users/reissue",         // 토큰 재발급
                                "/users/logout",          // 로그아웃

                                "/token/**",              // 토큰 재발급 및 생성
                                "/oauth2/**",             // 카카오 OAuth 리디렉션
                                "/login/oauth2/**",        // 카카오 OAuth 콜백

                                "/categories/**",         //  로그인 없이 카테고리 조회 가능
                                "/items/**"               //  로그인 없이 상품 조회 가능
                        ).permitAll()
                        .anyRequest().authenticated() // -> 그 이외의 것: 로그인, 인증 필요(jwt 있어야 접근 가능)
                )
                // 세션 정책: STATELESS (JWT 기반)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // OAuth2 로그인 설정 (UserService 연동)
                // 카카오 로그인 성공 시 -> oAuth2SuccessHandler 호출 -> jwt 발급 및 저장.
                .oauth2Login(oauth2 -> oauth2
                        //.loginPage("/users/login")
                        .successHandler(oAuth2SuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService))
                )

                // 필터 체인 적용 (순서 중요, 순서가 바뀌면 -> 문제 발생)
                .addFilterBefore(authCreationFilter, AnonymousAuthenticationFilter.class)
                .addFilterBefore(jwtValidationFilter, AuthCreationFilter.class);


        return http.build();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                // "아기 사자 백엔드 배포 주소",
                "https://ssupernova-shop.netlify.app/ "
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
