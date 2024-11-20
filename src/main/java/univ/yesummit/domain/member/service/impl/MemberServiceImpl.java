package univ.yesummit.domain.member.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import univ.yesummit.domain.member.dto.MemberInfoDTO;
import univ.yesummit.domain.member.dto.MemberSignUpDTO;
import univ.yesummit.domain.member.dto.MemberUpdateDTO;
import univ.yesummit.domain.member.dto.MemberWithdrawDTO;
import univ.yesummit.domain.member.entity.Member;
import univ.yesummit.domain.member.exception.MemberException;
import univ.yesummit.domain.member.repository.MemberRepository;
import univ.yesummit.domain.member.service.MemberService;
import univ.yesummit.global.auth.util.SecurityUtil;
import univ.yesummit.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public void saveAdditionalInfo(MemberSignUpDTO memberSignUpDTO, Long memberId) throws Exception {

        if (memberId == null) {
            throw new IllegalArgumentException("회원 ID가 null입니다. 인증 정보를 확인하세요.");
        }
        // 회원이 존재하지 않으면 예외를 발생
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        // 추가 정보 업데이트
        member.updateAdditionalInfo(
                memberSignUpDTO.phoneNumber(),
                memberSignUpDTO.userType(),
                memberSignUpDTO.company(),
                memberSignUpDTO.position(),
                memberSignUpDTO.businessRegistrationNumber(),
                memberSignUpDTO.businessIdeaField(),
                memberSignUpDTO.consentSummitAlerts(),
                memberSignUpDTO.consentPrivacyPolicy()
        );

        member.addUserAuthority(); // 권한 추가
        memberRepository.save(member); // 변경된 정보 저장
    }

    // 첫 로그인 여부 확인
    @Override
    public boolean isFirstLogin(Long memberId) {
        return memberRepository.findById(memberId)
                .map(member -> member.getPhoneNumber() == null || member.getUserType() == null)
                .orElse(false);
    }

    @Override
    public void withdraw(Long memberId, MemberWithdrawDTO memberWithdrawDTO) throws Exception {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("입력된 정보와 일치하는 사용자가 없습니다."));

        if (!member.getEmail().equals(memberWithdrawDTO.email())) {
            throw new MemberException(ErrorCode.MISMATCH_EMAIL);
        }

        memberRepository.delete(member); // 데이터베이스에서 완전히 삭제
    }

    @Override
    public void updateInfo(MemberUpdateDTO memberUpdateDTO) throws Exception {

        Member member = memberRepository.getByUsernameOrThrow(SecurityUtil.getLoginUsername());

        memberUpdateDTO.phoneNumber().ifPresent(member::updatePhoneNumber);
        memberUpdateDTO.businessIdeaField().ifPresent(member::updateBusinessIdeaField);
        memberUpdateDTO.consentSummitAlerts().ifPresent(member::updateConsentSummitAlerts);
    }

    @Override
    public MemberInfoDTO getMyInfo(Long memberId) throws Exception {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
        return new MemberInfoDTO(member);
    }
}
