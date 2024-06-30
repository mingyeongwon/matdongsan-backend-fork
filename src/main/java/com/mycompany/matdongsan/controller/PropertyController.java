package com.mycompany.matdongsan.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.matdongsan.dto.Favorite;
import com.mycompany.matdongsan.dto.Pager;
import com.mycompany.matdongsan.dto.Property;
import com.mycompany.matdongsan.dto.PropertyDetail;
import com.mycompany.matdongsan.dto.PropertyListing;
import com.mycompany.matdongsan.dto.PropertyPhoto;
import com.mycompany.matdongsan.dto.Report;
import com.mycompany.matdongsan.dto.TotalProperty;
import com.mycompany.matdongsan.dto.UserComment;
import com.mycompany.matdongsan.service.AgentService;
import com.mycompany.matdongsan.service.MemberService;
import com.mycompany.matdongsan.service.PagerService;
import com.mycompany.matdongsan.service.PropertyService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class PropertyController {
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private PagerService pagerService;

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

		if (keyword != null || price != null || date != null || rentType != null) {
			totalPropertyRows = propertyService.getPropertyCountByFilter(keyword, price, date, rentType);
			pager = new Pager(size, pageNo, totalPropertyRows);
			Propertylist = propertyService.getPropertyListByFilter(pager.getStartRowIndex(), pager.getRowsPerPage(),
					keyword, price, date, rentType);
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
	public Map<String, Object> readProperty(@PathVariable int pnumber, @ModelAttribute TotalProperty totalProperty, @RequestParam(defaultValue = "1", required = false) String pageNo, 
			@RequestParam(defaultValue = "desc", required = false) String date, HttpSession session) {
		
		// property 정보
		totalProperty.setProperty(propertyService.getProperty(pnumber));
		totalProperty.setPropertyDetail(propertyService.getPropertyDetailByPdPnumber(pnumber));
		totalProperty.setPropertyPhoto(propertyService.getPropertyPhotoByPpPnumber(pnumber));
		
		// property Comment
		int totalPropertyCommentRows = propertyService.getAllPropertyCommentCount(pnumber);
		Pager pager = pagerService.preparePager(session, pageNo, totalPropertyCommentRows, 9, 5, "propertyComment");
		List<UserComment> propertyCommentList = propertyService.getCommentByPnumber(pnumber, date, pager);
		
		Map<String, Object> propertyMap = new HashMap<>();
		propertyMap.put("totalProperty", totalProperty);
		propertyMap.put("propertyCommentList", propertyCommentList);
		
		return propertyMap;
	}
	

//	등록
//	@PreAuthorize("hasAuthority('ROLE_USER')")
	@Transactional
	@PostMapping("/PropertyForm")
	public boolean createProperty(@ModelAttribute TotalProperty totalProperty, Authentication authentication)
			throws IOException {
		
		String userEmail = authentication.getName();
		int userNumber = memberService.getUnumberByUemail(userEmail);
		
		// 유저가 이전에 결제한 적 있는지, 있다면 남아있는 개수가 있는지
		boolean hasPropertyListing = propertyService.checkPropertyCondition(userNumber); 
		
		if(hasPropertyListing) {
			
			Property property = totalProperty.getProperty();
			PropertyDetail propertyDetail = totalProperty.getPropertyDetail();
			PropertyPhoto propertyPhoto = totalProperty.getPropertyPhoto();
			// 사용자 설정
			// 추후 authentication 설정하기
			property.setPUnumber(userNumber);
			property.setPstatus("활성화");
			
			// property 파일 첨부 여부
			if (property.getPthumbnail() != null && !property.getPthumbnail().isEmpty()) {
				MultipartFile mf = property.getPthumbnail();
				property.setPthumbnailoname(mf.getOriginalFilename());
				property.setPthumbnailtype(mf.getContentType());
				property.setPthumbnaildata(mf.getBytes());
			}
			
			propertyService.createProperty(property, propertyDetail);
			
			// propertyPhoto
			log.info("propertyPhotos null 여부 : " + propertyPhoto.getPpattach().isEmpty());
			
			// propertyPhoto 파일 첨부 여부
			if (propertyPhoto.getPpattach() != null && !propertyPhoto.getPpattach().isEmpty()) {
				List<MultipartFile> files = propertyPhoto.getPpattach();
				if (files != null && !files.isEmpty()) {
					for (MultipartFile file : files) {
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
			
			propertyService.updateRemainPropertyListing(userNumber);
			
			return true;
		} else { // 등록권 없음
			// 등록권 소개 페이지로 이동 
			return false;
		}
		

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
		if (property.getPthumbnail() != null && !property.getPthumbnail().isEmpty()) {
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

//	삭제
//	@PreAuthorize("hasAuthority('ROLE_USER')")	
	@Transactional
	@DeleteMapping("/deleteProperty/{pnumber}")
	public void deleteProperty(@PathVariable int pnumber) {

		propertyService.deleteProperty(pnumber);
	}

//	상태 변경 (비활성화, 거래완료)
	@PatchMapping("/updatePropertyStatus")
	public Property updatePropertyStatus(@RequestBody Map<String, Object> requestData) {
		
		int pnumber = (int) requestData.get("pnumber");
		String pstatus = (String) requestData.get("pstatus");
		
		Property property = propertyService.getProperty(pnumber);
		property.setPstatus(pstatus);
		propertyService.updateProperty(property);
		return property;
	}

//	댓글 생성
	@PostMapping("/Property/{pnumber}")
	public UserComment createPropertyComment(@PathVariable int pnumber, @ModelAttribute UserComment userComment,
			Authentication authentication) {

		String userEmail = authentication.getName();
		String userRole = memberService.getUserRole(userEmail);
		int userNumber = memberService.getUnumberByUemail(userEmail);
		boolean isPropertyOwner = propertyService.isPropertyOwner(pnumber, userNumber); // 매물 주인 여부
		log.info(isPropertyOwner + "");
		if (userComment.getUcparentnumber() == 0) { // 부모 댓글 없음
			if (!userRole.equals("MEMBER")) {
				// agent가 댓글 못달게 처리하기
			}
			if (isPropertyOwner) {
				// member여도 매물 주인이면 댓글 못달게 처리
			} else {
				userComment.setUcUnumber(userNumber);
			}
			userComment.setUcparentnumber(0);
		} else { // 부모 댓글 있음

			if (userRole.equals("MEMBER")) { // 기존 댓글 주인 여부 파악하기 위해 member / agent 나눠서 처리
				boolean isFirstCommentOwner = propertyService.isFirstCommentOwner(userComment.getUcUnumber(), pnumber);
				if (!isFirstCommentOwner) {
					// 댓글 주인 아닌 경우 못달게 처리
				} else {
					userComment.setUcUnumber(userNumber);
				}
			} else { // agent인 경우
				if (isPropertyOwner) {
					userComment.setUcUnumber(userNumber);
				} else {
					// 올린 사람 아니면 댓글 못달게 하기
				}
			}
		}
		// 유저 넘버 없음
		userComment.setUcUnumber(userNumber);
		userComment.setUcPnumber(pnumber);
		userComment.setUcremoved(false);
		propertyService.createPropertyComment(userComment);

		return userComment;
	}
	

//	댓글 수정
	@PutMapping("/Property/{pnumber}/{ucnumber}")
	public UserComment updatePropertyComment(@PathVariable int pnumber, @PathVariable int ucnumber,
			@ModelAttribute UserComment userComment, Authentication authentication) {

		String userEmail = authentication.getName();
		int userNumber = memberService.getUnumberByUemail(userEmail);

		userComment.setUcnumber(ucnumber);
		userComment.setUcUnumber(userNumber);
		userComment.setUcPnumber(pnumber);
		propertyService.updatePropertyComment(userComment);
		return userComment;
	}

//	댓글 삭제
	@DeleteMapping("/Property/{pnumber}/{ucnumber}")
	public void deletePropertyComment(@PathVariable int pnumber, @PathVariable int ucnumber,
			Authentication authentication) {

		String userEmail = authentication.getName();
		int userNumber = memberService.getUnumberByUemail(userEmail);
		UserComment comment = propertyService.getCommentByCnumber(ucnumber);

		// 자식 댓글 존재 여부
		boolean isComment = propertyService.isComment(ucnumber, pnumber);
		if (isComment) {
			comment.setUcremoved(true);
			propertyService.updatePropertyComment(comment);
		} else {
			propertyService.deletePropertyComment(pnumber, ucnumber, userNumber);
		}
	}
	
	
//	상품 좋아요 추가
	@PostMapping("/Property/{pnumber}/favorite")
	public boolean addLikeButton(@PathVariable int pnumber, @ModelAttribute Favorite favorite,
			Authentication authentication) {
		
		String userEmail = authentication.getName();
		// agent도 좋아요 할 수 있는지 팀원들이랑 얘기해보기 
		int userNumber = memberService.getUnumberByUemail(userEmail);
		
		boolean existsFavorite = propertyService.existsFavorite(pnumber, userNumber); // 이미 좋아요 눌렀는지
		
		if(!existsFavorite) { // 좋아요 존재하지 않아서 추가하기
			favorite.setFPnumber(pnumber);
			favorite.setFMnumber(userNumber);
			propertyService.addLikeButton(favorite);
			return true;
		} else {
			return false;
		}
		
	}
	
	
//	상품 좋아요 취소
	@DeleteMapping("/Property/{pnumber}/favorite")
	public void cancelLikeButton(@PathVariable int pnumber, @ModelAttribute Favorite favorite,
			Authentication authentication) {
		
		String userEmail = authentication.getName();
		int userNumber = memberService.getUnumberByUemail(userEmail);
		
		propertyService.cancelLikeButton(pnumber, userNumber);
	}	
	

//	매물 신고
	@PostMapping("/Property/{pnumber}/report")
	public Report createPropertyReport(@PathVariable int pnumber, @ModelAttribute Report report,
			Authentication authentication) {

		String userEmail = authentication.getName();
		int userNumber = memberService.getUnumberByUemail(userEmail);
		report.setRPnumber(pnumber);
		report.setRUnumber(userNumber);
		
		propertyService.createPropertyReport(report);
		return report;
	}

	
//	등록권 구매
	@PostMapping("/Payment/PaymentResult/{quantity}")
	public boolean purchasePropertyListing(Authentication authentication, @PathVariable int quantity) {
		int price = 5500;
		PropertyListing propertyListing = new PropertyListing();

		// 유저 번호
		String userName = authentication.getName();
		int userNumber = agentService.getUserIdByUserName(userName);
		
		boolean hasPropertyListing = propertyService.checkPropertyCondition(userNumber); // 유저가 이전에 결제한 적 있는지, 있다면 남아있는 개수가 있는지
		
		log.info(hasPropertyListing + "");
		if (!hasPropertyListing) {
			propertyListing.setPlquantity(quantity);
			propertyListing.setPlremain(quantity);
			propertyListing.setPlUnumber(userNumber);
			if (quantity > 1) {
				price = quantity * 5500 - (500 * quantity);
				propertyListing.setPlprice(price);
			} else {
				propertyListing.setPlprice(price);
			}
			log.info(propertyListing.toString());
			propertyService.purchasePropertyListing(propertyListing);
			return true; // 등록권이 없는 유저나 처음 구매하는 유저라면 true
		} else {
			log.info("이미 등록권이 존재합니다.");
			return false; // 아직 등록권이 존재하는 유저라면 false를 리턴
		}
	}
}