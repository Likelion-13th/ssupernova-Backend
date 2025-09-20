package likelion13th.shop.login.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
public class UserRequestDto {
    @Schema(description="UserReqDto")
    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserReqDto{
        private Long userId;
        private String providerId;
        private String usernickname;
    }
}
