package meet.myo.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meet.myo.domain.authority.CustomOAuth2User;
import meet.myo.domain.authority.CustomUser;
import meet.myo.exception.NotAuthenticatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

    public static Optional<String> getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.debug("Security Context에 관련 정보가 없습니다.");
            return Optional.empty();
        }

        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(username);
    }

    public static Optional<Long> getCurrentUserPK() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.debug("Security Context에 관련 정보가 없습니다.");
            return Optional.empty();
        }

        Long userPK = null;
        if (authentication.getPrincipal() instanceof CustomUser) {
            CustomUser user = (CustomUser) authentication.getPrincipal();
            userPK = user.getUserPK();
        } else if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
            userPK = user.getUserPK();
        } else {
            throw new NotAuthenticatedException("로그인 정보가 존재하지 않습니다.");
        }

        return Optional.ofNullable(userPK);
    }
}
