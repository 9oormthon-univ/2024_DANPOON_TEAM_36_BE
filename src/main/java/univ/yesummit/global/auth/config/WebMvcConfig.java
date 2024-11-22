package univ.yesummit.global.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import univ.yesummit.global.resolver.AuthArgumentResolver;

import java.util.List;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthArgumentResolver authArgumentResolver;

//    @Override
//    public void addCorsMappings(final CorsRegistry registry ){
//        registry.addMapping("/**")
//                .allowedOriginPatterns("*")
//                .allowedMethods("PATCH","GET","POST","PUT","DELETE","HEAD","OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers); // 기존 Resolver
        resolvers.add(authArgumentResolver); // 커스텀 Resolver
    }
}
