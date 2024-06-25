package com.mycompany.matdongsan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.matdongsan.dao.MemberDao;
import com.mycompany.matdongsan.dto.Member;

@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;
	
	public void joinByMember(Member member) {
		memberDao.joinByMember(member);
		
	}

}
