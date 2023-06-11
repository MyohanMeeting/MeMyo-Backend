package meet.myo.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import meet.myo.config.SecurityUtil;
import meet.myo.domain.authority.CustomUser;
import meet.myo.service.CustomUserDetailsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private static final String AUTH_CLAIM_NAME = "auth";

    private final long accessTokenValidityTime;
    private final long refreshTokenValidityTime;

    private final String accessSecret;
    private final String refreshSecret;
    private Key accessKey;
    private Key refreshKey;

    private final CustomUserDetailsService customUserDetailsService;

    // yml 파일에서 jwt의 access, refresh를 들고옴
    public TokenProvider(
            @Value("${jwt.access-token-validity-time}") long accessTokenValidityTime,
            @Value("${jwt.access-secret}") String accessSecret,
            @Value("${jwt.refresh-secret}") String refreshSecret,
            @Value("${jwt.refresh-token-validity-time}") long refreshTokenValidityTime,
            @Autowired CustomUserDetailsService customUserDetailsService
    ) {
        this.accessTokenValidityTime = accessTokenValidityTime;
        this.refreshTokenValidityTime = refreshTokenValidityTime;
        this.accessSecret = accessSecret;
        this.refreshSecret = refreshSecret;
        this.customUserDetailsService = customUserDetailsService;
    }

    // BASE64로 Decode하는 코드
    @Override
    public void afterPropertiesSet() {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret));
    }

    public String getAccessToken(Authentication authentication) {
        return createToken(authentication, accessTokenValidityTime, accessKey, true);
    }

    public String getRefreshToken(Authentication authentication) {
        return createToken(authentication, refreshTokenValidityTime, refreshKey, false);
    }

    // 인증 부분
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails principal;
        Collection<? extends GrantedAuthority> authorities;

        if (claims.containsKey(AUTH_CLAIM_NAME) && claims.containsKey("memberId")) { // 액세스 토큰일 경우(클레임 존재) DB접근 없이 클레임에서 찾음
            authorities = Arrays.stream(claims.get(AUTH_CLAIM_NAME).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            principal = new CustomUser(claims.getSubject(), "", authorities, Long.valueOf(claims.get("memberId").toString()));

        } else { // ref 토큰일 경우(클레임 없음) DB접근 후 찾음
            principal = customUserDetailsService.loadUserByUsername(claims.getSubject());
            authorities = principal.getAuthorities();
        }

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // 토큰 검증 부분
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            // 잘못된 JWT 형식
        } catch (ExpiredJwtException e) {
            // 만료된 JWT
        } catch (UnsupportedJwtException e) {
            // 지원하지 않는 JWT
        } catch (IllegalArgumentException e) {
            // 잘못된 토큰
        }
        return false;
    }

    // 토큰 생성 부분
    private String createToken(Authentication authentication, long tokenValidityTime, Key key, boolean claimIncluded) {
        String authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + tokenValidityTime);

        JwtBuilder builder = Jwts.builder()
                .setSubject(authentication.getName());

        // 이미 클레임이 존재하면 refreshtoken 만드는 부분
        if (claimIncluded) {
            builder.claim(AUTH_CLAIM_NAME, authorities)
                    .claim("memberId", SecurityUtil.getCurrentUserPK().orElse(null));
        }


        return builder.signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }
}
