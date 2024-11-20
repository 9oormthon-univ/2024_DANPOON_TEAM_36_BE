package univ.yesummit.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import univ.yesummit.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public class MemberException extends RuntimeException {

    private final ErrorCode errorCode;
}
