package com.mycompany.matdongsan.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.PropertyListing;

@Mapper
public interface PropertyListingDao {

	void createPropertyListing(PropertyListing propertyListing);

	int checkUserDataInPropertyListing(int userNumber);

}
