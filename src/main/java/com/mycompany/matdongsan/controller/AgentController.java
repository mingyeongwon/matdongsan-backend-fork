package com.mycompany.matdongsan.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.matdongsan.dto.Agent;
import com.mycompany.matdongsan.dto.AgentDetail;
import com.mycompany.matdongsan.dto.Pager;
import com.mycompany.matdongsan.dto.UserEmail;
import com.mycompany.matdongsan.service.AgentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AgentController {

	@Autowired
	private AgentService agentService;

	// 부동산 정보 리스트 출력
	@GetMapping("/Agent")
	public Map<String, Object> GetAgentList(@RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String keyword) {

	
		// 무한 스크롤
		int totalAgentRows = agentService.getAllAgentCount();
		Pager pager = new Pager(size, pageNo, totalAgentRows);
		
		//검색 내용 찾기
		// 부동산 이름, 대표 이름, 주소명
		List<Agent> list;
		if(keyword != null) {
			log.info("키워드입니다. " + keyword);
			list = agentService.getAgentList(pager.getStartRowIndex(), pager.getRowsPerPage(),keyword);
		} else {
			list = agentService.getAgentList(pager.getStartRowIndex(), pager.getRowsPerPage());
		}
		
	

		Map<String, Object> map = new HashMap<>();
		map.put("agent", list);
		map.put("pager", pager);
		
		return map;
	}

	// 부동산 등록
	// 리턴값과 파라미터 값으로 agent와 agentDetail이 합쳐진 dto를 받아야함
	// agent관련 DTO를 만들어서 코드 바꿀것
	@Transactional
	@PostMapping("/Signup/AgentSignup")
	public AgentDetail createAgentAccount(@RequestParam("abrand") String abrand, @RequestParam("aphone") String aphone,
			@RequestParam("aaddress") String aaddress, @RequestParam("apostcode") String apostcode,
			@RequestParam("alatitude") String alatitude, @RequestParam("alongitude") String alongitude,
			@RequestParam("addressdetail") String addressdetail, @RequestParam("adname") String adname,
			@RequestParam("adbrandnumber") String adbrandnumber,
			@RequestParam(value = "adattach", required = false) MultipartFile adattach,
			@RequestParam("uemail") String uemail, @RequestParam("urole") String urole,
			@RequestParam("upassword") String upassword, @RequestParam("uremoved") boolean uremoved)
			throws IOException {
		// 객체 생성 및 데이터 설정
		Agent agent = new Agent();
		agent.setAbrand(abrand);
		agent.setAphone(aphone);
		agent.setAaddress(aaddress);
		agent.setApostcode(apostcode);
		agent.setAlatitude(alatitude);
		agent.setAlongitude(alongitude);
		agent.setAaddressdetail(addressdetail);

		AgentDetail agentDetail = new AgentDetail();
		agentDetail.setAdname(adname);
		agentDetail.setAdbrandnumber(adbrandnumber);

		UserEmail userEmail = new UserEmail();
		userEmail.setUemail(uemail);
		userEmail.setUrole(urole);
		userEmail.setUpassword(upassword);
		userEmail.setUremoved(uremoved);

		// 비밀번호 암호화
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		userEmail.setUpassword(passwordEncoder.encode(userEmail.getUpassword()));

		// 첨부 파일 처리
		if (adattach != null && !adattach.isEmpty()) {
			log.info("실행1234123");
			agentDetail.setAdattachoname(adattach.getOriginalFilename());
			agentDetail.setAdattachtype(adattach.getContentType());
			agentDetail.setAdattachdata(adattach.getBytes());
			log.info(agentDetail.getAdattachoname());
		}

		// 데이터베이스에 저장
		agentService.joinByUserEmail(userEmail);
		agent.setAUnumber(userEmail.getUnumber());
		agentService.joinByAgent(agent);
		agentDetail.setAdAnumber(agent.getAnumber());
		agentService.insertAgentData(agentDetail);
		userEmail.setUpassword(null);

		return agentDetail;
	}

	// 부동산 상세 정보 조회
	@GetMapping("/Agent/{anumber}")
	public Agent readAgentInfoByNumber(@PathVariable int anumber) {
		log.info("readAgentInfoByNumber 실행, anumber={}", anumber);
		// 실제 구현은 agentService를 통해 데이터를 가져와야 합니다.
		return null;// agentService.getAgentByNumber(anumber);
	}

	/*
	 * // 부동산 상세 정보 조회 with Sort
	 * 
	 * @GetMapping("/Agent/{anumber}") public Agent
	 * readAgentInfoByNumberWithSort(@PathVariable int anumber, @RequestParam String
	 * sort) { log.info("readAgentInfoByNumberWithSort 실행, anumber={}, sort={}",
	 * anumber, sort); // 실제 구현은 agentService를 통해 데이터를 가져와야 합니다. return
	 * null;//agentService.getAgentByNumberWithSort(anumber, sort); }
	 */

	// 마이페이지 부동산 중개업자 정보 수정
	@PreAuthorize("hasAuthority('ROLE_USER')") // 중개인일 경우에만 등록 가능
	@GetMapping("/Mypage/MyInfomation")
	public Agent getMypagePropertyInfoList(@PathVariable int aid) {

		return null;
	}

	// 검색
	@GetMapping("/Agent/search/{keyword}")
	public Agent getAgentByKeyword(@PathVariable String keyword) {
		return null;
	}
	// 댓글 정렬
}
