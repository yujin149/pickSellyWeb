package com.shop.controller;

import com.shop.entity.Member;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor

public class MainController {
    private final MemberService memberService;

    @GetMapping(value = "/")
    public String MainController(Principal principal, Model model) {
        /*로그인 시 사용자 이름 가져오기*/
/*
        if (principal == null) {
            // 로그인하지 않은 사용자의 경우 처리
            return "main";
        }

        String email = principal.getName(); // 로그인한 사용자의 이메일 가져오기
        System.out.println("controller 로그인한 정보 가져오기: " + email);

        // 이메일로 사용자 이름 조회
        String name = memberService.getMemberByName(email); // service에서 이메일로 이름 가져오기

        if (name  == null) {
            System.out.println("controller 로그인 정보 Null : " + email);  // 사용자 없음
        } else {
            model.addAttribute("name", name);
        }


 */
        return "main";
    }

}
