package com.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl(); // JavaMailSenderImpl 객체 생성
        mailSender.setHost("smtp.gmail.com"); // SMTP 서버 호스트 설정
        // SMTP 서버 포트 설정
        //일반적으로 TLS를 사용할 때 587 포트를 사용
        mailSender.setPort(587);
        //발신자 이메일, 비밀번호는 환경변수로 설정. 실제 비밀번호는 환경변수로 관리하는 것이 좋다.
        mailSender.setUsername(username); // application.properties에서 읽어온 발신자 이메일
        mailSender.setPassword(password); // application.properties에서 읽어온 앱 비밀번호

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp"); // 메일 전송 프로토콜 설정
        props.put("mail.smtp.auth", "true"); // SMTP 인증 사용 설정
        props.put("mail.smtp.starttls.enable", "true"); // STARTTLS 사용하여 보안 연결 설정.
        props.put("mail.debug", "true"); // 메일 전송과정에서 디버그 정보를 출력하도록 설정.

        return mailSender; // 설정된 JavaMailSender 객체 반환
    }
}
