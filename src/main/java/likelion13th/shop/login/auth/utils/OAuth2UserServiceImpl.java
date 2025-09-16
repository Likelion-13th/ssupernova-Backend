package likelion13th.shop.login.auth.utils;

import likelion13th.shop.domain.User;
import likelion13th.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("// 카카오 OAuth2 로그인 시도");

        String providerId = oAuth2User.getAttributes().get("id").toString();

        @SuppressWarnings("unchecked")
        Map<String, Object> properties =
                (Map<String, Object>) oAuth2User.getAttributes().getOrDefault("properties", Collections.emptyMap());
        String nickname = properties.getOrDefault("nickname", "카카오사용자").toString();

        Map<String, Object> extendedAttributes = new HashMap<>(oAuth2User.getAttributes());
        extendedAttributes.put("provider_id", providerId);
        extendedAttributes.put("nickname", nickname);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                extendedAttributes,
                "provider_id"
        );
    }
}
/*
* [이 코드의 역할]
* - 카카오에서 가져온 원본 사용자 정보를 우리 서비스에서 쓰기 좋게 정규화(키 이름 통일)
* - 최소 권한(ROLE_USER) 과 식별 키를 붙여 DefaultOAuth2User(principal) 로 넘겨줌 (가공 담당자??)
*
* [왜 필요한가?]
* - 이 클래스에서 provider_id, nickname 같은 우리 표준 키로 변환
* -> 이후 핸들러/서비스/리포지토리는 항상 같은 키만 보면 됨
* - JWT sub, DB 조회 키와 일관성을 맞추기 쉬워짐
* - 로그인 직후 사용자는 기본적으로 ROLE_USER만 갖게 -> 안전한 기본선 제공
* - 공급자별 매핑은 OAuth2UserService 단계에서 한 번만 처리 → 유지보수·확장(다른 SNS 추가)이 쉬워짐
*
* [없으면?]
* - 카카오 원본 응답을 provider_id·nickname으로 표준화해주지 않음
* -> 바로 다음 단계(OAuth2SuccessHandler)부터 필수 값이 전부 null
* -> 회원 생성 · JWT 발급 · 리다이렉트까지 전부 오류
* - 현재 구현은 최소 권한 ROLE_USER 를 명시 부여 -> 이 부분이 빠지면 권한이 비거나 SCOPE_xxx 등으로 들어옴 -> 403 유발
* - 카카오 외에 구글/네이버 추가 시 매번 하위 계층을 뜯어고쳐야 함
*
 */
