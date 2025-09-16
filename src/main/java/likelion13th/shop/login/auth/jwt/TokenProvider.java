package likelion13th.shop.login.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.dto.JwtDto;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@ComponentScan
public class TokenProvider {
    private final Key secretKey;
    private final Long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public TokenProvider(
            @Value("${JWT_SECRET}") String secretKey,
            @Value("${JWT_EXPIRATION}") long accessTokenExpiration,
            @Value("${JWT_REFRESH_EXPIRATION}") long refreshTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public JwtDto generateTokens(UserDetails userDetails) {
        log.info("JWT 생성 시작: 사용자 {}", userDetails.getUsername());

        String userId = userDetails.getUsername();

        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = createToken(userId, authorities, accessTokenExpiration);

        String refreshToken = createToken(userId, null, refreshTokenExpiration);

        log.info("JWT 생성 완료: 사용자 {}", userDetails.getUsername());
        return new JwtDto(accessToken, refreshToken);
    }

    private String createToken(String providerId, String authorities, long expirationtime) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(providerId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationtime))
                .signWith(secretKey, SignatureAlgorithm.HS384);

        if (authorities != null) {
            builder.claim("authorities", authorities);
        }

        return builder.compact().toString();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.warn("토큰 만료");
            throw e;
        } catch (JwtException e) {
            log.warn("JWT 파싱 실패");
            throw new GeneralException(ErrorCode.TOKEN_INVALID);
        }
    }

    public Collection<? extends GrantedAuthority> getAuthfromClaims(Claims claims) {
        String authoritiesString = claims.get("authorities", String.class);
        if (authoritiesString == null || authoritiesString.isEmpty()) {
            log.warn("권한 정보가 없다 - 기본 ROLE_USER 부여");
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return Arrays.stream(authoritiesString.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Claims parseClaimsAllowExpired(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}

/*
* [이 코드의 역할]
* - AccessToken, RefreshToken을 발급, 만료 검증, 권한 복구 표준화
* - 환경 변수 값(JWT_SECRET, JWT_EXPIRATION, JWT_REFRESH_EXPIRATION)을 읽어 -> 서명 키와 만료 정책 관리
* - parseClaimsAllowExpired: 만료된 토큰도 클레임만 읽어 재발급 흐름 등에서 사용 가능
*
* [왜 필요한가?]
* - 토큰 서명 검증 -> 누가 요청했는지, 어떤 권한이지 확인
* - token 발급, 검증, 파싱을 한 모듈로 통일 -> 여러 곳에 흩어지면서 발생하는 실수(알고리즘/만료/클레임 표 불일치) 방지
* - 파싱·검증에서 오류를 명확한 예외로 변환하고 로깅 -> 운영 중 원인 파악과 대응 체계화 가능
* - 만료된 토큰이라도 재발급 시 사용자 특정 가능
*
* [없으면?]
* - 각 계층이 제각각 토큰을 발급, 검증, 파싱(해석) -> 알고리즘/만료/클레임 불일치 -> 인증 불가
* - 만료된 토큰에서 사용자 정보를 읽어 재발급하는 경로 존재 x -> 재발급 API 불안정해짐
*
* [핵심 로직]
*   <발급>
*       1. UserDetails에서 username(=sub)과 권한들을 꺼냄
*       2. 액세스 sub+authorities+만료 -> 서명
*       3. Refresh: sub + 만료
*       4. 두 토큰을 DTO로 변화
*   <단일 토큰 생성>
*       sub 지정 → iat/exp 지정 → authorities(있으면) 클레임 추가 → HS384로 서명 → 문자열 반환
*   <검증>
*       서명/형식/만료 검사를 파서로 수행 → 예외 없으면 유효(true)
*   <클레임 파싱>
*       유효 토큰이면 Claims 반환 / 만료면 ExpiredJwtException / 그 외는 커스텀 예외
*   <권한 복구>
*       authorities CSV를 SimpleGrantedAuthority 리스트로 변환
*   <만료 허용 파싱>
*       유효 토큰이면 Claims / 만료 토큰이면 예외에서 Claims만 추출해 반환
*
*/