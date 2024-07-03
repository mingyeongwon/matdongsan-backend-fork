package com.mycompany.matdongsan.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.matdongsan.dto.UserCommonData;
import com.mycompany.matdongsan.security.AppUserDetails;
import com.mycompany.matdongsan.security.AppUserDetailsService;
import com.mycompany.matdongsan.security.JwtProvider;
import com.mycompany.matdongsan.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HomeController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private AppUserDetailsService userDetailsService;

	@GetMapping("/")
	public String home() {
		log.info("실행");
		return "restapi";
	}

	@PostMapping("/login")
	public Map<String, String> userLogin(String uemail, String upassword) {
		// 사용자 상세 정보 얻기
		AppUserDetails userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(uemail);
		// 비밀번호 체크
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		boolean checkResult = passwordEncoder.matches(upassword, userDetails.getUser().getUpassword());
		// 비활성화 되었는지 확인해야함
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
			map.put("uemail", uemail);
			map.put("accessToken", accessToken);

		} else if (checkActivation) { // 비활성화(삭제된) 유저의 경우 removed라고 map에 값을 넣음
			map.put("result", "removed");

		} else { // 로그인에 실패한 경우(비밀번호, 아이디 문제) fail 표시
			map.put("result", "fail");
		}
		log.info(map+"");
		return map;
	}
	
	// 탈퇴
	@PutMapping("/MyPage/DeleteAccount")
	public void activateAccount(@RequestBody Map<String, String> payload, Authentication authentication) {
		String currPw = payload.get("currPw");
		log.info("탈퇴 currPw : " + currPw);
		String uemail = authentication.getName();
		log.info("탈퇴 uemail : " + uemail);
		UserCommonData user = memberService.getUserDataFullyByUemail(uemail);
		boolean isDeactivate = true; // 비활성화 여부
		
		// 비밀번호 일치 여부
		if(memberService.checkPassword(currPw, user.getUpassword())) {
			memberService.deleteAccount(uemail, isDeactivate);
		}
	}

	//유저정보 불러오기
	@GetMapping("/Mypage/MyInfomation/{uemail}")
	public UserCommonData getUserDataByUemail(@PathVariable String uemail) {
		log.info(uemail);
		Map<String,Object> map = new HashMap<>();
		UserCommonData userData = memberService.getUserDataByUemail(uemail);
		map.put("userData",userData);
		return userData;
	}

	
}