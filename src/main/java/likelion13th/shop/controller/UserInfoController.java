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
        UserInfoResponse response = userService.getUserInfo(userDetails.getUser());
        return ApiResponse.onSuccess(SuccessCode.USER_INFO_GET_SUCCESS, response);
    }

    // 내 마일리지 조회
    @GetMapping("/mileage")
    @Operation(summary = "내 마일리지 조회", description = "로그인한 사용자의 마일리지를 조회합니다.")
    public ApiResponse<?> getMileage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserMileageResponse response = userService.getUserMileage(userDetails.getUser());
        return ApiResponse.onSuccess(SuccessCode.USER_MILEAGE_GET_SUCCESS, response);
    }

    // 내 주소 정보 조회
    @GetMapping("/address")
    @Operation(summary = "내 주소 조회", description = "로그인한 사용자의 주소 정보를 조회합니다.")
    public ApiResponse<?> getAddress(@AuthenticationPrincipal CustomUserDetails userDetails) {
        AddressResponse response = userAddressService.getUserAddress(userDetails.getUser());
        return ApiResponse.onSuccess(SuccessCode.ADDRESS_GET_SUCCESS, response);
    }
}
