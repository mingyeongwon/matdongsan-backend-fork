package com.mycompany.matdongsan.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.matdongsan.dto.Agent;
import com.mycompany.matdongsan.dto.AgentDetail;
import com.mycompany.matdongsan.service.AgentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AgentController {

	@Autowired
	private AgentService agentService;
	// 부동산 정보 리스트 출력
	@GetMapping("/Agent")
	public Map<String,Object> GetAgentList(@RequestParam(defaultValue = "1") int pageNo,@RequestParam(defaultValue = "agnet") String page) {
		log.info("실행");
		// 무한 스크롤
		List<Agent> list = agentService.getAgentList();
		
		Map<String,Object> map = new HashMap<>();
		map.put("agent", list);
		//무한 스크롤을 리턴값으로 넘겨야함
		return null;
	}

	// 부동산 등록
	// 리턴값과 파라미터 값으로 agent와 agentDetail이 합쳐진 dto를 받아야함
	@PostMapping("/Signup/AgentSignup")
	public AgentDetail createAgentAccount(AgentDetail agentDetail,Agent agent, Authentication authentication) {
		//첨부가 넘어왔을 경우 처리
		if (agentDetail.getAdattach() != null && !agentDetail.getAdattach().isEmpty()) {
			MultipartFile mf = agentDetail.getAdattach();
			// 파일 이름을 설정
			agentDetail.setAdattachoname(mf.getOriginalFilename());
			// 파일 종류를 설정
			agentDetail.setAdattachtype(mf.getContentType());
			try {
				// 파일 데이터를 설정
				agentDetail.setAdattachdata(mf.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// DB에 저장
		agent.setAemail(authentication.getName());
		agentService.insertAgentData(agent,agentDetail);
		// JSON으로 변환되지 않는 필드(attach,attach data등)는 null 처리
		agentDetail.setAdattach(null);
		agentDetail.setAdattachdata(null);

		return agentDetail;
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
	@PreAuthorize("hasAuthority('ROLE_USER')") // 중개인일 경우에만 등록 가능
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
