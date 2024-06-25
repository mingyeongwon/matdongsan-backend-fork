package com.mycompany.matdongsan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.matdongsan.dao.PropertyDao;
import com.mycompany.matdongsan.dto.Property;

@Service
public class PropertyService {
	@Autowired
	private PropertyDao propertyDao;

	public void createProperty(Property property) {
		// TODO Auto-generated method stub
		
	}

	public Property getPropertyDetail(int pnumber) {
		Property property = propertyDao.selectByPnumber(pnumber);
		return null;
	}

}
