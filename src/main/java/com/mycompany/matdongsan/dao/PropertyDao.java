package com.mycompany.matdongsan.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.matdongsan.dto.Property;

@Mapper
public interface PropertyDao {
	// 읽기
	public Property selectByPnumber(int pnumber);
	
	// 생성
	public int createPropertyByProperty(Property property);
	
	// 수정
	public int updatePropertyByProperty(Property property);
	
	// 조회수
	public int updatePhitcount(int pnumber);
	
	// property 총 개수
	public int getAllPropertyCount();
	
	// property 개수 by filter and keyword
	public int getPropertyCountByFilter(@Param("keyword") String keyword, @Param("price") String price, @Param("date") String date, @Param("rentType") String rentType);
	
	// property 전체 리스트
	public List<Property> getAllPropertyList(@Param("offset") int offset, @Param("limit") int limit);
	
	// property 리스트 by filter and keyword
	public List<Property> getPropertyListByFilter(@Param("offset") int offset, @Param("limit") int limit, @Param("keyword") String keyword, 
			@Param("price") String price, @Param("date") String date, @Param("rentType") String rentType);


}
