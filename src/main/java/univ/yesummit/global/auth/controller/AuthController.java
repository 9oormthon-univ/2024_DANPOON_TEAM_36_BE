package univ.yesummit.global.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import univ.yesummit.global.auth.service.AuthService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/kakao")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 진행합니다.")
    public RedirectView login() {
        return new RedirectView("/oauth2/authorization/kakao");
    }

    @GetMapping("/token")
    @Operation(summary = "로그인 후 토큰 요청", description = "로그인 후 accessToken과 refreshToken을 발급합니다.")
    public ResponseEntity<Map<String, Object>> getToken(@RequestParam Long userId) throws Exception {

        Map<String, Object> tokens = authService.generateTokens(userId);

        return ResponseEntity.ok(tokens);
    }
}
