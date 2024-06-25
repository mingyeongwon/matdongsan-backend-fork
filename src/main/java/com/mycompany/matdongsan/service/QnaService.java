package com.mycompany.matdongsan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.matdongsan.dao.QuestionDao;
import com.mycompany.matdongsan.dto.Question;

@Service
public class QnaService {
	@Autowired
	private QuestionDao questionDao;
	
	// 고객 문의 DB 저장 
	public int insert(Question question) {
		return questionDao.insert(question);
	}
}
