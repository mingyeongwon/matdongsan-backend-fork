package com.mycompany.matdongsan.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.matdongsan.dto.Property;
import com.mycompany.matdongsan.dto.PropertyDetail;
import com.mycompany.matdongsan.dto.PropertyPhoto;
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
	
	// 읽기
	@GetMapping("/Property/{pnumber}")
	public Property readProperty(@PathVariable int pnumber) {
		Property property = propertyService.getPropertyDetail(pnumber);
		
		// JSON으로 변환되지 않는 필드는 NULL 처리
		property.setPthumbnail(null);
		return property;
	}
	
//	등록
//	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PostMapping("/createProperty")
	public Property createProperty(Property property, PropertyDetail propertyDetail, 
			PropertyPhoto propertyPhoto, UserEmail userEmail, Authentication authentication) throws IOException {
		if(property.getPthumbnail() != null && !property.getPthumbnail().isEmpty()) {
			MultipartFile mf = property.getPthumbnail();
			property.setPthumbnailoname(mf.getOriginalFilename());
			property.setPthumbnailtype(mf.getContentType());
			property.setPthumbnaildata(mf.getBytes());
		}
		// db에 저장
		userEmail.setUemail(authentication.getName());
		propertyService.createProperty(property);
		
		// JSON으로 변환되지 않는 필드는 null 처리
		property.setPthumbnail(null);
		property.setPthumbnaildata(null);
		return property;
	}
	
	// 수정
	
	// 삭제
}
