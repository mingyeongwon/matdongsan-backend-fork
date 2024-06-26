package com.mycompany.matdongsan.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.mycompany.matdongsan.dto.Notice;
import com.mycompany.matdongsan.dto.Pager;
import com.mycompany.matdongsan.dto.Question;
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
	private QuestionDao questionDao;
	
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
		question.setQUnumber(8);
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
	
//	-------------------------------------------------------------------------------------------------------------------------------------
	// 공지 사항
	
	// 공지 사항 생성
	@PostMapping("/NoticeForm")
	public Notice createNotice(Notice notice) {
		qnaService.insertNotice(notice);
		return notice;
	}
	
	// 공지 사항 리스트 가져오기
	@GetMapping("/NoticeList")
	public Map<String, Object> noticeList(@RequestParam(defaultValue = "1") int pageNo){
		// 공지사항 갯수
		int totalRows = qnaService.countNotice(); 
		
		// 페이저 객체 생성(페이지 당 행 수, 그룹 당 페이지 수, 전체 행 수, 현재 페이지 번호)
		Pager pager = new Pager(10, 5, totalRows, pageNo);
		
		// 해당 페이지의 게시물 목록 가져오기
		List<Notice> list = qnaService.getNoticeList(pager);
		
		// 여러 객체를 리턴하기 위해 Map 객체 생성		
		Map<String, Object> map = new HashMap<>();
		
		// map에 데이터 넣기
		map.put("notice", list);
		map.put("pager", pager);
		
		return map; // return 값은 JSON으로 변환되어 응답 본문에 들어간다. {"pager":{}, "notice":[]};
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
	
	// 공지 사항 검색
//	@GetMapping("Notice")
//	public Map<String, Object> searchNoticeList(@RequestParam(defaultValue = "1") int pageNo, String searchKeyword){
//		// 공지사항 갯수
//		int totalRows = qnaService.countNotice(); 
//		
//		// 페이저 객체 생성(페이지 당 행 수, 그룹 당 페이지 수, 전체 행 수, 현재 페이지 번호)
//		Pager pager = new Pager(10, 5, totalRows, pageNo);
//		
//		// 해당 페이지의 게시물 목록 가져오기
//		List<Notice> list = qnaService.getNoticeList(pager);
//		
//		// 여러 객체를 리턴하기 위해 Map 객체 생성		
//		Map<String, Object> map = new HashMap<>();
//		
//		// map에 데이터 넣기
//		map.put("notice", list);
//		map.put("pager", pager);
//		
//		return map; // return 값은 JSON으로 변환되어 응답 본문에 들어간다. {"pager":{}, "notice":[]};
//	}
}
