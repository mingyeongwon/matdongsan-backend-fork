package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.PropertyDetail;

@Mapper
public interface PropertyDetailDao {

	public int createPropertyByPropertyDetail(PropertyDetail propertyDetail);

}
