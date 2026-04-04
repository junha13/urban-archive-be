package archive.backend.global.config;

import archive.backend.global.filter.JwtAuthenticationFilter;
import archive.backend.global.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration // 컴포넌트 스캔에 적용되도록 어노테이션을 달아줍니다.
@EnableWebSecurity // 모든 요청 URL이 스프링 시큐리티의 필터체인을 거치도록 하는 어노테이션입니다.
@RequiredArgsConstructor
public class SecurityConfig {

    // provider 주입
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)  // csrf 비활성
                .httpBasic(HttpBasicConfigurer::disable)    // http basic Auth 기반 인증 비활성
                .formLogin(AbstractHttpConfigurer::disable) // form 로그인 비활성
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 미사용
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error", "/favicon.ico").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()   // API 다 열어두기(개발용)
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        //.anyRequest().authenticated() // 인증 필요
                        .anyRequest().permitAll() // 다 열어두기
                )

                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // pattern을 "*"로 설정하면 모든 도메인을 허용하며, allowCredentials(true)와도 함께 쓸 수 있습니다.
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    // 비밀번호 해시화 빈 등록
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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