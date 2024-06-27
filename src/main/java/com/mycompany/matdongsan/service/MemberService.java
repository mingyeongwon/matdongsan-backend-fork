package com.mycompany.matdongsan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.matdongsan.dao.MemberDao;
import com.mycompany.matdongsan.dao.UserCommonDataDao;
import com.mycompany.matdongsan.dto.Member;
import com.mycompany.matdongsan.dto.UserCommonData;

@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private UserCommonDataDao userEmailDao;
	
	public void joinUserByMember(UserCommonData userEmail) {
		userEmailDao.insertUserDataByUser(userEmail);
	}
	
	public void joinByMember(Member member) {
		memberDao.insertMemberData(member);
	}

}
