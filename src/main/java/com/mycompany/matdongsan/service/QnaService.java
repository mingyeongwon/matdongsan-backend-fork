package com.mycompany.matdongsan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.matdongsan.dao.QuestionDao;
import com.mycompany.matdongsan.dto.Notice;
import com.mycompany.matdongsan.dto.Pager;
import com.mycompany.matdongsan.dto.Question;

@Service
public class QnaService {
	@Autowired
	private QuestionDao questionDao;
	
	// 고객 문의 DB 저장 
	public int insertQuestion(Question question) {
		return questionDao.insertQuestion(question);
	}
	
	// 고객 문의 읽기
	public Question getQuestion(int qnumber) {
		Question question =  questionDao.getQuestionByQnumber(qnumber);
		return question;
	}
	
	// 고객 문의 수정하기
	public int updateQuestion(Question question) {
		return questionDao.updateQuestion(question);
	}
	
	// 고객 문의 삭제하기
	public int deleteQuestionByQnumber(int qnumber) {
		return questionDao.deleteQuestionByQnumber(qnumber);
	}
	
	//공지 사항---------------------//////////////////////////////////////////////////////////////////////
	
	// 공지 사항 DB 저장
	public int insertNotice(Notice notice) {
		return questionDao.insertNotice(notice);
	}
	
	// 공지사항 갯수 가져오기
	public int countNotice() {
		return questionDao.countNotice();
	}
	
	// 공지사항 리스트 가져오기
	public List<Notice> getNoticeList(Pager pager){
		return questionDao.getNoticeList(pager);
	}
	
	// 공지사항 디테일 가져오기
	public Notice getNoticeDetail(int nnumber) {
		return questionDao.getNoticeDetail(nnumber);
	}
	
	// 공지사항 수정하기
	public int updateNotice(Notice notice) {
		return questionDao.updateNotice(notice);
	}
	
	// 공지사항 삭제하기
	public int deleteNotice(int nnumber) {
		return questionDao.deleteNotice(nnumber);
	}
}
