package univ.yesummit.domain.member.service;


import univ.yesummit.domain.member.dto.MemberInfoDTO;
import univ.yesummit.domain.member.dto.MemberSignUpDTO;
import univ.yesummit.domain.member.dto.MemberUpdateDTO;
import univ.yesummit.domain.member.dto.MemberWithdrawDTO;

public interface MemberService {

    /**
     * 추가 정보 수집
     */
    void saveAdditionalInfo(Long memberId, MemberSignUpDTO dto) throws Exception;

    /**
     * 첫 로그인 확인
     */
    boolean isFirstLogin(Long memberId);

    /**
     * 회원 탈퇴
     */
    void withdraw(Long memberId, MemberWithdrawDTO dto) throws Exception;

    /**
     * 회원 정보 수정
     */
    void updateInfo(Long memberId, MemberUpdateDTO dto) throws Exception;

    /**
     * 내 정보 조회
     */
    MemberInfoDTO getMyInfo(Long memberId) throws Exception;

}
