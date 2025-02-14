package com.shop.config;


import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//config 설정
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    MemberService memberService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
            //anyRequest()는 모든 요청을 다 처리하기 때문에 그 뒤에 다른 요청을 처리하면 안된다.
            // /emailCheck 경로에 대한 접근 허용
            //이메일 인증 요청 처리하는 엔드포인트에 대해 접근 허용.
            .requestMatchers("/emailCheck").permitAll()
            //인증번호 검증 요청을 처리하는 엔드포인트에 대한 접근을 허용
            .requestMatchers("/verifyAuthCode").permitAll()

            //requestMatchers ()안에 넣은 거 오류 제외하기 위해
            .requestMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/error").permitAll()
            .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
            //url이 admin이면 Role이 ADMIN일때만 할수 있게!!
            // "/admin/**" admin 밑에 있는 하위 어떤 url 포함 된다!
            .requestMatchers("/admin/**").hasRole("ADMIN")
            //위에를 제외한 모든 url 맵핑은 모두 로그인이 되어야 접속 가능.
            //.anyRequest().authenticated() // 요청 인증에 맞게
            .anyRequest().permitAll() // 다른 요청은 모두 허용
        ).formLogin(formLogin -> formLogin //form 로그인 경우 여기로 온다.
            .loginPage("/members/login") // 로그인 페이지는 /members/login (url)
            .defaultSuccessUrl("/") // 로그인이 성공하면 "/"(url)
            .usernameParameter("email") // 로그인에 필요한 파라미터("email") // MemberService.java에서
            .failureUrl("/members/login/error") // 실패 시 이동 url /members/login/error
        ).logout(logout -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃 실행
            .logoutSuccessUrl("/") // 로그아웃 성공 시 실행
        ).oauth2Login(oauthLogin -> oauthLogin
            .defaultSuccessUrl("/")
            .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                .userService(customOAuth2UserService))
        );
        //exceptionHandling 예외처리 핸들링 / 예외처리가 발생하면 CustomAuthenticaitonEntryPoint 클래스에 위임.
        //CustomAuthenticaitonEntryPoint.java
        http.exceptionHandling(exception -> exception
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        return http.build();
    }

    @Bean //패스워드 암호화 해주는 객체
    public static PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    //AuthenticationManagerBuilder -> UserDetailService를 구현 하고 그 객체 MemberService 지정과
    //동시에 비밀번호를 암호화
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
}
