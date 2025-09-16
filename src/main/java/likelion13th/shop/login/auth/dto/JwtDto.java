package likelion13th.shop.login.auth.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class JwtDto {
    private String accessToken;
    private String refreshToken;

    public JwtDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}


/*
 * JwtDto: 로그인/재발급 API가 프론트로 돌려주는 "토큰 세트" 응답 모델.
 *
 * [언제, 어디서 사용하는가]
 * 1. 사용자가 카카오로 로그인 성공
 * 2. 서버가 내부 유저 식별자 확인 -> JWT 두 개를 생성(accessToken & refreshToken)
 * 3. 이 두 토큰을 한 번에 프론트로 보냄
 * 4. 이때 정해진 모양으로 보내기 위해 JwtDto에 data를 담음
 *
 * [왜 필요한가]
 * - FE/BE 응답 계약(contract) 고정: 항상 같은 키(accessToken, refreshToken)로 내려보내기 위함.
 * - 액세스/리프레시 두 토큰을 "한 세트"로 묶어 전달 → 순서/오타 실수 방지.
 *
 * [없으면/틀리면]
 * - 컨트롤러마다 응답 모양이 달라짐 → 프론트 분기·버그 증가.
 * - 필드명 불일치(access_token vs accessToken)로 파싱 오류 가능.
 * - 만료 정보 등 필드 추가 시 오류 가능
 *
 * [핵심 설계 포인트]
 * - "응답 전용 DTO"로 엔티티와 분리.
 *
 */