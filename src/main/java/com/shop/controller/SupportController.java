package com.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SupportController {
    /*문의하기 리스트*/
    @GetMapping(value = "/support/inquiry")
    public String inquiry() {
        return "support/inquiry";
    }

    /*문의 작성하기*/
    @GetMapping(value = "/support/inquiryWrite")
    public String inquiryWrite() {
        return "support/inquiryWrite";
    }
}
