package com.mycompany.matdongsan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.matdongsan.dao.AgentDao;
import com.mycompany.matdongsan.dao.AgentDetailDao;
import com.mycompany.matdongsan.dao.UserEmailDao;
import com.mycompany.matdongsan.dto.Agent;
import com.mycompany.matdongsan.dto.AgentDetail;
import com.mycompany.matdongsan.dto.UserEmail;

@Service
public class AgentService {
	@Autowired
	private AgentDao agentDao;
	@Autowired
	private AgentDetailDao agentDetailDao;
	@Autowired
	private UserEmailDao userEmailDao;
	// agent 데이터 카운트
	public int getCount() {
		int totalAgentCount = agentDao.getAgentCount();
		return totalAgentCount;
	}

	// Agent 데이터 리스트 가져오기
	public List<Agent> getAgentList() {
		List<Agent> agentList = agentDao.getAgentList();
		return agentList;
	}
	//중개인 상세 데이터 추가
	public void insertAgentData(AgentDetail agentDetail) {
		agentDetailDao.insertNewAgentDetailData(agentDetail);
	}
	//중개인 데이터 추가 (회원가입)
	public void joinByAgent(Agent agent) {
		agentDao.joinByAgent(agent);
	}
	//공통 회원가입 데이터부분 저장
	public void joinByUserEmail(UserEmail userEmail) {
		userEmailDao.joinByUserEmail(userEmail);

	}

}
