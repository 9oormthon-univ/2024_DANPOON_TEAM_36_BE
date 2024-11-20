package univ.yesummit.domain.member.dto;

import univ.yesummit.domain.member.entity.Member;

public record MemberInfoDTO(
        String name,
        String phoneNumber,
        String company,
        String position,
        String userType,
        String businessRegistrationNumber,
        String businessIdeaField,
        boolean consentSummitAlerts,
        boolean consentPrivacyPolicy
) {
    public MemberInfoDTO(Member member) {
        this(
                member.getUsername(),
                member.getPhoneNumber(),
                member.getCompany(),
                member.getPosition(),
                member.getUserType().toString(),
                member.getBusinessRegistrationNumber(),
                member.getBusinessIdeaField(),
                member.isConsentSummitAlerts(),
                member.isConsentPrivacyPolicy()
        );
    }
}
