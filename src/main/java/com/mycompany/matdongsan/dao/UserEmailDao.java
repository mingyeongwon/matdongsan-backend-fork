package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.UserEmail;

@Mapper
public interface UserEmailDao {
	
	// 회원가입
//	public int joinByUserEmail(UserEmail userEmail);

	public int insertUserDataByMember(UserEmail userEmail);

	public UserEmail selectByUnumber(String username);

	public void joinByUserEmail(UserEmail userEmail);
	
}
