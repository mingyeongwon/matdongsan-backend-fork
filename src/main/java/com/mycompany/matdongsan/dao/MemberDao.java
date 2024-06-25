package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.Member;

@Mapper
public interface MemberDao {

	// 일반 유저 회원가입
	public int joinByMember(Member member);

}
