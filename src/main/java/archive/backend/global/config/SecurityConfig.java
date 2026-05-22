package archive.backend.global.config;

import archive.backend.global.filter.JwtAuthenticationFilter;
import archive.backend.global.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

@Configuration // 而댄룷?뚰듃 ?ㅼ틪???곸슜?섎룄濡??대끂?뚯씠?섏쓣 ?ъ븘以띾땲??
@EnableWebSecurity // 紐⑤뱺 ?붿껌 URL???ㅽ봽留??쒗걧由ы떚???꾪꽣泥댁씤??嫄곗튂?꾨줉 ?섎뒗 ?대끂?뚯씠?섏엯?덈떎.
@RequiredArgsConstructor
public class SecurityConfig {

    // provider 二쇱엯
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)  // csrf 鍮꾪솢??
                .httpBasic(HttpBasicConfigurer::disable)    // http basic Auth 湲곕컲 ?몄쬆 鍮꾪솢??
                .formLogin(AbstractHttpConfigurer::disable) // form 濡쒓렇??鍮꾪솢??
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ?몄뀡 誘몄궗??
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error", "/favicon.ico").permitAll()
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/api/swagger-ui.html",
                                "/api/swagger-ui/**",
                                "/api/v3/api-docs/**",
                                "/api/v3/api-docs.yaml"
                        ).permitAll()
                        .requestMatchers("/api/auth/**").permitAll()   // ?몄쬆 愿??API 怨듦컻
                        .requestMatchers("/api/news/**").permitAll()   // ?댁뒪 議고쉶 API 怨듦컻
                        .requestMatchers(HttpMethod.GET, "/api/record/**").permitAll() // 湲곕줉 議고쉶 API 怨듦컻
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/record/**").authenticated() // 湲곕줉 ?깅줉/?섏젙/??젣???몄쬆 ?꾩슂
                        .anyRequest().authenticated()
                )

                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // pattern??"*"濡??ㅼ젙?섎㈃ 紐⑤뱺 ?꾨찓?몄쓣 ?덉슜?섎ŉ, allowCredentials(true)????④퍡 ?????덉뒿?덈떎.
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    // 鍮꾨?踰덊샇 ?댁떆??鍮??깅줉
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //?꾪꽣泥댁씤??嫄곗튂吏 ?딆쓣 URL
    private static final String[] IGNORE_FILTER_URLS = {
    };

    //?꾪꽣泥댁씤??臾댁떆?섎룄濡??ㅼ젙
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(IGNORE_FILTER_URLS);
    }
}
