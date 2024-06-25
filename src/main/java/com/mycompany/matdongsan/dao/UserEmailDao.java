package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.UserEmail;

@Mapper
public interface UserEmailDao {

	UserEmail selectByUnumber(String username);

	void joinByUserEmail(UserEmail userEmail);

}
