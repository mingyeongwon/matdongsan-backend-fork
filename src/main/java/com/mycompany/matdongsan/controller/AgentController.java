package com.mycompany.matdongsan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.matdongsan.dto.Agent;
import com.mycompany.matdongsan.service.AgentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AgentController {

	@Autowired
	private AgentService agentService;

	// 부동산 정보 리스트 출력
	@GetMapping("/Agent")
	public String GetAgentList() {
		log.info("실행");
		return null;
	}

	// 부동산 등록
	@PostMapping("/Signup/AgentSignup")
	public Agent createAgentAccount(Agent agent, Authentication authentication) {

		return null;
	}

	// 매물 상세
	@GetMapping("/Agent/{aid}")
	public Agent readAgentInfo(@PathVariable int aid) {
		// Agent agent = agentService.getAgentData(aid);
		// agent.setAattachdata(null);

		// 부동산 후기

		return null;
	}
	// 부동산 검색
	@GetMapping("/Agent/{aid}/{sort}")
	public Agent readAgentInfo(@PathVariable int aid,@PathVariable String sort) {
		// Agent agent = agentService.getAgentData(aid);
		// agent.setAattachdata(null);

		return null;
	}
	// 마이페이지 부동산 중개업자 정보 수정
	@PreAuthorize("hasAuthority('ROLE_AGENT')") // 유저일 경우에만 등록 가능
	@GetMapping("/Mypage/MyInfomation")
	public Agent getMypagePropertyInfoList(@PathVariable int aid) {

		return null;
	}

	// 부동산 수정
	@PreAuthorize("hasAuthority('ROLE_USER')") // 유저일 경우에만 등록 가능
	@PutMapping("/Mypage/ManageMyProperty/{aid}")
	public Agent updateMypagePropertyInfo(@PathVariable int aid) {
		// 매물 정보 수정

		// 매물 비활성화

		// 매물 거래완료
		return null;
	}

	// 검색
	@GetMapping("/Agent/{keyword}")
	public Agent getAgentByKeyword(@PathVariable String keyword) {
		return null;
	}
	// 댓글 정렬
}
