package com.mycompany.matdongsan.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.matdongsan.dto.Property;
import com.mycompany.matdongsan.dto.PropertyDetail;
import com.mycompany.matdongsan.dto.PropertyPhoto;
import com.mycompany.matdongsan.dto.TotalProperty;
import com.mycompany.matdongsan.dto.UserEmail;
import com.mycompany.matdongsan.service.PropertyService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class PropertyController {
	@Autowired
	private PropertyService propertyService;
	
	// 리스트
	@GetMapping("/Property")
	public String getPropertyList() {
		return null;
	}
	
//	읽기
//	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/Property/{pnumber}")
	public Property readProperty(@PathVariable int pnumber) {
		Property property = propertyService.getPropertyDetail(pnumber);
		
		// JSON으로 변환되지 않는 필드는 NULL 처리
		property.setPthumbnail(null);
		return property;
	}
	
//	등록
//	@PreAuthorize("hasAuthority('ROLE_USER')")
	@Transactional
	@PostMapping("/PropertyForm")
	public Property createProperty(@ModelAttribute TotalProperty totalProperty, Authentication authentication) throws IOException {
		
		Property property = totalProperty.getProperty();
		PropertyDetail propertyDetail = totalProperty.getPropertyDetail();
		PropertyPhoto propertyPhoto = totalProperty.getPropertyPhoto();
		
	    // 사용자 설정
		// 추후 authentication 설정하기
		property.setPUnumber(9);
		property.setPstatus("활성화");
		
		// property 파일 첨부 여부
		if(property.getPthumbnail() != null && !property.getPthumbnail().isEmpty()) {
			MultipartFile mf = property.getPthumbnail();
			property.setPthumbnailoname(mf.getOriginalFilename());
			property.setPthumbnailtype(mf.getContentType());
			property.setPthumbnaildata(mf.getBytes());
		}
		
		// propertyPhoto 파일 첨부 여부
		if(propertyPhoto.getPpattach() != null && !propertyPhoto.getPpattach().isEmpty()) {
			MultipartFile mf = propertyPhoto.getPpattach();
			propertyPhoto.setPpattachoname(mf.getOriginalFilename());
			propertyPhoto.setPpattachtype(mf.getContentType());
			propertyPhoto.setPpattachdata(mf.getBytes());
		}

		propertyService.createProperty(property, propertyDetail, propertyPhoto);
		
		// JSON으로 변환되지 않는 필드는 null 처리
		property.setPthumbnail(null);
		property.setPthumbnaildata(null);
		propertyPhoto.setPpattach(null);
		propertyPhoto.setPpattachdata(null);
		
		return property;
	}
	
//	수정
//	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PutMapping("/updateProperty")
	public Property updateProperty(Property property) throws IOException {
		if(property.getPthumbnail() != null && !property.getPthumbnail().isEmpty()) {
			MultipartFile mf = property.getPthumbnail();
			property.setPthumbnailoname(mf.getOriginalFilename());
			property.setPthumbnailtype(mf.getContentType());
			property.setPthumbnaildata(mf.getBytes());
		}
		propertyService.updateProperty(property);
		
		// 수정된 내용의 property 객체 얻기
		property = propertyService.getPropertyDetail(property.getPnumber());
		property.setPthumbnail(null);
		return property;
	}
	
	// 삭제
}
