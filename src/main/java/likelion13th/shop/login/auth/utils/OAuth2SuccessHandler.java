package likelion13th.shop.login.auth.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion13th.shop.domain.Address;
import likelion13th.shop.domain.User;
import likelion13th.shop.login.auth.dto.JwtDto;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.login.auth.service.JpaUserDetailsManager;
import likelion13th.shop.login.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JpaUserDetailsManager jpaUserDetailsManager;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        String providerId = (String) oAuth2User.getAttribute("provider_id");
        String nickname   = (String) oAuth2User.getAttribute("nickname");

        String maskedPid  = (providerId != null && providerId.length() > 4) ? providerId.substring(0, 4) + "***" : "***";
        String maskedNick = (nickname != null && !nickname.isBlank()) ? "*(hidden)*" : "(none)";
        log.info("OAuth2 Success - providerId(masked)={}, nickname={}", maskedPid, maskedNick);

        if (!jpaUserDetailsManager.userExists(providerId)) {
            User newUser = User.builder()
                    .providerId(providerId)
                    .usernickname(nickname)
                    .deletable(true)
                    .build();

            newUser.setAddress(new Address("10540", "경기도 고양시 덕양구 항공대학로 76", "한국항공대학교"));

            CustomUserDetails userDetails = new CustomUserDetails(newUser);
            jpaUserDetailsManager.createUser(userDetails);
            log.info("신규 회원 등록 완료 - providerId(masked)={}", maskedPid);
        } else {
            log.info("기존 회원 로그인 - providerId(masked)={}", maskedPid);
        }

        JwtDto jwt = userService.jwtMakeSave(providerId);
        log.info("JWT 발급 완료 - providerId(masked)={}", maskedPid);

        String frontendRedirectUri = request.getParameter("redirect_uri");
        List<String> authorizedUris = List.of(
                "https://ssupernova-shop.netlify.app/",
                "http://localhost:3000"
        );
        if (frontendRedirectUri == null || !authorizedUris.contains(frontendRedirectUri)) {
            frontendRedirectUri = "https://ssupernova-shop.netlify.app/";
        }

        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendRedirectUri)
                .queryParam("accessToken", jwt.getAccessToken())
                .build()
                .toUriString();

        log.info("Redirecting to authorized frontend host: {}", frontendRedirectUri);

        response.sendRedirect(redirectUrl);
    }
}
/*
* [이 코드의 역할]
* - 카카오 OAuth2 로그인에 성공한 직후 실행(로그인 성공 마무리 담당자??)
*   1. 카카오에서 받은 사용자 식별자/닉네임을 꺼내
*   2. 우리 DB에 사용자가 없으면 신규 회원을 생성한 뒤
*   3. JWT를 발급·저장하고
*   4. 허용된 프론트 주소로 액세스 토큰을 실어 리다이렉트
*
* [왜 필요한가?]
* - 카카오에서 “성공” 응답이 와도, 그건 카카오 기준의 인증
* -> 이 사용자를 내부 유저로 어떻게 인식할지, 어떤 권한을 줄지, 어떤 토큰을 줄지를 마무리해야 함
* -> 이 핸들러가 그 마무리를 처리
* -  회원 온보딩 자동 처리: provider_id로 내부 DB에 유저가 없으면 생성, 있으면 기존 유저로 로그인
* - 핸들러가 userService를 호출 -> 토큰 발급 및 DB 저장(Refresh) 트리거 -> 이후 Access 토큰으로 바로 API 호출 가능
* - 성공 후 어디로 보낼지(redirect_uri)를 허용된 목록에서만 선택 -> 잘못된 목적지로 토큰이 새는 것 방지
*
* [없으면 어떻게 될까?]
* - 콜백만 받고 끝남 ->  DB에 유저 존재 X -> AI 요청에서 인증 불가(401)
* - 토큰 없음 -> 다시 로그인 요구, 무한 루프 ... (심각한 UX 저하)
* - 리다이렉트 목적지 검증 안 함 -> 공격자가 임의의 주소로 보냄 -> 토큰 탈취 노릴 수 있음
* - 컨트롤러/필터마다 제각각 생성 시도 -> 중복 계정/무결성 오류 발생!
*
*/
