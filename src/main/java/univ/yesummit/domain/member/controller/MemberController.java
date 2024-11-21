package univ.yesummit.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import univ.yesummit.domain.member.dto.MemberInfoDTO;
import univ.yesummit.domain.member.dto.MemberSignUpDTO;
import univ.yesummit.domain.member.dto.MemberUpdateDTO;
import univ.yesummit.domain.member.dto.MemberWithdrawDTO;
import univ.yesummit.domain.member.service.MemberService;
import univ.yesummit.global.auth.util.JwtUtils;
import univ.yesummit.global.oauth.OAuth2Member;
import univ.yesummit.global.resolver.LoginUser;
import univ.yesummit.global.resolver.User;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtils jwtUtils;

    /**
     * 첫 소셜 로그인 시에 추가적인 정보 수집(회원가입)진행
     */
    @PostMapping("/saveAdditionalInfo")
    @Operation(summary = "추가 정보 수집", description = "첫 로그인 시에 추가적인 정보를 수집합니다.")
    public void saveAdditionalInfo(@User LoginUser loginUser, @Valid @RequestBody MemberSignUpDTO memberSignUpDTO) throws Exception {
        Long memberId = loginUser.getMemberId();
        memberService.saveAdditionalInfo(memberId, memberSignUpDTO);
    }

    /**
     * 회원정보수정
     */
    @PutMapping
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @ResponseStatus(HttpStatus.OK)
    public void updateInfo(@User LoginUser loginUser, @Valid @RequestBody MemberUpdateDTO memberUpdateDTO) throws Exception {
        Long memberId = loginUser.getMemberId();
        memberService.updateInfo(memberId, memberUpdateDTO);
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping
    @Operation(summary = "회원 탈퇴", description = "회원탈퇴를 합니다.")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@User LoginUser loginUser, @Valid @RequestBody MemberWithdrawDTO memberWithdrawDTO) throws Exception {
        Long memberId = loginUser.getMemberId();
        memberService.withdraw(memberId, memberWithdrawDTO);
    }

    /**
     * 내정보조회
     */
    @GetMapping
    @Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MemberInfoDTO> getMyInfo(@User LoginUser loginUser) throws Exception {
        Long memberId = loginUser.getMemberId();
        log.info(memberId.toString());
        MemberInfoDTO myInfo = memberService.getMyInfo(memberId);
        return ResponseEntity.ok(myInfo);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃을 진행합니다.")
    public void logout(@User LoginUser loginUser) throws Exception {
        Long memberId = loginUser.getMemberId();
        memberService.logout(memberId);
    }

    /**
     * 토큰 갱신
     */
    @PostMapping("/refresh")
    @Operation(summary = "Access 토큰 재발급", description = "Refresh 토큰을 사용하여 새로운 Access 토큰을 발급받습니다.")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String refreshToken = jwtUtils.extractRefreshToken(request)
                .orElseThrow(() -> new IllegalArgumentException("Refresh Token이 없습니다."));
        // 서비스 레이어에 토큰 재발급 요청
        String newAccessToken = memberService.refreshAccessToken(refreshToken);
        // 새로운 Access 토큰을 응답 헤더에 추가
        jwtUtils.sendAccessToken(response, newAccessToken);
        return ResponseEntity.ok().build();
    }
}

