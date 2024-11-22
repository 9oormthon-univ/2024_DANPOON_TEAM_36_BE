package univ.yesummit.global.auth.service;

import java.util.Map;

public interface AuthService {

    Map<String, Object> generateTokens(Long userId) throws Exception;
}
