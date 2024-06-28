package com.mycompany.matdongsan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.matdongsan.dao.UserCommentDao;
import com.mycompany.matdongsan.dao.PropertyDao;
import com.mycompany.matdongsan.dao.PropertyDetailDao;
import com.mycompany.matdongsan.dao.PropertyListingDao;
import com.mycompany.matdongsan.dao.PropertyPhotoDao;
import com.mycompany.matdongsan.dto.UserComment;
import com.mycompany.matdongsan.dto.Property;
import com.mycompany.matdongsan.dto.PropertyDetail;
import com.mycompany.matdongsan.dto.PropertyListing;
import com.mycompany.matdongsan.dto.PropertyPhoto;

@Service
public class PropertyService {
	@Autowired
	private PropertyDao propertyDao;
	@Autowired
	private PropertyDetailDao propertyDetailDao;
	@Autowired
	private PropertyPhotoDao propertyPhotoDao;
	@Autowired
	private PropertyListingDao propertyListingDao;
	@Autowired
	private UserCommentDao commentDao;
	
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
	
	// 수정 시 propertyPhoto 삭제
	public int deletePropertyPhoto(int ppnumber) {
		return propertyPhotoDao.deleteByPpnumber(ppnumber);
	}
	
	// property 전체 개수 
	public int getAllPropertyCount() {
		int totalPropertyRows = propertyDao.getAllPropertyCount();
		return totalPropertyRows;
	}
	
	// property 개수 by filter and keyword
	public int getPropertyCountByFilter(String keyword, String price, String date, String rentType) {
		int totalPropertyRows = propertyDao.getPropertyCountByFilter(keyword, price, date, rentType);
		return totalPropertyRows;
	}
	
	// property 전체 리스트
	public List<Property> getAllPropertyList(int offset, int limit) {
		List<Property> propertyList = propertyDao.getAllPropertyList(offset, limit);
		return propertyList;
	}

	// property 리스트 by filter and keyword
	public List<Property> getPropertyListByFilter(int offset, int limit, String keyword, String price,
			String date, String rentType) {
		List<Property> propertyList = propertyDao.getPropertyListByFilter(offset, limit, keyword, price, date, rentType);
		return propertyList;
	}
	
	// 삭제
	public void deleteProperty(int pnumber) {
		// propertyDetail
		propertyDetailDao.deletePropertyDetailByPdPnumber(pnumber);		
		
		// propertyPhoto
		propertyPhotoDao.deletePropertyPhotoByPpPnumber(pnumber);	
		
		// property
		propertyDao.deletePropertyByPnumber(pnumber);			
	}

	public void readProperty(int pnumber) {
		// TODO Auto-generated method stub
		
	}
	
	// 읽기 - propertyDetail
	public PropertyDetail getPropertyDetailByPdPnumber(int pnumber) {
		PropertyDetail propertyDetail = propertyDetailDao.selectPropertyDetailByPdPnumber(pnumber);
		return propertyDetail;
	}
	
	// 읽기 - propertyDetail
	public PropertyPhoto getPropertyPhotoByPpPnumber(int pnumber) {
		PropertyPhoto propertyPhoto = propertyPhotoDao.selectPropertyPhotoByPpPnumber(pnumber);
		return propertyPhoto;
	}
	
	// 상품 구매
	public void purchasePropertyListing(PropertyListing propertyListing) {
		propertyListingDao.createPropertyListing(propertyListing);
		
	}
	
	public boolean checkPropertyCondition(int userNumber) {
		return propertyListingDao.checkUserDataInPropertyListing(userNumber)>0? true : false;
	}
	
	//  댓글 작성 시 매물 주인 여부
	public boolean isPropertyOwner(int pnumber, int userNumber) {
		int propertyCount = propertyDao.isPropertyOwnerByComment(pnumber, userNumber);
		if(propertyCount == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	// 댓글 생성
	public void createPropertyComment(UserComment comment) {
		commentDao.createPropertyComment(comment);
		
	}


	public boolean isFirstCommentOwner(int cUnumber, int pnumber) {
		// TODO Auto-generated method stub
		return false;
	}
	
	// 댓글 삭제
	public void deletePropertyComment(int pnumber, int cnumber, int userNumber) {
		commentDao.deletePropertyComment(pnumber, cnumber, userNumber);
	}
	
	// 자식 댓글 존재 여부
	public boolean isComment(int cnumber, int pnumber) {
		return commentDao.isChildComment(cnumber, pnumber) != 0 ? true : false;
	}
	
	// 댓글 정보 가져오기
	public UserComment getCommentByCnumber(int cnumber) {
		return commentDao.getCommentByCnumber(cnumber);
	}


}
