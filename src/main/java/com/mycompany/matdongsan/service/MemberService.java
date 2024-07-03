package com.mycompany.matdongsan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
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

	public void deleteAccount(String uemail, Boolean isDeactivate) {
		userEmailDao.deleteUser(uemail, isDeactivate);
	}

	public int getMemberNumberByMemberEmail(String name) {
		int userName = userEmailDao.getUserIdByUsername(name);
		int memberNumber = memberDao.getMemberNumberByMemberEmail(userName);
		return memberNumber;

	}

	public Member getMemberDataFullyByUserNumber(int userNumber) {
		Member member = memberDao.getMemberDataByUserNumber(userNumber);
		return member;
	}

	public int getUnumberByUemail(String userEmail) {
		int userNumber = userEmailDao.getUserIdByUsername(userEmail); // userId = userNumber
		return userNumber;
	}

	//map타입으로 리턴하도록 바꿔야함
	public UserCommonData getUserDataByUemail(String uemail) {
		UserCommonData userData = userEmailDao.getUserDataByUser(uemail);
		return userData;
	}

	public UserCommonData getUserDataFullyByUemail(String uemail) {
		return userEmailDao.getUserDataByUemail(uemail);
	}

	public boolean checkPassword(String currPw, String upassword) {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return passwordEncoder.matches(currPw, upassword);
	}

}
