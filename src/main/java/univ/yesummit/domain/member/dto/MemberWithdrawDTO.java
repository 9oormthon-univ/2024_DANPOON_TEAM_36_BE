package univ.yesummit.domain.member.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberWithdrawDTO(
        @NotBlank(message = "이름을 입력해주세요")
        String username,

        @NotBlank(message = "이메일을 입력해주세요")
        String email
) {
}
