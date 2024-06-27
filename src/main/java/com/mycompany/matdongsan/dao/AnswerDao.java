package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.Answer;

@Mapper
public interface AnswerDao {
	
	public int insertAnswer(Answer answer);
	public int updateAnswer(Answer answer);
	public Answer getAnswer(int anumber);
	public int deleteAnswer(int anumber);
}
