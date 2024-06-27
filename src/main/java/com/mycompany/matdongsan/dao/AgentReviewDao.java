package com.mycompany.matdongsan.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.AgentReview;

@Mapper
public interface AgentReviewDao {

	void createAgentReviewByMember(AgentReview agentReview);

	void deleteAgentReview(int arnumber, int anumber, int userNumber);

	List<AgentReview> getAgentReviewByAnumber(int anumber);

	void updateAgentReview(AgentReview agentReview);

}
