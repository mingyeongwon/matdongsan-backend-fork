package com.mycompany.matdongsan.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.matdongsan.dto.Member;
import com.mycompany.matdongsan.dto.UserCommonData;
import com.mycompany.matdongsan.security.AppUserDetails;
import com.mycompany.matdongsan.security.AppUserDetailsService;
import com.mycompany.matdongsan.security.JwtProvider;
import com.mycompany.matdongsan.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private AppUserDetailsService userDetailsService;
	// 로그인

	// 회원가입
	// 참고 블로그
	// https://velog.io/@tjddnths0223/%ED%8C%81-RequestBody%EB%A1%9C-%EC%97%AC%EB%9F%AC-%EA%B0%9D%EC%B2%B4-%EB%B0%9B%EA%B8%B0
	@Transactional
	@PostMapping("/Signup/MemberSignup")
	public UserCommonData joinByMember(
		    @RequestParam("uemail") String uemail,
		    @RequestParam("urole") String urole,
		    @RequestParam("upassword") String upassword,
		    @RequestParam("mname") String mname,
		    @RequestParam("mphone") String mphone,
			@RequestParam(value = "mprofile", required = false) MultipartFile mprofile) throws IOException {

		UserCommonData userEmail = new UserCommonData();
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

	@PostMapping("/login")
	public Map<String, String> userLogin(String uemail, String upassword) {
		// 사용자 상세 정보 얻기
		AppUserDetails userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(uemail);
		// 비밀번호 체크
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		boolean checkResult = passwordEncoder.matches(upassword, userDetails.getUser().getUpassword());
		//비활성화 되었는지 확인해야함
		boolean checkActivation = userDetails.isEnabled();
		
		// Spring security 인증 처리
		if (checkResult && !checkActivation) {
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
					userDetails.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		// 응답 생성 비밀번호 일치 && 계정 활성화 확인
		Map<String, String> map = new HashMap<>();
		if (checkResult && !checkActivation) {
			// AccessToken을 생성
			String accessToken = jwtProvider.createAccessToken(uemail, userDetails.getUser().getUrole());
			// JSON 응답
			map.put("result", "success");
			map.put("userEmail", uemail);
			map.put("accessToken", accessToken);
			
		} else if(checkActivation){ // 비활성화(삭제된) 유저의 경우 removed라고 map에 값을 넣음
			map.put("result", "removed");

		} else { //로그인에 실패한 경우(비밀번호, 아이디 문제) fail 표시
			map.put("result", "fail");
		}
		return map;
	}
	// 탈퇴
	@PutMapping("/MyPage/DeleteAccount")
	public String activateAccount(Authentication authentication) {
		String role = memberService.getUserRole(authentication.getName());
		
		return role;
	}


	// 비밀번호 수정

	//
}
