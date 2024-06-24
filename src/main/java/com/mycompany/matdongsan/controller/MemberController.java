package com.mycompany.matdongsan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.matdongsan.dto.Member;
import com.mycompany.matdongsan.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	//로그인
	
	
	//회원가입
	@PostMapping("/joinByMember")
	public Member joinByMember(@RequestBody Member member) {
		// 비밀번호 암호화
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		member.setMpassword(passwordEncoder.encode(member.getMpassword()));
		
		// 아이디 활성화
		member.setMremoved(false);
		
		// 권한 설정
		
		memberService.joinByMember(member);
		member.setMpassword(null);
		return member;
	}
	
	//탈퇴
	
	//비밀번호 수정
	
	// 
}
