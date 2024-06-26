package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.matdongsan.dto.UserEmail;

@Mapper
public interface UserEmailDao {
	
	// 회원가입
//	public int joinByUserEmail(UserEmail userEmail);
	
	//회원가입
	public int insertUserDataByUser(UserEmail userEmail);
	
	public UserEmail selectByUnumber(String username);

	public int getUserIdByUsername(String username);

}
