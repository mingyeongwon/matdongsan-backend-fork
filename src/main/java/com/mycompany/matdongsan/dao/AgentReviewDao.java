package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.AgentReview;

@Mapper
public interface AgentReviewDao {

	void createAgentReviewByMember(AgentReview agentReview);

}
