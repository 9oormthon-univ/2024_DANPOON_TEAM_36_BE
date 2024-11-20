package univ.yesummit.domain.member.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import univ.yesummit.domain.member.entity.Member;
import univ.yesummit.domain.member.entity.UserType;

public record MemberSignUpDTO(

        @NotBlank(message = "이름을 입력해주세요")
        @Size(min = 2, message = "사용자 이름이 너무 짧습니다.")
        @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "사용자 이름은 한글 또는 알파벳만 입력해주세요.")
        String name,

        @NotBlank(message = "연락처를 입력해주세요")
        String phoneNumber,

        @NotBlank(message = "이메일을 입력해주세요")
        String email,

        @NotNull(message = "참여자 구분을 선택해주세요")
        UserType userType, // 청년 창업가 or 예비 투자자

        @NotBlank(message = "소속 회사를 입력해주세요")
        String company,

        @NotBlank(message = "사내 직책을 입력해주세요")
        String position,

        String businessRegistrationNumber, // 사업자 등록 번호 (선택적)

        String businessIdeaField, // 사업 아이디어 및 분야 (선택적)

        @NotNull(message = "써밋 알림톡 수신 여부를 선택해주세요")
        Boolean consentSummitAlerts, // 써밋 알림톡 수신 여부

        @NotNull(message = "개인정보 수집 및 이용 동의를 선택해주세요")
        Boolean consentPrivacyPolicy // 개인정보 수집 및 이용 동의
)
{
    public Member toEntity() {
        return Member.builder()
                .username(name)
                .phoneNumber(phoneNumber)
                .email(email)
                .userType(userType)
                .company(company)
                .position(position)
                .businessRegistrationNumber(businessRegistrationNumber)
                .businessIdeaField(businessIdeaField)
                .consentSummitAlerts(consentSummitAlerts)
                .consentPrivacyPolicy(consentPrivacyPolicy)
                .build();
    }
}