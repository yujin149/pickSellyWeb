package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;


@SpringBootTest
//MockMvc를 사용하기 위해서 어노테이션을 추가 (MockMvc는 가짜 객체)
@AutoConfigureMockMvc //가짜 config
@Transactional //문제가 발생하면 모든 작업 취소
@TestPropertySource(locations = "classpath:application-test.properties")

class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc; // 테스트에 필요한 가짜 객체 -> 웹브라우저에서 요청하는 것처럼 만들어주는 객체

    @Autowired
    PasswordEncoder passwordEncoder;

    // 로그인 전 회원을 등록하는 메소드
    public Member createMember(String email, String password) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email,password);
        //userParameter()를 이용하여 이메일을 아이디로 세팅하고 로그인 url 요청.
        //formLogin() -> Spring Security에 있는 로그인 동작
        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email).password(password))
            //andExpect -> 동작이 올바르게 되는 기대를 하는 것을 확인.
            //SecurityMockMvcResultMatchers.authenticated() 결과가 올바르게 인증이 됐는지 확인.
            .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception{
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);
        mockMvc.perform(formLogin().userParameter("email")
            .loginProcessingUrl("/members/login")
            .user(email).password("12345"))
            .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

}