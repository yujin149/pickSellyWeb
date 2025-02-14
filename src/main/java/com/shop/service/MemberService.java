package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor

public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository; //자동 주입됨

    //전화번호를 하이픈(-)을 포함한 형식으로 변환하는 메소드
    public String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        // 숫자만 있는 전화번호를 하이픈 포함 형식으로 변환
        //replaceAll("[^0-9]", "")는 숫자 외의 문자를 제거한다. (010-1111-1111에서 -을 제거)
        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");  // 숫자만 남기기

        if (phoneNumber.length() == 11) { //문자 길이가 11이라면 010-1111-1111으로
            return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 7) + "-" + phoneNumber.substring(7);
        } else if (phoneNumber.length() == 10) { //문자 길이가 10이라면 010-111-1111으로
            return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6);
        }
        return phoneNumber;  // 전화번호 길이가 10자리 또는 11자리가 아니면 그대로 반환
    }

    public Member saveMember(Member member) {
        // 중복 회원 체크
        validateDuplicateMember(member);

        // 전화번호를 하이픈이 포함된 형식으로 저장
        String formattedPhone = formatPhoneNumber(member.getTel());
        member.setTel(formattedPhone);  // 하이픈포함된 전화번호 저장.

        member.setPostcode(member.getPostcode()); // 우편번호 저장
        member.setAddress(member.getAddress()); // 주소 저장
        member.setBirth(member.getBirth()); // 생년월일 저장
        member.setGender(member.getGender()); // 성별 저장

        //데이터베이스에 저장을 하라는 명령
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            // 예외가 발생하면 throw해서 controller에서 화면 적용.
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

        // 전화번호 중복 체크
        Member findMemberPhone = memberRepository.findByTel(member.getTel());
        if (findMemberPhone != null) {
            throw new IllegalStateException("중복된 전화번호입니다.");
        }
    }

    //로그인 추가
    //로그인할 유저의 email을 파라미터로 전달받음.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);


        if(member == null){
            throw new UsernameNotFoundException(email);
        }
        //빌더 패턴
        //UserDetail을 구현하고 있는 User 객체를 반환.
        //User 객체를 생성하기 위해 생성자로 회원의 이메일, 비밀번호, role을 파라미터로 넘겨줌.
        return User.builder()
            .username(member.getName())
            //.username(member.getEmail())
            .password(member.getPassword())
            .roles(member.getRole().toString())
            .build();
    }

    //사용자 이름 가져오기
    public String getMemberByName(String email){
        System.out.println("service 확인용 : " + email);  // 호출 여부 확인용
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            // 로그에 출력하거나, 데이터베이스에서 찾을 수 없다고 알려주기
            System.out.println("service 로그인 정보 Null: " + email);
            return null;  // 사용자가 없으면 null 반환
        }else{
            System.out.println("service 로그인 정보 : " + member.getName());
            return member.getName(); // 사용자의 이름 반환
        }
    }
}