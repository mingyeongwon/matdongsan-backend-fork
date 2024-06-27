package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;
import com.mycompany.matdongsan.dto.Question;

@Mapper
public interface QuestionDao {
	
	public int insertQuestion(Question question);
	public Question getQuestionByQnumber(int qnumber);
	public int updateQuestion(Question question);
	public int deleteQuestionByQnumber(int qnumber);
	
}
