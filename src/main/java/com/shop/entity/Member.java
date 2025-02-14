package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Entity //엔티티
@Table(name="member") //테이블명
@Getter
@Setter
@ToString
public class Member {
    //기본키 컬럼명 = member_id AI -> 데이터 저장시 1씩 증가
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //중복 허용X
    @Column(unique = true)
    private String email;

    //알아서 설정
    private String password;

    //알아서 설정
    private String name;

    //중복 허용X
    @Column(unique = true)
    private String tel;

    private String postcode; // 우편번호

    //알아서 설정
    private String address;

    private LocalDate birth; //생년월일
    private String gender; //성별




    //Enum -> 컴 : 숫자 // 우리 : 문자
    //데이터베이스 문자 그대로 -> USER, ADMIN
    @Enumerated(EnumType.STRING)
    private Role role;

    //PasswordEncoder passwordEncoder는 SecurityConfig에서 @Bean 패스워드 암호화 해주는 객체와 연결
    //static이라 바로 메모리에 올라감.
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setEmail(memberFormDto.getEmail());
        member.setName(memberFormDto.getName());
        member.setTel(memberFormDto.getTel());
        member.setPostcode(memberFormDto.getPostcode());
        member.setAddress(memberFormDto.getAddress());
        member.setBirth(memberFormDto.getBirth());
        member.setGender(memberFormDto.getGender());

        //보안상 안전한 형식으로 바꿔줌 -> 암호화 시킴
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password); // 암호화된 비밀번호를 엔티티에 저장
        member.setRole(Role.ADMIN); // 역할을 관리자(Admin)로 설정
        return member; // 엔티티를 반환
    }
}
