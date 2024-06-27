package com.mycompany.matdongsan.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.matdongsan.dto.Comment;
import com.mycompany.matdongsan.dto.Pager;
import com.mycompany.matdongsan.dto.Property;
import com.mycompany.matdongsan.dto.PropertyDetail;
import com.mycompany.matdongsan.dto.PropertyPhoto;
import com.mycompany.matdongsan.dto.TotalProperty;
import com.mycompany.matdongsan.service.PropertyService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class PropertyController {
	@Autowired
	private PropertyService propertyService;
	
//  리스트
	@GetMapping("/Property")
	public Map<String, Object> getPropertyList(@RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String keyword,
			@RequestParam(required = false) String price, @RequestParam(required = false) String date,
			@RequestParam(required = false) String rentType) {

		// 검색 내용 찾기 : 주소, 필터 : price, date, rentType
		int totalPropertyRows;
		Pager pager;
		List<Property> Propertylist;
		
		if(keyword != null || price != null || date != null || rentType != null) {
			totalPropertyRows = propertyService.getPropertyCountByFilter(keyword, price, date, rentType);
			pager = new Pager(size, pageNo, totalPropertyRows);
			Propertylist = propertyService.getPropertyListByFilter(pager.getStartRowIndex(), pager.getRowsPerPage(), keyword, price, date, rentType);
		} else { // 전체 리스트 
			totalPropertyRows = propertyService.getAllPropertyCount();
            pager = new Pager(size, pageNo, totalPropertyRows);
            Propertylist = propertyService.getAllPropertyList(pager.getStartRowIndex(), pager.getRowsPerPage());
        }
		
		// 여러 객체를 리턴하기 위해 map 객체 생성 (property, pager)
		Map<String, Object> map = new HashMap<>();
		map.put("property", Propertylist);
		map.put("pager", pager);
		return map; // { "property" : {}, "pager" : {}}
	}
	
	
//	읽기
	@GetMapping("/Property/{pnumber}")
	public TotalProperty readProperty(@PathVariable int pnumber, @ModelAttribute TotalProperty totalProperty) {
	    
	    totalProperty.setProperty(propertyService.getProperty(pnumber));
	    totalProperty.setPropertyDetail(propertyService.getPropertyDetailByPdPnumber(pnumber));
	    totalProperty.setPropertyPhoto(propertyService.getPropertyPhotoByPpPnumber(pnumber));	    
	    
		return totalProperty;
	}
	
	
//	등록
//	@PreAuthorize("hasAuthority('ROLE_USER')")
	@Transactional
	@PostMapping("/PropertyForm")
	public TotalProperty createProperty(@ModelAttribute TotalProperty totalProperty, Authentication authentication) throws IOException {
		
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
		
		propertyService.createProperty(property, propertyDetail);
		
		// propertyPhoto 
		log.info("propertyPhotos null 여부 : " + propertyPhoto.getPpattach().isEmpty());
		
		// propertyPhoto 파일 첨부 여부
		if(propertyPhoto.getPpattach() != null && !propertyPhoto.getPpattach().isEmpty()) {
			List<MultipartFile> files = propertyPhoto.getPpattach();
			if(files != null && !files.isEmpty()) {	
				for(MultipartFile file : files) {
					log.info(file.getOriginalFilename());
					propertyPhoto.setPpattachoname(file.getOriginalFilename());
					propertyPhoto.setPpattachtype(file.getContentType());
					propertyPhoto.setPpattachdata(file.getBytes());
			        propertyPhoto.setPpPnumber(property.getPnumber()); // FK 값 주기
			        propertyService.createPropertyByPropertyPhoto(propertyPhoto);
				}
			}
		}
		
		// JSON으로 변환되지 않는 필드는 null 처리
		property.setPthumbnail(null);
		property.setPthumbnaildata(null);
	    propertyPhoto.setPpattach(null);
	    propertyPhoto.setPpattachdata(null);
		
		return totalProperty;
	}
	
	
//	수정
//	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PutMapping("/updateProperty")
	public TotalProperty updateProperty(@ModelAttribute TotalProperty totalProperty) throws IOException {
		
		Property property = totalProperty.getProperty();
		PropertyDetail propertyDetail = totalProperty.getPropertyDetail();
		PropertyPhoto propertyPhoto = totalProperty.getPropertyPhoto();
		
		// PK 값 가져오기
		propertyDetail.setPdnumber(propertyService.getPdnumber(property.getPnumber()));
		
		// property 파일 첨부 여부
		if(property.getPthumbnail() != null && !property.getPthumbnail().isEmpty()) {
			MultipartFile mf = property.getPthumbnail();
			property.setPthumbnailoname(mf.getOriginalFilename());
			property.setPthumbnailtype(mf.getContentType());
			property.setPthumbnaildata(mf.getBytes());
		}
		
		propertyService.updateProperty(property, propertyDetail);
		
	    // propertyPhoto 파일 첨부 여부
	    if (propertyPhoto.getPpattach() != null && !propertyPhoto.getPpattach().isEmpty()) {
	        List<Integer> ppnumbers = propertyService.getPpnumbers(property.getPnumber()); // pk 값 가져오기
	        List<MultipartFile> files = propertyPhoto.getPpattach();
	        log.info("files.size() : " + files.size());
	        int existingPhotosCount = ppnumbers.size();
	        int newFilesCount = files.size();

	        for (int i = 0; i < newFilesCount; i++) {
	            MultipartFile file = files.get(i);
	            propertyPhoto.setPpattachoname(file.getOriginalFilename());
	            propertyPhoto.setPpattachtype(file.getContentType());
	            propertyPhoto.setPpattachdata(file.getBytes());
	            if (i < existingPhotosCount) {
	                // 기존 사진을 업데이트하는 경우
	                propertyPhoto.setPpnumber(ppnumbers.get(i));
		            propertyService.updatePropertyByPropertyPhoto(propertyPhoto);
	            } else {
	                // 새로운 사진을 추가하는 경우
	                propertyPhoto.setPpnumber(0); // 새로운 사진의 경우 ppnumber는 0(null)로 설정하고, DB에서 자동 생성되도록 처리
	                propertyPhoto.setPpPnumber(property.getPnumber()); // FK 값 주기
	                propertyService.createPropertyByPropertyPhoto(propertyPhoto);
	            }
	        }

	        // 기존 사진 중 남은 사진은 삭제 처리 (newFilesCount < existingPhotosCount 인 경우)
	        for (int i = newFilesCount; i < existingPhotosCount; i++) {
	            propertyService.deletePropertyPhoto(ppnumbers.get(i));
	        }
	    }

	    // totalProperty 객체에 수정된 내용 다시 설정
	    totalProperty.setProperty(propertyService.getProperty(property.getPnumber()));
	    totalProperty.setPropertyDetail(propertyService.getPropertyDetail(propertyDetail.getPdnumber()));
	    totalProperty.setPropertyPhoto(propertyService.getPropertyPhoto(propertyPhoto.getPpnumber()));

	    // JSON으로 변환되지 않는 필드는 null 처리
		property.setPthumbnail(null);
		property.setPthumbnaildata(null);
		propertyPhoto.setPpattach(null);
	    propertyPhoto.setPpattachdata(null);
	    
		return totalProperty;
	}
	
	
//	 삭제
//	@PreAuthorize("hasAuthority('ROLE_USER')")	
	@Transactional
	@DeleteMapping("/deleteProperty/{pnumber}")
	public void deleteProperty(@PathVariable int pnumber) {
		propertyService.deleteProperty(pnumber);
	}
	
	
//	상태 변경 (비활성화, 거래완료)
	@PatchMapping("/updatePropertyStatus")
	public Property updatePropertyStatus(Property property) {
		property.setPstatus(null);
		return property;
	}
	
	
//	댓글 생성
	@PostMapping("/Property/{pnumber}")
	public Property createPropertyReview(@PathVariable int pnumber, @ModelAttribute Comment comment,
			Authentication authentication) {
		
		
		
		return null;
	}

//	매물 신고
}
