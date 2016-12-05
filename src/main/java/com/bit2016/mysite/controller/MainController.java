package com.bit2016.mysite.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	
	private static final Log log = LogFactory.getLog(MainController.class);
	
	@RequestMapping("")
	public String index(){
		
		return "main/index";
	}
	
	@ResponseBody
	@RequestMapping("/hello")
	public String hello(){
		log.debug("MainController.hello() called.....");
		return "안녕 JRebel";
	}
	
	
}
