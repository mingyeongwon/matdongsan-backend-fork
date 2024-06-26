package com.mycompany.matdongsan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.matdongsan.dao.PropertyDao;
import com.mycompany.matdongsan.dao.PropertyDetailDao;
import com.mycompany.matdongsan.dao.PropertyPhotoDao;
import com.mycompany.matdongsan.dto.Property;
import com.mycompany.matdongsan.dto.PropertyDetail;
import com.mycompany.matdongsan.dto.PropertyPhoto;

@Service
public class PropertyService {
	@Autowired
	private PropertyDao propertyDao;
	@Autowired
	private PropertyDetailDao propertyDetailDao;
	@Autowired
	private PropertyPhotoDao propertyPhotoDao;
	
	// 생성
	public void createProperty(Property property, PropertyDetail propertyDetail, PropertyPhoto propertyPhoto) {
		// property
		propertyDao.createPropertyByProperty(property);
		
		// propertyDetail
		propertyDetail.setPdPnumber(property.getPnumber()); // FK 값 주기
        propertyDetailDao.createPropertyByPropertyDetail(propertyDetail);
        
        // propertyPhoto
        propertyPhoto.setPpPnumber(property.getPnumber()); // FK 값 주기
        propertyPhotoDao.createPropertyByPropertyPhoto(propertyPhoto);
	}
	
	// 읽기
	public Property getPropertyDetail(int pnumber) {
		Property property = propertyDao.selectByPnumber(pnumber);
		return null;
	}
	
	// 수정
	public void updateProperty(Property property) {
		// TODO Auto-generated method stub
		
	}

}
