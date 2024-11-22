package univ.yesummit.global.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import univ.yesummit.global.auth.util.JwtUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/kakao")
public class AuthController {

    private final JwtUtils jwtUtils;

    @GetMapping("/login")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 진행합니다.")
    public RedirectView login() {
        return new RedirectView("/oauth2/authorization/kakao");
    }

    @GetMapping("/token")
    @Operation(summary = "로그인 후 토큰 요청", description = "로그인 후 accessToken과 refreshToken을 발급합니다.")
    public ResponseEntity<Map<String, String>> getToken(@RequestParam Long userId) {
        // JWT 토큰 생성
        String accessToken = jwtUtils.createAccessToken(userId);
        String refreshToken = jwtUtils.createRefreshToken(userId);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok(tokens);
    }
}
