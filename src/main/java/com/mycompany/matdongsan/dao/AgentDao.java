package com.mycompany.matdongsan.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.Agent;
import com.mycompany.matdongsan.dto.AgentDetail;

@Mapper
public interface AgentDao {
	//총 중개인 수
	int getAgentCount();
	//전체 중개인 데이터 리스트
	List<Agent> getAgentList();
	void insertNewAgentData(Agent agent);
	void joinByAgent(Agent agent);

}
