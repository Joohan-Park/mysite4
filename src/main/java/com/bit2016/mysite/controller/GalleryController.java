package com.bit2016.mysite.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bit2016.mysite.service.GalleryService;
import com.bit2016.mysite.service.UserService;
import com.bit2016.mysite.vo.GalleryVo;
import com.bit2016.mysite.vo.UserVo;
import com.bit2016.security.Auth;
import com.bit2016.security.AuthUser;



@Controller
@RequestMapping("/gallery")
public class GalleryController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GalleryService galleryService;
	
	@RequestMapping("")
	public String index(Model model){
		
		Map<String, Object> map = 
				galleryService.getList();
		
		model.addAttribute( "map", map );
		
		return "gallery/index";
	}
	
	@Auth
	@RequestMapping("/form")
	public String form(
			@AuthUser UserVo authUser,
			Model model
		){
		UserVo userVo = userService.getUser(authUser.getNo());
		model.addAttribute("userVo",userVo);
		return "gallery/form";
	}
	
	@Auth
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public String upload(
			@RequestParam(value="file1")  MultipartFile file1,
			@AuthUser UserVo authUser,
			Model model,
			@ModelAttribute GalleryVo vo
			){
		
		vo.setUserNo(authUser.getNo());
		
		System.out.println(file1);
		
		String saveFileName = galleryService.restore(file1,vo);
		
		System.out.println("@@@@@@@@@@"+saveFileName);
		
		model.addAttribute("url1",saveFileName);
		
		return "gallery/result";
	}
}
