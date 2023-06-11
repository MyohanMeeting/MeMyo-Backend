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
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig{

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // 패스워드 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // antMatchers -> requestMatchers 로 바뀌었음

    // h2 테스트를 위해 ignoring 해두었음
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .ignoring()
                .requestMatchers(
                        "/h2-console/**",
                        "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // csrf가 필요없어서 disable
                .csrf().disable()

                // 401, 403 Exception 핸들링
                // RestException보다 먼저 터지는 지 모르겠음..
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // HTTP 응답 보안 관련 된 것 같은데 자세히는 모르겠음... 알려주세용..
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션 사용하지 않음
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // TODO: 컨트롤러 작업 시 수정
                // HttpServletRequest를 사용하는 요청에 대한 접근 제한 설정 부분
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "").permitAll()
                .requestMatchers(HttpMethod.POST, "").permitAll()
                .requestMatchers(HttpMethod.POST, "").permitAll()
                .anyRequest().authenticated()

                // JwtSecurityConfig 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }


}
