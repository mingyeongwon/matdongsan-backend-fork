package com.mycompany.matdongsan.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.Notice;
import com.mycompany.matdongsan.dto.Pager;
import com.mycompany.matdongsan.dto.Question;

@Mapper
public interface QuestionDao {
	
	public int insertQuestion(Question question);
	public Question getQuestionByQnumber(int qnumber);
	public int updateQuestion(Question question);
	public int deleteQuestionByQnumber(int qnumber);
	
	public int insertNotice(Notice notice);
	public int countNotice();
	public List<Notice> getNoticeList(Pager pager);
	public Notice getNoticeDetail(int nnumber);
	public int updateNotice(Notice notice);
	public int deleteNotice(int nnumber);
}
