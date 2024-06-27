package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.matdongsan.dto.UserCommonData;

@Mapper
public interface UserCommonDataDao {
	
	// 회원가입
//	public int joinByUserEmail(UserEmail userEmail);
	
	//회원가입
	public int insertUserDataByUser(UserCommonData userCommonData);
	
	public UserCommonData selectByUnumber(String username);

	public int getUserIdByUsername(String username);

	public void getUserDataByUserName(String name,boolean isDeactivate);

	public String getUserRoleByUserName(String name);

}
