package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.Property;

@Mapper
public interface PropertyDao {
	// 읽기
	public Property selectByPnumber(int pnumber);
	
	// 생성
	public int createPropertyByProperty(Property property);
	
	// 수정
	public int updatePropertyByProperty(Property property);

	public int updatePhitcount(int pnumber);

}
