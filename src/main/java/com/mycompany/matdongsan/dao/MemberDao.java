package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.Member;

@Mapper
public interface MemberDao {

	Member selectByMid(String username);

}
