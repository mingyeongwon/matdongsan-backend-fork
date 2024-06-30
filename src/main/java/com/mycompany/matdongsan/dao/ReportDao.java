package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.Report;

@Mapper
public interface ReportDao {
	
	// 매물 신고
	public void createPropertyReport(Report report);

}
