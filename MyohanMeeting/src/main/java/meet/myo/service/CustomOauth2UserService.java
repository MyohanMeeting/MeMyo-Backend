package meet.myo.service;

import jakarta.transaction.Transactional;
import meet.myo.domain.Member;
import meet.myo.domain.OauthType;
import meet.myo.dto.request.MemberOauthCreateRequestDto;
import meet.myo.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Transactional
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private MemberRepository memberRepository;
    private MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 사용자 정보 추출
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String oauthId = (String) attributes.get("id");  // 카카오에서 제공하는 사용자 ID
        String email = (String) attributes.get("email");  // 카카오에서 제공하는 이메일

        // DTO 생성
        // TODO:저장하는 부분을 어떻게 처리할지 모르겠음..
//        MemberOauthCreateRequestDto dto = new MemberOauthCreateRequestDto().toEntity();
//                .oauthType(OauthType.KAKAOTALK)
//                .oauthId(oauthId)
//                .email(email)
//                .build();


        // 저장
//        memberService.oauthJoin(dto.toEntity());

        return oAuth2User;

    }
}
