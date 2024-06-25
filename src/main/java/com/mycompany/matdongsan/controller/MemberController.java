package com.mycompany.matdongsan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mycompany.matdongsan.dto.Member;
import com.mycompany.matdongsan.dto.UserEmail;
import com.mycompany.matdongsan.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService memberService;

	// 로그인

	// 회원가입
	// 참고 블로그
	// https://velog.io/@tjddnths0223/%ED%8C%81-RequestBody%EB%A1%9C-%EC%97%AC%EB%9F%AC-%EA%B0%9D%EC%B2%B4-%EB%B0%9B%EA%B8%B0

	@PostMapping("/Signup/MemberSignup")
	public UserEmail joinByMember(@RequestBody ObjectNode memberData) throws JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		Member member = mapper.treeToValue(memberData.get("member"), Member.class);
		UserEmail userEmail = mapper.treeToValue(memberData.get("userEmail"), UserEmail.class);
		
		// 비밀번호 암호화
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		userEmail.setUpassword(passwordEncoder.encode(userEmail.getUpassword()));

		// 아이디 활성화
		userEmail.setUremoved(false);

		// 권한 설정

		memberService.joinUserByMember(userEmail);
		memberService.joinByMember(member);
		
		userEmail.setUpassword(null);
		return userEmail;
	}

	// 탈퇴

	// 비밀번호 수정

	//
}
