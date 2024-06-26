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
	
	// 수정
	public void updateProperty(Property property, PropertyDetail propertyDetail, PropertyPhoto propertyPhoto) {
		propertyDao.updatePropertyByProperty(property);
		propertyDetailDao.updatePropertyByPropertyDetail(propertyDetail);
		if (propertyPhoto.getPpattachdata() != null && propertyPhoto.getPpattachdata().length > 0) {
			propertyPhotoDao.updatePropertyByPropertyPhoto(propertyPhoto);
		}
		
	}
	
	// 읽기 - property
	public Property getProperty(int pnumber) {
		Property property = propertyDao.selectByPnumber(pnumber);
		propertyDao.updatePhitcount(pnumber);
		return property;
	}
	
	// 읽기 - propertyDetail
	public PropertyDetail getPropertyDetail(int pdnumber) {
		PropertyDetail propertyDetail = propertyDetailDao.selectByPdnumber(pdnumber);
		return propertyDetail;
	}
	
	// 읽기 - propertyPhoto
	public PropertyPhoto getPropertyPhoto(int ppnumber) {
		PropertyPhoto propertyPhoto = propertyPhotoDao.selectByPpnumber(ppnumber);
		return propertyPhoto;
	}
	
	// pk 값 가져오기 - propertyDetail
	public int getPdnumber(int pdPnumber) {
		int pdnumber = propertyDetailDao.selectPdnumberByPnumber(pdPnumber);
		return pdnumber;
	}

	// pk 값 가져오기 - propertyDetail
	public int getPpnumber(int ppPnumber) {
		int ppnumber = propertyPhotoDao.selectPpnumberByPnumber(ppPnumber);
		return ppnumber;
	}

}
