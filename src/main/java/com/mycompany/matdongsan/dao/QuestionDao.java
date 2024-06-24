package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.Question;

@Mapper
public interface QuestionDao {
	
	public int insert(Question question);
}
