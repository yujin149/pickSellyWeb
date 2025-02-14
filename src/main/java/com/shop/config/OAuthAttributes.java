package com.shop.config;

import com.shop.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public OAuthAttributes(){

    }
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        if(registrationId.equals("kakao")){
            return ofKakao(userNameAttributeName, attributes);
        }
        if (registrationId.equals("naver")){
            return ofNaver(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);

    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes){
        //네이버에서 받은 데이터에서 프로필 정보가 담긴 response 값을 꺼낸다.
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return new OAuthAttributes(attributes, userNameAttributeName,
            (String) response.get("name"),
            (String) response.get("email"),
            (String) response.get("profile_image")
        );
    }
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes){
        //카카오로 받은 데이터에서 계정 정보가 담긴 Kakao_account 값을 꺼낸다.
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");

        //마찬가지로 profile(nickname, image_url.. 등)정보가 담긴 값을 꺼낸다.
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");

        //카카오에서 설정한 닉네임 이메일 프로필사진
        return new OAuthAttributes(attributes, userNameAttributeName,
            (String) profile.get("nickname"),
            (String) kakao_account.get("email"),
            (String) profile.get("profile_image_url")
        );
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){

        return new OAuthAttributes(attributes, userNameAttributeName,
            (String) attributes.get("name"),
            (String) attributes.get("email"),
            (String) attributes.get("picture")
        );
    }
    public User toEntity(){ return new User(name, email, picture);}

}
