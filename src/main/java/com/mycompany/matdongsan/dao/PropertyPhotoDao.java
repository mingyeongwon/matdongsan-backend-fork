package com.mycompany.matdongsan.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.PropertyPhoto;

@Mapper
public interface PropertyPhotoDao {
	// 생성
	public int createPropertyByPropertyPhoto(PropertyPhoto propertyPhoto);
	
	// 수정
	public int updatePropertyByPropertyPhoto(PropertyPhoto propertyPhoto);
	
	// 읽기
	public PropertyPhoto selectByPpnumber(int ppnumber);
	
	// pk 값 가져오기
	public List<Integer> selectPpnumbersByPnumber(int ppPnumber);
	
	// 삭제
	public int deleteByPpnumber(int ppnumber);

}
