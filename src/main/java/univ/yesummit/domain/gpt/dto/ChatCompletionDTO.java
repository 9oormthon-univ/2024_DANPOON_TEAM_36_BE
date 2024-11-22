package univ.yesummit.domain.gpt.dto;

import lombok.*;

import java.util.List;

/**
 * 새로운 모델에 대한 요청 객체를 관리한다.
 * gpt-4, gpt-4 turbo, gpt-3.5-turbo
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatCompletionDTO {

    // 사용할 모델
    private String model;

    private List<ChatRequestMsgDTO> messages;

    @Builder
    public ChatCompletionDTO(String model, List<ChatRequestMsgDTO> messages) {
        this.model = model;
        this.messages = messages;
    }
}