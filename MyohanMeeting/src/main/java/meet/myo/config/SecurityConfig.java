package meet.myo.config;

import lombok.RequiredArgsConstructor;
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


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    // 패스워드 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // csrf가 필요없어서 disable
                .csrf().disable()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션 사용하지 않음
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // HttpServletRequest를 사용하는 요청에 대한 접근 제한 설정 부분
                .and()
                .authorizeHttpRequests()

                // h2 console 관련 세팅
                // TODO: test 환경에서만 작동하도록 설정해야 함
                .requestMatchers(HttpMethod.GET, "/favicon.ico").permitAll()
                .requestMatchers(HttpMethod.GET, "/h2/console").permitAll()
                .requestMatchers(HttpMethod.GET, "/h2/console/**").permitAll()

                // 회원가입, 로그인 요청은 권한 없이도 permit하도록 설정
                .requestMatchers(HttpMethod.POST, "/v1/member/direct").permitAll()
                .requestMatchers(HttpMethod.POST, "v1/member/oauth").permitAll()
                .requestMatchers(HttpMethod.GET, "/v1/auth/signin/direct").permitAll()
                .requestMatchers(HttpMethod.GET, "/v1/auth/signin/oauth").permitAll()
                .anyRequest().authenticated()

                // JwtSecurityConfig 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }


}
