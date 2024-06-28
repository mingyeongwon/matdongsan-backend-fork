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
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.matdongsan.dto.PropertyListing;
import com.mycompany.matdongsan.security.AppUserDetails;
import com.mycompany.matdongsan.security.AppUserDetailsService;
import com.mycompany.matdongsan.security.JwtProvider;
import com.mycompany.matdongsan.service.AgentService;
import com.mycompany.matdongsan.service.MemberService;
import com.mycompany.matdongsan.service.PropertyService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HomeController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private PropertyService propertyService;
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
			map.put("userEmail", uemail);
			map.put("accessToken", accessToken);

		} else if (checkActivation) { // 비활성화(삭제된) 유저의 경우 removed라고 map에 값을 넣음
			map.put("result", "removed");

		} else { // 로그인에 실패한 경우(비밀번호, 아이디 문제) fail 표시
			map.put("result", "fail");
		}
		return map;
	}

	// 탈퇴
	@PutMapping("/MyPage/DeleteAccount")
	public void activateAccount(Authentication authentication) {
		boolean isDeactivate = true;
		memberService.getUserRole(authentication.getName(), isDeactivate);
	}

	// 등록권 구매
	@PostMapping("/Payment/PaymentResult/{quantity}")
	public boolean purchasePropertyListing(Authentication authentication, @PathVariable int quantity) {
		String userName = authentication.getName();
		// 유저 번호
		int userNumber = agentService.getUserIdByUserName(userName);
		boolean hasPropertyListing = propertyService.checkPropertyCondition(userNumber); //유저정보가 테이블에 이미 존재하는지 확인함 있다면 등록권 수량 체크함
		log.info(hasPropertyListing+"");
		if(!hasPropertyListing) {
			int price = 5500;
			PropertyListing propertyListing = new PropertyListing();
			propertyListing.setPlquantity(quantity);
			propertyListing.setPlUnumber(userNumber);
			if (quantity > 1) {
				price = quantity * 5500 - (500 * quantity);
				propertyListing.setPlprice(price);
			} else {
				propertyListing.setPlprice(price);
			}
			log.info(propertyListing.toString());
			propertyService.purchasePropertyListing(propertyListing);
			return true; //등록권이 없는 유저나 처음 구매하는 유저라면 true
		} else {
			log.info("이미 등록권이 존재합니다.");
			return false; //아직 등록권이 존재하는 유저라면 false를 리턴
		}
		

	}
	
}