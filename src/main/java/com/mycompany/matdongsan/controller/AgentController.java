package com.mycompany.matdongsan.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.mycompany.matdongsan.dto.Member;
import com.mycompany.matdongsan.dto.Pager;
import com.mycompany.matdongsan.dto.Property;
import com.mycompany.matdongsan.dto.UserCommonData;
import com.mycompany.matdongsan.service.AgentService;
import com.mycompany.matdongsan.service.MemberService;
import com.mycompany.matdongsan.service.PagerService;
import com.mycompany.matdongsan.service.PropertyService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AgentController {

	@Autowired
	private AgentService agentService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private PagerService pagerService;
	@Autowired
	private PropertyService propertyService;

	// 부동산 정보 리스트 출력
	@GetMapping("/Agent")
	public Map<String, Object> GetAgentList(@RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String keyword,
			@RequestParam(required = false) String byRate, @RequestParam(required = false) String byComment,
			@RequestParam(required = false) String byDate) {
		log.info("Received filters: byRate={}, byComment={}, byDate={}", byRate, byComment, byDate); // 로그 출력 수정
		// 무한 스크롤
		int totalAgentRows = agentService.getAllAgentCount();
		Pager pager = new Pager(size, pageNo, totalAgentRows);
		// 검색 내용 찾기
		// 부동산 이름, 대표 이름, 주소명
		// 키워드 유무 확인
		log.info(pager.getStartRowIndex() + "");
		List<Agent> list = new ArrayList<>();

			list = agentService.getAgentList(pager.getStartRowIndex(), pager.getRowsPerPage(), keyword,byRate,byComment,byDate);

			//list = agentService.getAgentList(pager.getStartRowIndex(), pager.getRowsPerPage());


		Map<String, Object> map = new HashMap<>();
		map.put("agent", list);
		map.put("pager", pager);

		return map;
	}

	// 중개인별 리뷰 개수와 별점 총합 가져오기
	@GetMapping("/Agent/AgentReview/{anumber}")
	public Map<String, String> getAgentReview(@PathVariable int anumber) {
		Map<String, String> map = new HashMap<>();
		String reviewRateAvg = agentService.getReviewAvgByanumber(anumber);
		String totalReviewsByAgent = agentService.getReviewCountByAnumber(anumber);
		map.put("sum", reviewRateAvg);
		map.put("total", totalReviewsByAgent);
		return map;
	}

	// 중개인별 매물 데이터 가져오기
	@GetMapping("/Agent/Property/{anumber}")
	public Map<String, Object> getAgentPropertyList(@RequestParam(defaultValue = "1", required = false) String pageNo,
			@PathVariable int anumber, HttpSession session) {
		log.info("중개인 매물 실행");
		// 유저번호
		int unumber = agentService.getUserNumberByAnumber(anumber);
		log.info(unumber + "이게 anumber로받은 번호");

		int totalUserPropertyRows = propertyService.getAllUserPropertyCount(unumber);
		Pager pager = pagerService.preparePager(session, pageNo, totalUserPropertyRows, 9, 5, "agentPropertyList");
		List<Property> userPropertyList = propertyService.getAllUserPropertyList(unumber, pager);
		log.info(userPropertyList.toString());
		Map<String, Object> map = new HashMap<>();
		map.put("pager", pager);
		map.put("agentProperty", userPropertyList);
		return map;
	}

	// 부동산 등록
	// 리턴값과 파라미터 값으로 agent와 agentDetail이 합쳐진 dto를 받아야함
	// agent관련 DTO를 만들어서 코드 바꿀것
	@Transactional
	@PostMapping("/Signup/AgentSignup")
	public void createAgentAccount(@ModelAttribute AgentSignupData agentSignupData) throws IOException {
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
//		return agentSignupData;
	}

	// 부동산 상세 정보 조회
	// 댓글 정렬기능을 위한 sort 파라미터
	@GetMapping("/Agent/{anumber}")
	public Map<String, Object> readAgentInfoByNumber(@PathVariable int anumber,
			@RequestParam(defaultValue = "1", required = false) String pageNo,
			@RequestParam(defaultValue = "desc", required = false) String sort, HttpSession session) {
		log.info("페이지넘버입니다.: " + pageNo);
		// 중개인 정보
		Agent agent = agentService.getAgentDataByUserNumber(anumber);
		// 중개인 상세정보
		AgentDetail agentDetail = agentService.getAgentDetailByAgentNumber(anumber);
		// 중개인 리뷰정보
		int totalRows = agentService.getTotalReviews(anumber);
		Pager pager = pagerService.preparePager(session, pageNo, totalRows, 5, 5, "agentReview");
		List<AgentReview> agentReviewList = agentService.getAgentReviewListByAnumber(anumber, sort, pager);

		for (AgentReview agentReview : agentReviewList) {
			String memberName = memberService.getUserEmailByMemberNumber(agentReview.getArMnumber());
			agentReview.setMembername(memberName);
		}
		log.info("페이지네이션으로 호출됨");
		log.info(agentReviewList.toString());
		Map<String, Object> map = new HashMap<>();
		map.put("agent", agent);
		map.put("agentDetail", agentDetail);
		map.put("agentReviewList", agentReviewList);
		map.put("pager", pager);
		return map;
	}

	// 마이페이지 부동산 중개업자 정보 불러오기
	// @PreAuthorize("hasAuthority('ROLE_USER')") // 중개인일 경우에만 등록 가능
	@GetMapping("/Mypage/MyInfomation")
	public Map<String, Object> getMypagePropertyInfo(Authentication autentication) {
		String userName = autentication.getName();
		log.info("username이다: " + userName);
		// 로그인한 중개인의 데이터 수정
		// 아이디로 중개인의 정보 가져옴
		int userNumber = agentService.getUserIdByUserName(userName);
		String userRole = memberService.getUserRole(userName);
		Map<String, Object> map = new HashMap<>();
		map.put("userRole", userRole);
		// 일반 유저일 경우
		if (userRole.equals("MEMBER")) {
			Member member = memberService.getMemberDataFullyByUserNumber(userNumber);
			map.put("member", member);

		} else {
			// 중개인일 경우
			AgentSignupData agentSignupData = agentService.getAgentDataFullyByUserNumber(userNumber);
			map.put("agentSignupData", agentSignupData);
		}
		int propertyListing = propertyService.getUserPropertyListingQuantity(userNumber);
		map.put("propertyListing", propertyListing);
		return map;

	}

	// 중개업자 정보 업데이트
	@Transactional
	@PutMapping("/Agent/Mypage/MyInfomation")
	public void updateMypagePropertyInfo(@ModelAttribute AgentSignupData agentSignupData,
			Authentication authentication) {
		Agent agent = agentSignupData.getAgent();
		AgentDetail agentDetail = agentSignupData.getAgentDetail();
		// 프로필사진 & 등록증 사진
		if (agent.getAprofile() != null) {
			MultipartFile agentProfile = agent.getAprofile();

			// 파일 이름을 설정
			agent.setAprofileoname(agentProfile.getOriginalFilename());
			// 파일 종류를 설정
			agent.setAprofiletype(agentProfile.getContentType());
			agentDetail.setAdattachtype(agentDetail.getAdattachtype());
			try {
				// 파일 데이터를 설정
				agent.setAprofiledata(agentProfile.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		int userNum = agentService.getUserIdByUserName(authentication.getName()); // 유저 번호
		int Anumber = agentService.getAgentNumberByUserNumber(userNum); // 중개인 번호
		agent.setAnumber(Anumber);
		agentDetail.setAdAnumber(Anumber);
		// 수정하기
		log.info(agentDetail.toString());
		agentService.updateAgentData(agent, agentDetail);
	}

	// 중개인 리뷰

	// 댓글 생성
	@PostMapping("/Agent/{anumber}")
	public int createAgentReview(@PathVariable int anumber, @ModelAttribute AgentReview agentReview,
			Authentication authentication) {
		log.info(authentication.getName());
		log.info("readAgentInfoByNumber 실행, anumber={}", anumber);
		agentReview.setArAnumber(anumber);
		int memberNum = memberService.getMemberNumberByMemberEmail(authentication.getName());
		agentReview.setArMnumber(memberNum);
		log.info(agentReview.toString());
		String role = memberService.getUserRole(authentication.getName());

		if (role.equals("MEMBER")) {
			agentService.createAgentReview(agentReview);
			return 1; // 멤버인 경우 리턴 1과 서비스 실행
		} else {
			return 0; // role이 멤버가 아닌경우 리턴 0
		}

	}

	// 댓글 수정
	@PutMapping("/Agent/{anumber}/{arnumber}")
	public void updateAgentReview(@PathVariable int anumber, @PathVariable int arnumber,
			@ModelAttribute AgentReview agentReview, Authentication authentication) {
		String userEmail = authentication.getName();
		int userNumber = memberService.getMemberNumberByMemberEmail(userEmail);
		agentReview.setArAnumber(anumber);
		agentReview.setArnumber(arnumber);
		agentReview.setArMnumber(userNumber);
		agentService.updateAgentReview(agentReview);
	}

	// 댓글 삭제
	@DeleteMapping("/Agent/{anumber}/{arnumber}")
	public void deleteAgentReview(@PathVariable int anumber, @PathVariable int arnumber,
			Authentication authentication) {
		log.info("댓글 삭제실행");
		String userEmail = authentication.getName();
		int userNumber = memberService.getMemberNumberByMemberEmail(userEmail);
		// 리뷰글번호,중개인번호,유저번호로 글 삭제
		agentService.deleteAgentReview(anumber, arnumber, userNumber);
	}

	// 중개인 프로파일 다운로드
//	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/aattach/{anumber}")
	public void downloadAgentProfile(@PathVariable int anumber, HttpServletResponse response) {
		// 해당 게시물 가져오기
		Agent agent = agentService.getAgentDataByUserNumber(anumber);
		// 파일 이름이 한글일 경우, 브라우저에서 한글 이름으로 다운로드 받기 위한 코드
		try {
			String fileName = new String(agent.getAprofileoname().getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			// 파일 타입을 헤더에 추가
			response.setContentType(agent.getAprofiletype());
			// 응답 바디에 파일 데이터를 출력
			OutputStream os = response.getOutputStream();
			os.write(agent.getAprofiledata());
			os.flush();
			os.close();
		} catch (IOException e) {
			log.error(e.toString());
		}

	}
}
