package com.mycompany.matdongsan.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.matdongsan.dto.Question;
import com.mycompany.matdongsan.service.QnaService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/Qna")
public class QnaController {
	@Autowired 
	private QnaService qnaService;
	
	// 고객 문의 생성
	@PostMapping("/CustomerInquiryForm")
	public Question create(Question question) {
		if(question.getQattach() != null && !question.getQattach().isEmpty()) {
			log.info("첨부 파일 있음");
			
			MultipartFile mf = question.getQattach();
			
			question.setQattachoname(mf.getOriginalFilename());
			question.setQattachtype(mf.getContentType());
			try {
				question.setQattachdata(mf.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
		} else {
			log.info("첨부 파일 없음");
		}
		
//		question.setQEmail(authentication.getName());
		question.setQEmail("user");
		log.info(question.toString()); // postman으로 볼 수 없는 multipartfile이나 byte배열을 확인 
		
		qnaService.insert(question);
		
		// json 반환할 때 json으로 변환할 수 없는 multipartfile이나 byte배열같은 타입은 빼야 한다.
		question.setQattach(null);
		question.setQattachdata(null);
		return question;
	}
	
//	@GetMapping("/MyPage/CustomerInquiry")
//	public Question read(@PathVariable int qnumber) {
//		
//		return ;
//	}
}
