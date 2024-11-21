package univ.yesummit.global.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/v1/api/kakao")
public class AuthController {

    @GetMapping("/login")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 진행합니다.")
    public RedirectView login() {
        return new RedirectView("/oauth2/authorization/kakao");
    }
}
