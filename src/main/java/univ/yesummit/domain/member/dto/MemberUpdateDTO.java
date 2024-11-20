package univ.yesummit.domain.member.dto;

import java.util.Optional;

public record MemberUpdateDTO(
        Optional<String> phoneNumber,
        Optional<String> businessIdeaField,
        Optional<Boolean> consentSummitAlerts
) {}
