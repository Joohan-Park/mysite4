package com.bit2016.mysite.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bit2016.mysite.service.UserService;
import com.bit2016.mysite.vo.UserVo;
import com.bit2016.security.Auth;
import com.bit2016.security.AuthUser;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/joinform")
	public String joinForm(@ModelAttribute UserVo userVo){
		return "/user/joinform";
	}
	
	@RequestMapping("/loginform")
	public String loginForm(){
		return "/user/loginform";
	}
	
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join(@ModelAttribute @Valid UserVo userVo, BindingResult result, Model model){
		
		if(result.hasErrors()){
			model.addAllAttributes(result.getModel());
			return "/user/joinform";
		}
		
		userService.join(userVo);
		return "redirect:/user/joinsuccess";
	}
	
	@RequestMapping("/joinsuccess")
	public String joinSuccess(){
		return "/user/joinsuccess";
	}	
	
	@Auth
	@RequestMapping("/modifyform")
	public String modifyForm( @AuthUser UserVo authUser, Model model){
		UserVo vo = userService.getUser(authUser.getNo());
		model.addAttribute("userVo",vo);
		return "/user/modifyform";
	}
	
	@Auth
	@RequestMapping("/modify")
	public String modify(@AuthUser UserVo authUser, @ModelAttribute UserVo vo){
		
		vo.setNo(authUser.getNo());
		userService.updateUser(vo);
		
		authUser.setName(vo.getName());
		
		return "redirect:/user/modifyform?update=success";
	}
	
//	
//	@ExceptionHandler(UserDaoException.class)
//	public String handlerUserDaoException(){
//		
//		// 1. logging(파일에 내용 저장)
//		// 2. 사용자에게 안내(error) 페이지
//		return "/error/500";
//	}
//	
	
}