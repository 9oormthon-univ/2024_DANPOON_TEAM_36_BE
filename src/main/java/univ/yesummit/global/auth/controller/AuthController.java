package univ.yesummit.global.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/v1/api/kakao")
public class AuthController {

    @GetMapping("/login")
    public RedirectView login() {
        return new RedirectView("/oauth2/authorization/kakao");
    }
    // test13
}
