package meet.myo.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private RedisTemplate<String, String> redisTemplate;
    private final TokenProvider tokenProvider;

    public JwtFilter(RedisTemplate<String, String> redisTemplate, TokenProvider tokenProvider) {
        this.redisTemplate = redisTemplate;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(
            ServletRequest request, ServletResponse response, FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String accessToken = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken, true)) {
            validBlackToken(accessToken);
            Authentication authentication = tokenProvider.getAuthentication(accessToken, true);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        chain.doFilter(request, response);
    }

    private void validBlackToken(String accessToken) {
        String blackList = redisTemplate.opsForValue().get(accessToken);
        if(StringUtils.hasText(blackList)) {
            throw new AccessDeniedException("SIGNOUT_TOKEN");
        }
    }

    // Bearer 토큰인지 판단하는 부분
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
