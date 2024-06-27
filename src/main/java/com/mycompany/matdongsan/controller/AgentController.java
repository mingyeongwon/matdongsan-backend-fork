package com.mycompany.matdongsan.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.matdongsan.dto.Agent;
import com.mycompany.matdongsan.dto.AgentDetail;
import com.mycompany.matdongsan.dto.AgentReview;
import com.mycompany.matdongsan.dto.AgentSignupData;
import com.mycompany.matdongsan.dto.Pager;
import com.mycompany.matdongsan.dto.UserCommonData;
import com.mycompany.matdongsan.service.AgentService;
import com.mycompany.matdongsan.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AgentController {

	@Autowired
	private AgentService agentService;
	@Autowired
	private MemberService memberService;

	// 부동산 정보 리스트 출력
	@GetMapping("/Agent")
	public Map<String, Object> GetAgentList(@RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String keyword) {

		// 무한 스크롤
		int totalAgentRows = agentService.getAllAgentCount();
		Pager pager = new Pager(size, pageNo, totalAgentRows);

		// 검색 내용 찾기
		// 부동산 이름, 대표 이름, 주소명
		// 키워드 유무 확인
		log.info(pager.getStartRowIndex() + "");
		List<Agent> list = new ArrayList<>();
		if (keyword != null) {
			list = agentService.getAgentList(pager.getStartRowIndex(), pager.getRowsPerPage(), keyword);
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
	public AgentSignupData createAgentAccount(@ModelAttribute AgentSignupData agentSignupData) throws IOException {
		// 객체 생성 및 데이터 설정
		Agent agent = agentSignupData.getAgent();
		AgentDetail agentDetail = agentSignupData.getAgentDetail();
		UserCommonData userEmail = agentSignupData.getUserEmail();

		// 비밀번호 암호화
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		userEmail.setUpassword(passwordEncoder.encode(userEmail.getUpassword()));

		MultipartFile profileImg = agent.getAprofile();

		// 중개인 프로필 사진 이미지 처리
		if (profileImg != null && !profileImg.isEmpty()) {
			agent.setAprofileoname(profileImg.getOriginalFilename());
			agent.setAprofiletype(profileImg.getContentType());
			agent.setAprofiledata(profileImg.getBytes());
			log.info(agent.getAprofileoname());
		}

		// 사업자 등록증 이미지
		// 사업자 등록증 첨부 파일 처리
		if (agentDetail.getAdattach() != null && !agentDetail.getAdattach().isEmpty()) {
			agentDetail.setAdattachoname(agentDetail.getAdattach().getOriginalFilename());
			agentDetail.setAdattachtype(agentDetail.getAdattach().getContentType());
			agentDetail.setAdattachdata(agentDetail.getAdattach().getBytes());
			log.info(agentDetail.getAdattachoname());
		}

		// 데이터베이스에 저장
		agentService.joinByUserEmail(userEmail);
		agent.setAUnumber(userEmail.getUnumber());
		agentService.joinByAgent(agent);
		agentDetail.setAdAnumber(agent.getAnumber());
		agentService.insertAgentData(agentDetail);
		// 출력시 데이터 부분은 출력 길이가 길어서 null로 처리
		userEmail.setUpassword(null);
		agentDetail.setAdattachdata(null);
		agent.setAprofiledata(null);
		return agentSignupData;
	}

	// 부동산 상세 정보 조회
	@GetMapping("/Agent/{anumber}")
	public Map<String, Object> readAgentInfoByNumber(@PathVariable int anumber) {

		log.info("readAgentInfoByNumber 실행, anumber={}", anumber);
		// 중개인 정보
		Agent agent = agentService.getAgentDataByUserNumber(anumber);
		// 중개인 상세정보
		AgentDetail agentDetail = agentService.getAgentDetailByAgentNumber(anumber);
		// 중개인 리뷰정보

		Map<String, Object> map = new HashMap<>();
		map.put("agent", agent);
		map.put("agentDetail", agentDetail);
		return map;
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

	// 마이페이지 부동산 중개업자 정보 불러오기
	// @PreAuthorize("hasAuthority('ROLE_USER')") // 중개인일 경우에만 등록 가능
	@GetMapping("/Mypage/MyInfomation")
	public AgentSignupData getMypagePropertyInfo(String userEmail
	// Authentication autentication
	) {
		// 로그인한 중개인의 데이터 수정
		// 아이디로 중개인의 정보 가져옴
		// int agentId = agentService.getUserIdByUserName(autentication.getUsername());
		int userNumber = agentService.getUserIdByUserName(userEmail);
		AgentSignupData agentSignupData = agentService.getAgentDataFullyByUserNumber(userNumber);
		return agentSignupData;
	}

	// 중개업자 정보 업데이트
	@PutMapping("/Mypage/MyInfomation")
	public void updateMypagePropertyInfo(@ModelAttribute AgentSignupData agentSignupData) {
		Agent agent = agentSignupData.getAgent();
		AgentDetail agentDetail = agentSignupData.getAgentDetail();
		UserCommonData userEmail = agentSignupData.getUserEmail();
		// 프로필사진 & 등록증 사진
		MultipartFile agentProfile = agent.getAprofile();
		MultipartFile agentDetailFile = agentDetail.getAdattach();

		// 파일 이름을 설정
		agent.setAprofileoname(agentProfile.getOriginalFilename());
		agentDetail.setAdattachoname(agentDetailFile.getOriginalFilename());
		// 파일 종류를 설정
		agent.setAprofiletype(agentProfile.getContentType());
		agentDetail.setAdattachtype(agentDetail.getAdattachtype());
		try {
			// 파일 데이터를 설정
			agent.setAprofiledata(agentProfile.getBytes());
			agentDetail.setAdattachdata(agentDetailFile.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int userNum = agentService.getUserIdByUserName(userEmail.getUemail()); // 유저 번호
		int Anumber = agentService.getAgentNumberByUserNumber(userNum); // 중개인 번호
		agent.setAnumber(Anumber);
		agentDetail.setAdAnumber(Anumber);
		// 수정하기
		agentService.updateAgentData(agent, agentDetail);
	}

	// 중개인 리뷰

	// 댓글 생성
	@PostMapping("/Agent/{anumber}")
	public Agent createAgentReview(@PathVariable int anumber, @ModelAttribute AgentReview agentReview,
			Authentication authentication) {
		log.info(authentication.getName());
		log.info("readAgentInfoByNumber 실행, anumber={}", anumber);
		agentReview.setArAnumber(anumber);
		int memberNum = memberService.getMemberNumberByMemberEmail(authentication.getName());
		agentReview.setArMnumber(memberNum);
		log.info(agentReview.toString());
		agentService.createAgentReview(agentReview);
		return null;
	}

	// 대댓글 생성

	// 댓글 삭제

	// 대댓글 삭제

}
