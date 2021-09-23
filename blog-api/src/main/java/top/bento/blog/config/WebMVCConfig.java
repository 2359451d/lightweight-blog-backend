package top.bento.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.bento.blog.handler.AuthenticationInterceptor;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // CrossOrigin configuration:
        // cannot be set to *
        // frontend port: 8080
        registry.addMapping("/**").allowedOrigins("http://localhost:8080").allowedOrigins("http://localhost:8081");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(authenticationInterceptor)
        //        .addPathPatterns("/**").excludePathPatterns("/login").excludePathPatterns("/register").
        //        excludePathPatterns("/logout");
    }

}
