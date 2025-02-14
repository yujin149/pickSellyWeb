package com.shop.controller;

import com.shop.dto.MailDto;
import com.shop.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService; // MailService 객체를 주입받기 위한 필드

    @ResponseBody // 메서드의 반환값을 HTTP 응답 본문으로 사용
    @PostMapping("/emailCheck") // POST 요청을 처리하는 메서드, "/emailCheck" 경로에 매핑
    //클라이언트로부터 MailDto 객체를 JSON형식으로 받아옴.
    public String emailCheck(@RequestBody MailDto mailDto) throws MessagingException, UnsupportedEncodingException {
        // mailDto 객체는 클라이언트로부터 받은 JSON 데이터로 자동으로 초기화된다.
        String authCode = mailService.sendSimpleMessage(mailDto.getEmail()); // 이메일로 인증번호 전송
        return authCode; // Response body에 값을 반환 // 생성된 인증번호를 반환
    }

    @ResponseBody
    @PostMapping("/verifyAuthCode") // POST 요청을 처리하는 메서드, "/verifyAuthCode" 경로에 매핑
    //클라이언트로부터 이메일과 인증번호를 받아오는 메서드.
    public String verifyAuthCode(@RequestParam String email, @RequestParam String code) {
        boolean isValid = mailService.verifyAuthCode(email, code);
        if (isValid) {
            return "이메일 인증에 성공하였습니다."; // 인증이 성공하면 메시지 반환
        } else {
            return "인증에 실패하였습니다."; // 인증이 실패하면 메시지 반환
        }
    }
}
