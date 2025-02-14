package com.shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Getter
@Setter
public class MemberFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name; //이름

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    //@Length(min=8, max = 16, message = "비밀번호는 8자이상, 16자 이하로 입력해주세요.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
        message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 포함된 8자 ~ 16자의 비밀번호여야 합니다.")
    private String password; //비밀번호


    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email; //이메일

    //이메일 인증번호를 저장할 필드
    private String authCode; 

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(01[0-9])[-]?[0-9]{3,4}[-]?[0-9]{4}$", message = "전화번호는 010-XXXX-XXXX 또는 010XXXXXXXX으로 입력해주세요.")
    private String tel; //전화번호

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String postcode; // 우편번호

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address; //주소 (주소 + 상세주소)

    private LocalDate birth; //생년월일

    private String gender; //성별


}
