package univ.yesummit.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private Authority authority; //ADMIN, USER, SIGN_OUT

    private String provider;

    private String providerId;

    private String refreshToken;

    /**
     * 추가 정보 필드
     */
    private String company; //회사 정보

    private String position; //회사 내 직책

    private String phoneNumber; //전화번호

    @Enumerated(EnumType.STRING)
    private UserType userType; // 청년 창업가 or 예비 투자자

    private String businessRegistrationNumber;

    private String businessIdeaField; //사업 아이디어 분야

    private boolean consentSummitAlerts; //알림톡 수신 여부

    private boolean consentPrivacyPolicy; //개인정보 수집 및 이용 동의

    /**
     * 회원 정보 수정
     * 서비스 특성상 최초 로그인 시 입력받은 정보 중 일부만 수정 가능하게 하였음
     */

    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }

    public void updateConsentSummitAlerts(boolean newConsentSummitAlerts) {
        this.consentSummitAlerts = newConsentSummitAlerts;
    }

    public void updatePhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
    }

    public void updateBusinessIdeaField(String newBusinessIdeaField) {
        this.businessIdeaField = newBusinessIdeaField;
    }

    //== 회원 가입 시에 USER권한을 부여 ==//
    public void addUserAuthority() {
        this.authority = Authority.USER;
    }

    //== 회원 추가 정보 ==//
    public void updateAdditionalInfo(
            String phoneNumber,
            UserType userType,
            String company,
            String position,
            String businessRegistrationNumber,
            String businessIdeaField,
            Boolean consentSummitAlerts,
            Boolean consentPrivacyPolicy
    ) {
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.company = company;
        this.position = position;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.businessIdeaField = businessIdeaField;
        this.consentSummitAlerts = consentSummitAlerts;
        this.consentPrivacyPolicy = consentPrivacyPolicy;
    }

    //== 토큰 관련 ==//
    public void updateRefreshToken(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }
    public void destroyRefreshToken() {
        this.refreshToken = null;
    }
}
