package univ.yesummit.global.auth.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class SecurityUtil {

    public static String getLoginUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            return ((OAuth2User) principal).getAttribute("name"); // 필요에 따라 변경 가능
        } else if (principal instanceof String) {
            return (String) principal;
        }

        throw new IllegalStateException("Unknown principal type");
    }
}