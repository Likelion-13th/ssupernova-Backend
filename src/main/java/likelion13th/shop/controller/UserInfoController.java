// 사용자 정보 조회 API를 제공하며, Swagger 문서화 및 통일된 응답 포맷 적용

package likelion13th.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13th.shop.DTO.response.AddressResponse;
import likelion13th.shop.DTO.response.UserInfoResponse;
import likelion13th.shop.DTO.response.UserMileageResponse;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.service.UserAddressService;
import likelion13th.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자", description = "사용자 정보 관련 API 입니다.")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserService userService;
    private final UserAddressService userAddressService;

    /**
     * 로그인한 사용자의 기본 프로필 정보를 조회
     * - 닉네임, 주소, 최근 결제 금액 등 포함
     */
    @GetMapping("/profile")
    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 기본 프로필 정보를 조회합니다.")
    public ApiResponse<?> getUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // → SecurityContext에서 현재 로그인된 사용자 정보 주입받음
        UserInfoResponse response = userService.getUserInfo(userDetails.getUser());
        return ApiResponse.onSuccess(SuccessCode.USER_INFO_GET_SUCCESS, response);
    }

    // 내 마일리지 조회
    @GetMapping("/mileage")
    @Operation(summary = "내 마일리지 조회", description = "로그인한 사용자의 마일리지를 조회합니다.")
    public ApiResponse<?> getMileage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // → 현재 사용자 기준으로 마일리지 정보만 추출
        UserMileageResponse response = userService.getUserMileage(userDetails.getUser());
        return ApiResponse.onSuccess(SuccessCode.USER_MILEAGE_GET_SUCCESS, response);
    }

    // 내 주소 정보 조회
    @GetMapping("/address")
    @Operation(summary = "내 주소 조회", description = "로그인한 사용자의 주소 정보를 조회합니다.")
    public ApiResponse<?> getAddress(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // → 내장된 Address 객체를 응답용 DTO로 가공해 반환
        AddressResponse response = userAddressService.getUserAddress(userDetails.getUser());
        return ApiResponse.onSuccess(SuccessCode.ADDRESS_GET_SUCCESS, response);
    }
    // → @AuthenticationPrincipal로 로그인 사용자 정보 주입
    // → Swagger 문서화 및 ApiResponse 응답 통일 구조 적용
}
// 로그인한 사용자의 프로필, 마일리지, 주소 정보를 조회하는 컨트롤러
// Spring Security와 Swagger를 적용해 일관된 구조로 설계

