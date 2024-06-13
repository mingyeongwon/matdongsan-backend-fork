package com.mycompany.matdongsan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	@GetMapping("/")
	public String home() {
		log.info("실행");
		return "restapi";
	}
	
}
