package likelion13th.shop.service;

import likelion13th.shop.DTO.request.AddressRequest;
import likelion13th.shop.DTO.response.AddressResponse;
import likelion13th.shop.domain.Address;
import likelion13th.shop.domain.User;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

/**
 * 사용자 주소 정보를 조회 및 수정하는 서비스 클래스
 * - 사용자 인증 정보(CustomUserDetails)를 바탕으로 주소를 가져오거나 변경
 */
public class UserAddressService {

    private final UserRepository userRepository;

    /** 내 주소 조회 **/
    public AddressResponse getUserAddress(User user) {
        // → 유저 객체 내의 Address 필드값을 AddressResponse로 변환
        var address = user.getAddress();
        return AddressResponse.from(user.getAddress());
    }

    /** 내 주소 수정 **/
    @Transactional
    public void updateUserAddress(AddressRequest request, CustomUserDetails userDetails) {
        // → 인증된 사용자 조회 후 새 Address 객체로 덮어씀
        User user = findUser(userDetails);
        Address newAddress = new Address(
                request.getZipcode(),
                request.getAddress(),
                request.getAddressDetail()
        );
        user.updateAddress(newAddress);  // 엔티티 내부 updateAddress 메서드 사용
    }

    private User findUser(CustomUserDetails userDetails) {
        return userRepository.findByProviderId(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    // → 인증 기반 주소 조회/수정 기능만 분리하여 책임 분담

}
// 사용자 인증 정보를 바탕으로 주소를 조회하거나 수정하는 서비스
// @Transactional -> 변경 감지를 활용해 JPA에서 주소 값 자동 반영
