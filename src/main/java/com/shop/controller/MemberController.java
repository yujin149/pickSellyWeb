package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import com.shop.service.MailService;
import com.shop.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        // 회원가입 폼을 GET 방식으로 보여줌
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        //@Valid 붙은 객체를 검사해서 결과에 에러가 있으면 실행
        if(bindingResult.hasErrors()){
            memberFormDto.setPostcode(""); // 우편번호 초기화
            memberFormDto.setAddress(""); // 주소 초기화
            return "member/memberForm"; // 다시 회원가입으로 돌려보냅니다. GET
        }

        // gender가 null이 아닌지 확인
        //System.out.println("Selected Gender: " + memberFormDto.getGender());

        // 이메일 인증 상태 확인
        //mailService의 isEmailVerified 메서드를 호출하여 인증 상태를 체크
        if (!mailService.isEmailVerified(memberFormDto.getEmail())) {
            // 이메일 인증이 완료되지 않은 경우, 에러 메시지를 추가
            bindingResult.rejectValue("email", "error.email", "이메일 인증이 필요합니다."); // 이메일 필드에 에러 메시지 추가
            model.addAttribute("memberFormDto", memberFormDto); // 인증번호를 유지하기 위해 현재 입력된 데이터를 모델에 추가
            return "member/memberForm"; // 다시 회원가입 폼으로 돌려보냄
        }

        try{
            //Member 객체 생성
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            //데이터베이스에 저장
            memberService.saveMember(member);
        }
        catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm"; //다시 회원가입으로 돌려보냅니다. GET
        }
        return "redirect:/";
    }

    //로그인
    @GetMapping(value = "/login")
    public String loginMember(){
        return "member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "member/memberLoginForm";
    }





}
