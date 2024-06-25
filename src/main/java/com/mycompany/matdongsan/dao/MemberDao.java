package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.Member;

@Mapper
public interface MemberDao {

	public Member selectByMid(String username);
	
	// 일반 유저 회원가입
	public int joinByMember(Member member);

}
