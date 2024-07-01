package com.mycompany.matdongsan.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.matdongsan.dao.QuestionDao;
import com.mycompany.matdongsan.dto.Answer;
import com.mycompany.matdongsan.dto.Notice;
import com.mycompany.matdongsan.dto.Pager;
import com.mycompany.matdongsan.dto.Question;
import com.mycompany.matdongsan.service.PagerService;
import com.mycompany.matdongsan.service.QnaService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@Slf4j
@RequestMapping("/Qna")
public class QnaController {
	@Autowired 
	private QnaService qnaService;
	
	@Autowired
	private PagerService pagerService;
	
	// Question
	// 고객 문의 생성
	@PostMapping("/CustomerInquiryForm")
	public Question createQuestion(Question question) {
		if(question.getQattach() != null && !question.getQattach().isEmpty()) {
			log.info("첨부 파일 있음");
			
			MultipartFile mf = question.getQattach();
			
			question.setQattachoname(mf.getOriginalFilename());
			question.setQattachtype(mf.getContentType());
			try {
				question.setQattachdata(mf.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
						
		} else {
			log.info("첨부 파일 없음");
		}
		
//		question.setQUnumber(authentication.getName());
		question.setQUnumber(17);// 프론트 연결하면 토큰에서 받아온 정보로 setting 해야 한다.
		log.info(question.toString()); // postman으로 볼 수 없는 multipartfile이나 byte배열을 확인 
		
		qnaService.insertQuestion(question);
		
		// json 반환할 때 json으로 변환할 수 없는 multipartfile이나 byte배열같은 타입은 빼야 한다.
		question.setQattach(null);
		question.setQattachdata(null);
		return question;
	}
	
	// 문의 사항 읽기 
	@GetMapping("/MyCustomerInquiry")
	public Question readQuestion(int qnumber) {
		Question question = qnaService.getQuestion(qnumber);
		return question;
	}
	
	// 문의 사항 수정
	@PutMapping("/MyCustomerInquiryUpdate")
	public Question updateQuestion(Question question) {
		if(question.getQattach() != null && !question.getQattach().isEmpty()) {
			log.info("첨부 파일 있음");
			
			MultipartFile mf = question.getQattach();
			
			question.setQattachoname(mf.getOriginalFilename());
			question.setQattachtype(mf.getContentType());
			try {
				question.setQattachdata(mf.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
						
		} else {
			log.info("첨부 파일 없음");
		}
		
		qnaService.updateQuestion(question);

		// 수정 후 변경 된 문의사항 가져오기
		question = qnaService.getQuestion(question.getQnumber());
		
		return question;
	}
	
	// 문의 사항 삭제
	@DeleteMapping("/MyCustomerInquiryDelete")
	public int deleteQuestion(int qnumber) {
		int result = qnaService.deleteQuestionByQnumber(qnumber); // 정상적으로 삭제가 되면 1을 반환, 없는 게시물을 삭제하려고 하면 0을 반환
		log.info("결과 값", result);
		return result;
	}
	
	// 문의 사항 리스트 가져오기(특정 회원이 문의한 문의사항-마이페이지에서 사용할 예정 -> pager...필요 없지 않ㄴ나....(물어보고 수정)
	@GetMapping("/MyCustomerInquiryList")
	public Map<String, Object> customerQuestionList(@RequestParam(defaultValue = "1") String pageNo, int qUnumber, HttpSession session){
		// 해당하는 회원이 작성한 공지사항 갯수
		int totalRows = qnaService.getQuestionCountByUnumber(qUnumber);
		
		// 페이저 객체 생성(페이지 당 행 수, 그룹 당 페이지 수, 전체 행 수, 현재 페이지 번호)
		Pager pager= pagerService.preparePager(session, pageNo, totalRows, 5, 5, "myCustomerInquiry");
		
		
		// 해당하는 유저의 페이지 불러오기 위한 Map 생성 (Mybatis는 parameter을 하나의 타입만 보낼 수 있어서 map에 담아서 매개변수를 넣는다.)
		Map<String, Object> usersQuestion = new HashMap<>();
		usersQuestion.put("pager", pager);
		usersQuestion.put("qUnumber", qUnumber);
		
		// 해당 페이지의 게시물 목록 가져오기
		List<Question> list = qnaService.getUsersQuestionList(usersQuestion);
		
		// 여러 객체를 리턴하기 위해 Map 객체 생성		
		Map<String, Object> map = new HashMap<>();
		
		// map에 데이터 넣기
		map.put("question", list);
		map.put("pager", pager);
		
		return map; // return 값은 JSON으로 변환되어 응답 본문에 들어간다. {"pager":{}, "notice":[]};
	}
	
	// 문의 사항 리스트 가져오기(전체 문의 사항 - 마이페이지)
	@GetMapping("/CustomerInquiryList")
	public Map<String, Object> CustomerInquiryList(@RequestParam(defaultValue = "1") String pageNo, HttpSession session){
		// 공지사항 갯수
		int totalRows = qnaService.getQuestionCount(); 
		
		// 페이저 객체 생성(페이지 당 행 수, 그룹 당 페이지 수, 전체 행 수, 현재 페이지 번호)
		Pager pager= pagerService.preparePager(session, pageNo, totalRows, 10, 5, "customerInquiry");
		
		// 해당 페이지의 게시물 목록 가져오기
		List<Question> list = qnaService.getQuestionList(pager);
		
		// 여러 객체를 리턴하기 위해 Map 객체 생성		
		Map<String, Object> map = new HashMap<>();
		
		// map에 데이터 넣기
		map.put("question", list);
		map.put("pager", pager);
		
		return map; // return 값은 JSON으로 변환되어 응답 본문에 들어간다. {"pager":{}, "notice":[]};
	}
	
	
	
//	-------------------------------------------------------------------------------------------------------------------------------------
	// Answer 문의사항 답변
	
	// 문의 답변 생성
	@PostMapping("/AdminInquiryAnswer")
	public Answer createAnswer(Answer answer) {
		qnaService.insertAnswer(answer);
		return answer;
	}
	
	// 문의 답변 수정
	@PutMapping("/AdminInquiryAnswer")
	public Answer updateAnswer(Answer answer) {
		qnaService.updateAnswer(answer);
		// 읽기 메소드 만들어서 수정한거 DB에서 새로 불러오기
		return answer;
	}
	
	// 문의 답변 읽기
	@GetMapping("/AdminInquiryAnswerDetail")
	public Answer readAnswer(int anumber) {
		return qnaService.getAnswer(anumber);
	}
	
	// 문의 답변 삭제
	@DeleteMapping("/AdminInquiryAnswerDelete")
	public int deleteAnswer(int anumber) {
		return qnaService.deleteAnswer(anumber);
	}
	
//	-------------------------------------------------------------------------------------------------------------------------------------
	// Notice 공지 사항
	
	// 공지 사항 생성
	@PostMapping("/NoticeForm")
	public Notice createNotice(Notice notice) {
		qnaService.insertNotice(notice);
		return notice;
	}
	
	// 공지 사항 읽기
	@GetMapping("/NoticeDetail")
	public Notice getNotice(int nnumber) {
		return qnaService.getNoticeDetail(nnumber);
	}
	
	// 공지 사항 수정
	@PutMapping("NoticeForm")
	public Notice updateNotice(Notice notice) {
		
		// 공지 사항 수정
		qnaService.updateNotice(notice);
		
		// 수정 후 수정 된 공지사항 가져오기
		notice =  qnaService.getNoticeDetail(notice.getNnumber());
		
		return notice;
	}
	
	// 공지 사항 삭제
	@DeleteMapping("/NoticeDetailDelete")
	public int deleteNotice(int nnumber) {
		return qnaService.deleteNotice(nnumber);
	}
		
	// 공지 사항 검색 및 정렬
	@GetMapping("/NoticeList")
	public Map<String, Object> searchNoticeList(@RequestParam(defaultValue = "1") String pageNo
			, @RequestParam(required = false) String searchKeyword
			, @RequestParam(required = false) String sort, HttpSession session)
		{
		// 검색 된 공지사항 갯수
		int totalNoticeRows;
		
		// 검색어가 없으면 전체 공지사항의 갯수를 반환하고 아니면 검색된 공지사항의 갯수를 반환
		if(searchKeyword == "") {
			totalNoticeRows = qnaService.getCountNotice();
		}else {
			totalNoticeRows = qnaService.getCountOfSearchedNotices(searchKeyword);
		}
		
		// 페이저 객체 생성(페이지 당 행 수, 그룹 당 페이지 수, 전체 행 수, 현재 페이지 번호)
		Pager pager= pagerService.preparePager(session, pageNo, totalNoticeRows, 10, 5, "Notice");
		
		// 검색된 페이지 불러오기 위한 Map 생성 (Mybatis는 parameter을 하나의 타입만 보낼 수 있어서 map에 담아서 매개변수를 넣는다.)
		Map<String, Object> mapForSearch = new HashMap<>();
		mapForSearch.put("pager", pager);
		mapForSearch.put("keyword", searchKeyword);
		mapForSearch.put("sort", sort);
		
		// 해당 페이지의 게시물 목록 가져오기
		List<Notice> list = qnaService.getSearchedNoticeList(mapForSearch);
		
		// 여러 객체를 리턴하기 위해 Map 객체 생성		
		Map<String, Object> map = new HashMap<>();
		
		// map에 데이터 넣기
		map.put("notice", list);
		map.put("pager", pager);
		
		return map; // return 값은 JSON으로 변환되어 응답 본문에 들어간다. {"pager":{}, "notice":[]};
	}
	
	
}
