package archive.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 컴포넌트 스캔에 적용되도록 어노테이션을 달아줍니다.
@EnableWebSecurity // 모든 요청 URL이 스프링 시큐리티의 필터체인을 거치도록 하는 어노테이션입니다.
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)  // csrf 비활성
                .httpBasic(HttpBasicConfigurer::disable)    // http basic Auth 기반 인증 비활성
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 미사용
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error", "/favicon.ico").permitAll()
                        .requestMatchers("/api/**").permitAll()   // API 다 열어두기(개발용)
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    //필터체인을 거치지 않을 URL
    private static final String[] IGNORE_FILTER_URLS = {
            "/" // 루트 경로
            //,   "/api/user/sign"   // 회원 가입

    };

    //필터체인을 무시하도록 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(IGNORE_FILTER_URLS);
    }
}