package com.mycompany.matdongsan.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	@Transactional
	@PostMapping("/Signup/MemberSignup")
	public UserEmail joinByMember(
		    @RequestParam("uemail") String uemail,
		    @RequestParam("urole") String urole,
		    @RequestParam("upassword") String upassword,
		    @RequestParam("mname") String mname,
		    @RequestParam("mphone") String mphone,
			@RequestParam(value = "mprofile", required = false) MultipartFile mprofile) throws IOException {

		UserEmail userEmail = new UserEmail();
	    userEmail.setUemail(uemail);
	    userEmail.setUrole(urole);
	    userEmail.setUpassword(upassword);
	    userEmail.setUremoved(false);		
		
	    // 비밀번호 암호화
	    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	    userEmail.setUpassword(passwordEncoder.encode(userEmail.getUpassword()));

	    // 권한 설정

	    
	    Member member = new Member();
	    member.setMname(mname);
	    member.setMphone(mphone);
	    
	    memberService.joinUserByMember(userEmail);
	    member.setMUnumber(userEmail.getUnumber()); // FK 값 주기

	    // 프로필 사진 첨부 여부
	    if (mprofile != null && !mprofile.isEmpty()) {
	        member.setMprofileoname(mprofile.getOriginalFilename());
	        member.setMprofiletype(mprofile.getContentType());
	        member.setMprofiledata(mprofile.getBytes());
	    }
	    memberService.joinByMember(member);

	    // JSON으로 변환되지 않는 필드는 null 처리
	    userEmail.setUpassword(null);
	    member.setMprofile(null);
	    member.setMprofiledata(null);

	    return userEmail;
	}

	// 탈퇴

	// 비밀번호 수정

	//
}
