package univ.yesummit.domain.gpt.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import univ.yesummit.domain.gpt.dto.ChatCompletionDTO;
import univ.yesummit.domain.gpt.service.ChatGPTService;
import univ.yesummit.global.exception.dto.ResponseVO;
import univ.yesummit.global.resolver.LoginUser;
import univ.yesummit.global.resolver.User;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/v1/api/gpt")
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;


    /**
     * ChatGPT 유효한 모델인지 조회
     */
    @GetMapping("/model")
    @Operation(summary = "유효한 모델인지 판단", description = "Chat GPT의 유효한 모델인지 조회하고, 판단합니다.")
    public ResponseVO<Map<String, Object>> isValidModel(@RequestParam(name = "modelName") String modelName) {
        Map<String, Object> result = chatGPTService.isValidModel(modelName);
        return new ResponseVO<>(result);
    }


    /**
     * 최신 ChatGPT 프롬프트 명령어를 수행 :
     * gpt-4, gpt-4 turbo, gpt-3.5-turbo
     */
    @PostMapping("/prompt")
    @Operation(summary = "프롬프트 명령어를 수행", description = "GPT API 구현을 위한 프롬프트 명령어를 수행합니다.")
    public ResponseVO<Map<String, Object>> selectPrompt(@RequestBody ChatCompletionDTO chatCompletionDto) {
        log.debug("param :: " + chatCompletionDto.toString());
        Map<String, Object> result = chatGPTService.prompt(chatCompletionDto);
        return new ResponseVO<>(result);
    }

    /**
     * 써밋 생성
     */
//    @PostMapping("/summit")
//    @Operation(summary = "써밋 생성", description = "주기적(1주일에 2번) 써밋을 자동으로 생성해줍니다.")
//    public ResponseVO<Map<String, Object>> createSummit() {
//        log.info("Summit creation request without input payload");
//
//        Map<String, Object> result = chatGPTService.createSummit();
//        return new ResponseVO<>(result);
//    }
}