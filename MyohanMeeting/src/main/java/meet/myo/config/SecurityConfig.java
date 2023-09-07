package meet.myo.config;

import lombok.RequiredArgsConstructor;
import meet.myo.jwt.JwtAccessDeniedHandler;
import meet.myo.jwt.JwtAuthenticationEntryPoint;
import meet.myo.jwt.JwtSecurityConfig;
import meet.myo.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    // 패스워드 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests()

                // 문서, h2 console 관련 세팅
                // TODO: test 환경에서만 작동하도록 설정해야 함
                .requestMatchers(HttpMethod.GET, "/favicon.ico").permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()

                // 로그인 및 토큰 refresh
                .requestMatchers(HttpMethod.POST, "/v1/auth/signin/direct").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/auth/signin/oauth").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/auth/refresh").permitAll()

                // 회원가입
                .requestMatchers(HttpMethod.POST, "/v1/member/direct").permitAll()
                .requestMatchers(HttpMethod.POST, "v1/member/oauth").permitAll()

                // 이메일, 닉네임 중복확인
                .requestMatchers(HttpMethod.GET, "/v1/member/email").permitAll()
                .requestMatchers(HttpMethod.GET, "/v1/member/nickname").permitAll()

                // 이메일 인증
                .requestMatchers(HttpMethod.POST, "/v1/member/certification").permitAll()
                .requestMatchers(HttpMethod.PUT, "/v1/member/certification").permitAll()

                // 입양공고, 입양공고 덧글
                .requestMatchers(HttpMethod.GET, "/v1/adoption/notices").permitAll()
                .requestMatchers(
                        RegexRequestMatcher.regexMatcher(
                                HttpMethod.GET, "/v1/adoption/notices/[0-9]+")).permitAll()
                .requestMatchers(
                        RegexRequestMatcher.regexMatcher(
                                HttpMethod.GET, "/v1/adoption/notices/[0-9]+/comments")).permitAll()
                .anyRequest().authenticated()

                // JwtSecurityConfig 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }


}
