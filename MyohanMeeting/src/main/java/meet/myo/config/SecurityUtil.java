package meet.myo.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meet.myo.domain.authority.CustomUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

    private static final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    public static Optional<String> getCurrentUserName() {

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

        if (authentication == null) {
            log.debug("Security Context에 관련 정보가 없습니다.");
            return Optional.empty();
        }

        Long userPK = null;
        if (authentication.getPrincipal() instanceof CustomUser) {
            CustomUser user = (CustomUser) authentication.getPrincipal();
            userPK = user.getUserPK();
        } else if (authentication.getPrincipal() instanceof String) {
            userPK = null;
        }

        return Optional.ofNullable(userPK);
    }
}
