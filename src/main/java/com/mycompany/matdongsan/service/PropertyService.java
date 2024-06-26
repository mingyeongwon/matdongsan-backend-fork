package com.mycompany.matdongsan.service;

import java.util.List;

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
	
	// 생성 - property and propertyDetail
	public void createProperty(Property property, PropertyDetail propertyDetail) {
		// property
		propertyDao.createPropertyByProperty(property);
		
		// propertyDetail
		propertyDetail.setPdPnumber(property.getPnumber()); // FK 값 주기
        propertyDetailDao.createPropertyByPropertyDetail(propertyDetail);
	}
	
	// 생성 - propertyPhoto
	public void createPropertyByPropertyPhoto(PropertyPhoto propertyPhoto) {
        propertyPhotoDao.createPropertyByPropertyPhoto(propertyPhoto);
	}
	
	// 수정 
	public void updateProperty(Property property, PropertyDetail propertyDetail) {
		// property
		propertyDao.updatePropertyByProperty(property);
		
		// propertyDetail
		propertyDetailDao.updatePropertyByPropertyDetail(propertyDetail);
	}
	
	// 수정 - propertyPhoto
	public void updatePropertyByPropertyPhoto(PropertyPhoto propertyPhoto) {
    	if (propertyPhoto.getPpattach() != null && !propertyPhoto.getPpattach().isEmpty()) {
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

	// pk 값 가져오기 - propertyPhoto
	public List<Integer> getPpnumbers(int ppPnumber) {
		List<Integer> ppnumbers = propertyPhotoDao.selectPpnumbersByPnumber(ppPnumber);
		return ppnumbers;
	}
	
	// 삭제 - propertyPhoto
	public int deletePropertyPhoto(int ppnumber) {
		return propertyPhotoDao.deleteByPpnumber(ppnumber);
	}

}
