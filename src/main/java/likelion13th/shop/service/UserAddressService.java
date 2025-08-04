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

    // 주소 응답 생성
    public AddressResponse getUserAddress(User user) {
        var address = user.getAddress();
        return AddressResponse.from(user.getAddress());
    }

    // 내 주소 수정
    @Transactional
    public void updateUserAddress(AddressRequest request, CustomUserDetails userDetails) {
        User user = findUser(userDetails);
        Address newAddress = new Address(
                request.getZipcode(),
                request.getAddress(),
                request.getAddressDetail()
        );
        user.updateAddress(newAddress);
    }

    private User findUser(CustomUserDetails userDetails) {
        return userRepository.findByProviderId(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}
