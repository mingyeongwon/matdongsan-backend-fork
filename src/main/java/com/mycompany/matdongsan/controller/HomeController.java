package com.mycompany.matdongsan.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.matdongsan.dao.UserCommonDataDao;
import com.mycompany.matdongsan.dto.Agent;
import com.mycompany.matdongsan.dto.Member;
import com.mycompany.matdongsan.dto.UserCommonData;
import com.mycompany.matdongsan.security.AppUserDetails;
import com.mycompany.matdongsan.security.AppUserDetailsService;
import com.mycompany.matdongsan.security.JwtProvider;
import com.mycompany.matdongsan.service.AgentService;
import com.mycompany.matdongsan.service.MemberService;
import com.mycompany.matdongsan.service.PropertyService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HomeController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private AppUserDetailsService userDetailsService;
	@Autowired
	private UserCommonDataDao userCommonDataDao;
	@Autowired
	private PropertyService propertyService;
	
	@GetMapping("/")
	public String home() {
		log.info("실행");
		return "restapi";
	}

	

}