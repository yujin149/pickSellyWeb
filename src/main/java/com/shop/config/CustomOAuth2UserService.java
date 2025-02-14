package com.shop.config;


import com.shop.dto.SessionUser;
import com.shop.entity.User;
import com.shop.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession httpSession;

    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException{
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();


        //registrationId 카카오 네이버 구분 / 구굴은 클라이언트 name을 구별을 안해놔서 조건 else로 구글을 넣어야함.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes); //DB 넣어주고 있음.
        httpSession.setAttribute("user", new SessionUser(user));

        //로그인 하면 user로 역할을 줌.
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            , attributes.getAttributes()
            , attributes.getNameAttributeKey()
        );
    }


    private User saveOrUpdate(OAuthAttributes attributes) {
        //만약에, 이메일이 있으면, entity(변수)에 name, picture를 바꿔줌. (객체가 있을때만 가능)
        //그렇지 않으면, (이메일이 없으면) 새로 객체 만듬(name, email, picture을 만들어주고있음)
        User user = userRepository.findByEmail(attributes.getEmail())
            //map 위에 나온 정보를 entity에 빼주고 여기에 업데이트 한다음에 name과 picture를 바꿔줌.
            //orElse 그렇지 않으면 새로 만듬.
            .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
            .orElse(attributes.toEntity());
        return userRepository.save(user);
    }
}











