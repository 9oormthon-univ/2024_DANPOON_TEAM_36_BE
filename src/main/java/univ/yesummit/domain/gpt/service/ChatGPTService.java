package univ.yesummit.domain.gpt.service;


import univ.yesummit.domain.gpt.dto.ChatCompletionDTO;

import java.util.Map;

public interface ChatGPTService {


    // 유효한 모델 체크
    Map<String, Object> isValidModel(String modelName);


    // 최신 모델 요청
    Map<String, Object> prompt(ChatCompletionDTO chatCompletionDto);

    // 써밋 생성
//    Map<String, Object> createSummit();

}