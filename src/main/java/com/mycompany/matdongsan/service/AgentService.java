package com.mycompany.matdongsan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.matdongsan.dao.AgentDao;
import com.mycompany.matdongsan.dao.AgentDetailDao;
import com.mycompany.matdongsan.dao.UserCommonDataDao;
import com.mycompany.matdongsan.dto.Agent;
import com.mycompany.matdongsan.dto.AgentDetail;
import com.mycompany.matdongsan.dto.AgentSignupData;
import com.mycompany.matdongsan.dto.UserCommonData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AgentService {
	@Autowired
	private AgentDao agentDao;
	@Autowired
	private AgentDetailDao agentDetailDao;
	@Autowired
	private UserCommonDataDao userEmailDao;

	// agent 데이터 카운트
	public int getCount() {
		int totalAgentCount = agentDao.getAgentCount();
		return totalAgentCount;
	}

	// Agent 데이터 리스트 가져오기
	public List<Agent> getAgentList(int offset, int limit) {
		List<Agent> agentList = agentDao.getAgentList(offset, limit);
		log.info(agentList.toString());
		return agentList;
	}

	public List<Agent> getAgentList(int offset, int limit, String keyword) {
		List<Agent> agentList = agentDao.getAgentListByKeyword(offset, limit, keyword);
		return agentList;
	}

	// 중개인 상세 데이터 추가
	public void insertAgentData(AgentDetail agentDetail) {
		agentDetailDao.insertNewAgentDetailData(agentDetail);
	}

	// 중개인 데이터 추가 (회원가입)
	public void joinByAgent(Agent agent) {
		agentDao.joinByAgent(agent);
	}

	// 공통 회원가입 데이터부분 저장
	public void joinByUserEmail(UserCommonData userEmail) {
		userEmailDao.insertUserDataByUser(userEmail);

	}

	public int getAllAgentCount() {
		int totalAgentRows = agentDao.getAllAgentCount();
		return totalAgentRows;
	}

	public int getUserIdByUserName(String username) {
		int userId = userEmailDao.getUserIdByUsername(username);
		return userId;
	}

	public AgentSignupData getAgentDataFullyByUserNumber(int agentNumber) {
		AgentSignupData agentSignupData = new AgentSignupData();
		log.info("check");
		agentSignupData.setAgent(agentDao.getAgentDataByAgentNumber(agentNumber));
		log.info("check1");
		agentSignupData.setAgentDetail(
				agentDetailDao.getAgentDetailDataByAgentNumber(agentSignupData.getAgent().getAnumber()));
		return agentSignupData;
	}

	public void updateAgentData(Agent agent, AgentDetail agentDetail) {
		// TODO Auto-generated method stub
		agentDao.updateAgentData(agent);
		agentDetailDao.updateAgentDetailData(agentDetail);
	}

	public int getAgentNumberByUserNumber(int userNum) {
		// TODO Auto-generated method stub
		return agentDao.getAgentDataByAgentNumber(userNum).getAnumber();
	}

}
