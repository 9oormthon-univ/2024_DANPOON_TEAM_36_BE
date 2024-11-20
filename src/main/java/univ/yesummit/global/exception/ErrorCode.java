package univ.yesummit.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 사용자 */
    ALREADY_EXIST_MEMBER(5000, "이미 존재하는 사용자입니다."),
    NOT_FOUND_MEMBER(5001, "사용자를 찾을 수 없습니다."),
    MISMATCH_PASSWORD(5002, "비밀번호가 일치하지 않습니다."),
    MISMATCH_EMAIL(5003, "이메일이 일치하지 않습니다."),
    OAUTH2_LOGIN_FAILED(5004, "로그인에 실패했습니다."),
    OAUTH2_REGISTRATION_FAILED(5005, "회원가입에 실패했습니다."),


    /* 공용 */
    NOT_FOUND_ACCESS_TOKEN(00, "토큰을 찾을 수 없습니다."),
    EXPIRED_ACCESS_TOKEN(403, "토큰의 유효시간이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(00, "유효하지 않은 토큰입니다.");

    private final int errorCode;
    private final String message;
}
