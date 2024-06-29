package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.matdongsan.dto.UserComment;

@Mapper
public interface UserCommentDao {
	// 자식 댓글 존재 여부
	public int isChildComment(int ucnumber, int pnumber);
	
	// 삭제
	public void deletePropertyComment(int pnumber, int cnumber, int userNumber);
	
	// 댓글 가져오기
	public UserComment getCommentByCnumber(int cnumber);
	
	// 생성
	public void createPropertyComment(UserComment comment);
	
	// 수정
	public void updatePropertyComment(UserComment userComment);
}
