package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.PropertyPhoto;

@Mapper
public interface PropertyPhotoDao {

	public int createPropertyByPropertyPhoto(PropertyPhoto propertyPhoto);

}
