package com.shop.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
//인증되지 않은 사용자가 리소스 요청하면 차단하는 클래스

//AuthenticationEntryPoint는 사용자가 인증되지 않은 상태에서 보호된 리소스에 접근할 때 호출.
//commence 메서드를 사용하여 어떤 응답을 반환할지 정의.
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException{
        //인증되지 않은 사용자가 보호된 리소스를 접근했을 때 에러를 보내는 부분.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized"); //401 Unauthorized 에러를 반환 (권한이 없는 에러)
    }
}
