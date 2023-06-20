package meet.myo.service;

import jakarta.transaction.Transactional;
import meet.myo.domain.Member;
import meet.myo.domain.OauthType;
import meet.myo.domain.exception.NotFoundException;
import meet.myo.dto.request.MemberOauthCreateRequestDto;
import meet.myo.repository.MemberRepository;
import org.apache.el.stream.Optional;
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


        Member member = memberRepository.findByNickName(OauthType.KAKAOTALK + "_" + oauthId);

        if (member == null){
            Member newMember = Member.oauthJoinBuilder()
                    .email(email)
                    .oauthType(OauthType.KAKAOTALK)
                    .oauthId(oauthId)
                    .nickName(OauthType.KAKAOTALK + "_" + oauthId)
                    .build();

            newMember = memberRepository.save(newMember);
        }
        /**
         *         TODO: JWT토큰 만드는 부분 어떻게 구현할지 모르겠음..
         *
          */


        return oAuth2User;

    }
}
