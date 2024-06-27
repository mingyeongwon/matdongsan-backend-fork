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
	public String getUserRole(String name) {
		return userEmailDao.getUserRoleByUserName(name);
	}
	public void getUserRole(String name,boolean isDeactivate) {
		userEmailDao.getUserDataByUserName(name,isDeactivate);
	}

	public int getMemberNumberByMemberEmail(String name) {
		int userName=userEmailDao.getUserIdByUsername(name);
		int memberNumber = memberDao.getMemberNumberByMemberEmail(userName);
		return memberNumber;
		
	}

	public Member getMemberDataFullyByUserNumber(int userNumber) {
		Member member = memberDao.getMemberDataByUserNumber(userNumber);
		return member;
	}

}
