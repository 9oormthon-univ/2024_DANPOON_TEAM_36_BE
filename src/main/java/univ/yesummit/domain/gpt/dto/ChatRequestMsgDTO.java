package univ.yesummit.domain.gpt.dto;

import lombok.*;

/**
 * ChatGPT 신규 모델의 Request
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRequestMsgDTO {

    private String role;

    private String content;

    @Builder
    public ChatRequestMsgDTO(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
