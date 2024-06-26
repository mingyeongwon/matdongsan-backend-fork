package com.mycompany.matdongsan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.matdongsan.dao.MemberDao;
import com.mycompany.matdongsan.dao.UserEmailDao;
import com.mycompany.matdongsan.dto.Member;
import com.mycompany.matdongsan.dto.UserEmail;

@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private UserEmailDao userEmailDao;
	
	public void joinUserByMember(UserEmail userEmail) {
		userEmailDao.insertUserDataByUser(userEmail);
	}
	
	public void joinByMember(Member member) {
		memberDao.insertMemberData(member);
	}

}
