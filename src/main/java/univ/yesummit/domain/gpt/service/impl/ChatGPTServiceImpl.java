package univ.yesummit.domain.gpt.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import univ.yesummit.domain.gpt.config.ChatGPTConfig;
import univ.yesummit.domain.gpt.dto.ChatCompletionDTO;
import univ.yesummit.domain.summit.repository.SummitRepository;
import univ.yesummit.domain.gpt.service.ChatGPTService;
import univ.yesummit.domain.member.repository.MemberRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * ChatGPT Service 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTServiceImpl implements ChatGPTService {

    private final ChatGPTConfig chatGPTConfig;
    private final ObjectMapper objectMapper;
    private final SummitRepository summitRepository;
    private final MemberRepository memberRepository;

    @Value("${openai.url.model}")
    private String modelUrl;

    @Value("${openai.url.model-list}")
    private String modelListUrl;

    @Value("${openai.url.prompt}")
    private String promptUrl;

    @Value("${openai.url.legacy-prompt}")
    private String legacyPromptUrl;


    /**
     * 모델이 유효한지 확인하는 비즈니스 로직
     */
    @Override
    public Map<String, Object> isValidModel(String modelName) {
        log.debug("[+] 모델이 유효한지 조회합니다. 모델 : " + modelName);
        Map<String, Object> result = new HashMap<>();

        // 토큰 정보가 포함된 Header를 가져온다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // 통신을 위한 RestTemplate을 구성
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(modelListUrl + "/" + modelName, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        try {
            // Jackson을 기반으로 응답값을 가져온다.
            result = objectMapper.readValue(response.getBody(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }
        return result;
    }


    /**
     * 최신 모델에 대한 프롬프트
     */
    @Override
    public Map<String, Object> prompt(ChatCompletionDTO chatCompletionDto) {
        log.debug("[+] 신규 프롬프트를 수행합니다.");

        Map<String, Object> resultMap = new HashMap<>();

        // 토큰 정보가 포함된 Header를 가져온다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // 통신을 위한 RestTemplate을 구성
        HttpEntity<ChatCompletionDTO> requestEntity = new HttpEntity<>(chatCompletionDto, headers);
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);
        try {
            // String -> HashMap 역직렬화를 구성
            resultMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }
        return resultMap;
    }

//    @Override
//    public Map<String, Object> createSummit() {
//        String prompt = "지속 가능한 미래를 이끄는 건, 결국 청년과 그들의 새로운 시각입니다. " +
//                "이 점에서 청년들이 그들의 반짝이는 아이디어를 실현하고 세상에 선보일 수 있도록 돕는 것은 " +
//                "청년의 창업 문제 해결을 통해 지속 가능한 미래를 만들 수 있습니다. 다만 예비 청년 창업가들에게는 " +
//                "자신의 창업 아이템을 전문적으로 소개할 창구가 부족합니다. 그래서 Young Entrepreneur들이 " +
//                "주인공인 Summit 플랫폼을 만들고자 합니다. 이를 바탕으로 써밋 주제를 5줄 정도로 만들어주세요. " +
//                "제목과 본문으로 구성해 주세요.";
//
//        ChatCompletionDTO chatCompletionDto = ChatCompletionDTO.builder()
//                .model("gpt-3.5-turbo")
//                .messages(List.of(
//                        ChatRequestMsgDTO.builder()
//                                .role("user")
//                                .content(prompt)
//                                .build()
//                ))
//                .build();
//
//        // ChatGPT API 호출
//        Map<String, Object> gptResponse = prompt(chatCompletionDto);
//
//        // 응답 데이터 검증
//        if (gptResponse == null || !gptResponse.containsKey("choices")) {
//            throw new RuntimeException("ChatGPT 응답이 유효하지 않습니다.");
//        }
//
//        List<Map<String, Object>> choices = (List<Map<String, Object>>) gptResponse.get("choices");
//        if (choices == null || choices.isEmpty()) {
//            throw new RuntimeException("ChatGPT 응답에서 choices가 비어 있습니다.");
//        }
//
//        Map<String, Object> firstChoice = choices.get(0);
//        if (firstChoice == null || !firstChoice.containsKey("text")) {
//            throw new RuntimeException("ChatGPT 응답의 텍스트가 없습니다.");
//        }
//
//        String responseText = (String) firstChoice.get("text");
//        if (responseText == null || responseText.isEmpty()) {
//            throw new RuntimeException("ChatGPT 응답의 텍스트가 비어 있습니다.");
//        }
//
//        // 텍스트 파싱
//        String[] splitText = responseText.split("\n", 2);
//        if (splitText.length < 2) {
//            throw new RuntimeException("ChatGPT 응답 형식이 올바르지 않습니다.");
//        }
//
//        String title = splitText[0].replace("제목:", "").trim();
//        String content = splitText[1].replace("본문:", "").trim();
//
//        // Summit 엔티티 생성 및 저장
//        Summit newSummit = new Summit(title, content);
//        Summit savedSummit = summitRepository.save(newSummit);
//
//        return Map.of(
//                "message", "Summit has been created successfully",
//                "summitId", savedSummit.getId(),
//                "title", savedSummit.getTitle(),
//                "content", savedSummit.getContent()
//        );
//    }
}