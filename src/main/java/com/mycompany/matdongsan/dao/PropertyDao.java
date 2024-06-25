package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.Property;

@Mapper
public interface PropertyDao {

	Property selectByPnumber(int pnumber);

}
