package univ.yesummit.global.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/kakao")
public class AuthController {

    @GetMapping("/login")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 진행합니다.")
    public RedirectView login() {
        return new RedirectView("/oauth2/authorization/kakao");
    }

    @GetMapping("/status")
    @Operation(summary = "로그인 상태 확인", description = "사용자의 로그인 상태를 확인합니다.")
    public ResponseEntity<Map<String, Boolean>> getLoginStatus(HttpServletRequest request) {
        // 쿠키 또는 세션에서 로그인 상태를 확인
        HttpSession session = request.getSession(false); // 세션이 없으면 null 반환
        boolean loggedIn = session != null && session.getAttribute("user") != null;

        // 응답 데이터 생성
        Map<String, Boolean> response = new HashMap<>();
        response.put("loggedIn", loggedIn);

        return ResponseEntity.ok(response);
    }
}