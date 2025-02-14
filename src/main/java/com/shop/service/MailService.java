package com.shop.service;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class MailService {
    private final JavaMailSender javaMailSender; // 메일 전송을 위한 JavaMailSender 객체
    @Value("${mail.username}") // 환경변수에서 발신자 이메일 가져오기
    private String senderEmail;


    // 인증번호 저장을 위한 Map
    private Map<String, String> authCodes = new HashMap<>();
    private Map<String, Boolean> emailVerified = new HashMap<>(); // 이메일 인증 상태 저장

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender; // JavaMailSender 객체 주입
    }

    // 인증번호 확인 후 인증 상태 업데이트
    public boolean verifyAuthCode(String email, String code) {
        String storedCode = authCodes.get(email); // 저장된 인증번호 가져오기
        boolean isValid = code.equals(storedCode); // 입력된 인증번호와 비교
        if (isValid) {
            emailVerified.put(email, true); // 인증 성공 시 이메일 상태 업데이트
        }
        return isValid; // 인증 결과 반환
    }

    // 이메일 인증 상태 확인
    public boolean isEmailVerified(String email) {
        return emailVerified.getOrDefault(email, false); // 이메일 인증 상태 반환, 기본값은 false
    }

    // 랜덤으로 숫자 생성
    public String createNumber() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 6; i++) { // 인증 코드 6자리
            int index = random.nextInt(3); // 0~2까지 랜덤, 랜덤값으로 switch문 실행

            switch (index) {
                case 0 -> key.append((char) (random.nextInt(26) + 97)); // 소문자
                case 1 -> key.append((char) (random.nextInt(26) + 65)); // 대문자
                case 2 -> key.append(random.nextInt(10)); // 숫자
            }
        }
        return key.toString(); // 생성된 인증번호 반환
    }

    public MimeMessage createMail(String mail, String number) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage(); // MimeMessage 객체 생성

        // 발신자 이메일을 임의로 설정하거나 환경 변수에서 가져와 설정
        message.setFrom(senderEmail); // 환경변수로 설정한 발신자

        message.setRecipients(MimeMessage.RecipientType.TO, mail); // 수신자 설정
        message.setSubject("[PICKSELLY] 이메일 인증번호입니다."); // 메일 제목 설정
        String body = "";
        body += "<h3 style=\"font-family: pretendard; font-size:2.5rem; font-weight:700; margin-bottom:20px;\">이메일 인증 코드 발송</h3>";
        body += "<p style=\"font-family:pretendard; font-size:1rem; line-height:1.6; margin-bottom:20px; color:#606060;\">PICKSELLY에 가입하신 것을 환영합니다.<br> 아래의 인증코드를 입력하시면 가입이 정상적으로 완료됩니다.</p>";
        body += "<h1 style=\"font-family:pretendard; background:#f5f5f5; padding:30px; \">" + number + "</h1>"; // 인증번호 포함
        body += "<p style=\"font-family:pretendard; font-size:1rem; line-height:1.6; margin-top:20px; margin-bottom:3rem; color:#606060;\">본 메일은 발신전용이며, 문의에 대한 회신은 처리되지 않습니다.<br>감사합니다.</p>";
        message.setText(body, "UTF-8", "html"); // 메일 본문 설정

        return message; // 생성된 메일 반환
    }

    // 메일 발송
    public String sendSimpleMessage(String sendEmail) throws MessagingException {
        String number = createNumber(); // 랜덤 인증번호 생성
        authCodes.put(sendEmail, number); // 인증번호 저장
        MimeMessage message = createMail(sendEmail, number); // 메일 생성
        try {
            javaMailSender.send(message); // 메일 발송
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("메일 발송 중 오류가 발생했습니다: " + e.getMessage()); // 예외 처리
        }
        return number; // 생성된 인증번호 반환
    }
}