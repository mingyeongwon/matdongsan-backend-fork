package com.mycompany.matdongsan.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.matdongsan.dto.Member;
import com.mycompany.matdongsan.dto.UserCommonData;
import com.mycompany.matdongsan.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	// 로그인

	// 회원가입
	// 참고 블로그
	// https://velog.io/@tjddnths0223/%ED%8C%81-RequestBody%EB%A1%9C-%EC%97%AC%EB%9F%AC-%EA%B0%9D%EC%B2%B4-%EB%B0%9B%EA%B8%B0
	@Transactional
	@PostMapping("/Signup/MemberSignup")
	public UserCommonData joinByMember(@RequestParam("uemail") String uemail, @RequestParam("urole") String urole,
			@RequestParam("upassword") String upassword, @RequestParam("mname") String mname,
			@RequestParam("mphone") String mphone,
			@RequestParam(value = "mprofile", required = false) MultipartFile mprofile) throws IOException {

		UserCommonData userEmail = new UserCommonData();
		userEmail.setUemail(uemail);
		userEmail.setUrole(urole);
		userEmail.setUpassword(upassword);
		userEmail.setUremoved(false);

		// 비밀번호 암호화
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		userEmail.setUpassword(passwordEncoder.encode(userEmail.getUpassword()));

		// 권한 설정

		Member member = new Member();
		member.setMname(mname);
		member.setMphone(mphone);

		memberService.joinUserByMember(userEmail);
		member.setMUnumber(userEmail.getUnumber()); // FK 값 주기

		// 프로필 사진 첨부 여부
		if (mprofile != null && !mprofile.isEmpty()) {
			member.setMprofileoname(mprofile.getOriginalFilename());
			member.setMprofiletype(mprofile.getContentType());
			member.setMprofiledata(mprofile.getBytes());
		}
		memberService.joinByMember(member);

		// JSON으로 변환되지 않는 필드는 null 처리
		userEmail.setUpassword(null);
		member.setMprofile(null);
		member.setMprofiledata(null);

		return userEmail;
	}

	//일반 유저 프로필 사진
	@GetMapping("/mattach/{mnumber}")
	public void downloadAgentProfile(@PathVariable int mnumber, HttpServletResponse response) {
		
		log.info("백엔드 사진 실행");
		// 해당 게시물 가져오기
		Member memeber = memberService.getMemberDataByMemberNumber(mnumber);
		// 파일 이름이 한글일 경우, 브라우저에서 한글 이름으로 다운로드 받기 위한 코드
		if(memeber.getMprofileoname() !=null) {
			try {
				String fileName = new String(memeber.getMprofileoname().getBytes("UTF-8"), "ISO-8859-1");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
				// 파일 타입을 헤더에 추가
				response.setContentType(memeber.getMprofiletype());
				// 응답 바디에 파일 데이터를 출력
				OutputStream os = response.getOutputStream();
				os.write(memeber.getMprofiledata());
				os.flush();
				os.close();
			} catch (IOException e) {
				log.error(e.toString());
			}
		}
	
	}
	
	// 일반 멤버 정보 업데이트
		@Transactional
		@PutMapping("/Mypage/MyInfomation")
		public void updateMypagePropertyInfo(@ModelAttribute Member memberData,
				Authentication authentication) {
			Member member =  memberData;
			// 프로필사진 & 등록증 사진
			if (member.getMprofile() != null) {
				MultipartFile memberProfile = member.getMprofile();

				// 파일 이름을 설정
				member.setMprofileoname(memberProfile.getOriginalFilename());
				// 파일 종류를 설정
				member.setMprofiletype(memberProfile.getContentType());
				try {
					// 파일 데이터를 설정
					member.setMprofiledata(memberProfile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			int memberNum = memberService.getMemberNumberByMemberEmail(authentication.getName()); // 멤버 넘버
			member.setMnumber(memberNum);
			// 수정하기
			memberService.updateMemberData(member);
		}

	// 비밀번호 수정

	//
}
