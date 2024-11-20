package univ.yesummit.global.exception;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @Operation(summary = " 접근 거부 처리 ", description = " 접근 거부 처리 ")
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";  // 뷰 이름 반환
    }
}
