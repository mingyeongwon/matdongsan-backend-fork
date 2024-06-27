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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.matdongsan.dto.UserEmail;
import com.mycompany.matdongsan.security.AppUserDetails;
import com.mycompany.matdongsan.security.AppUserDetailsService;
import com.mycompany.matdongsan.security.JwtProvider;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HomeController {
	
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
		} else {
			map.put("result", "fail");
		}
		return map;

	}
	
	@PutMapping("/MyPage/DeleteAccount")
	public UserEmail activateAccount() {
		UserEmail userEmail = new UserEmail();
		return userEmail;
	}
}