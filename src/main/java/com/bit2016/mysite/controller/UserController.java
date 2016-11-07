package com.bit2016.mysite.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bit2016.mysite.exception.UserDaoException;
import com.bit2016.mysite.service.UserService;
import com.bit2016.mysite.vo.UserVo;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/joinform")
	public String joinForm(){
		return "/user/joinform";
	}
	
	@RequestMapping("/loginform")
	public String loginForm(){
		return "/user/loginform";
	}
	
	@RequestMapping("/join")
	public String join(@ModelAttribute UserVo vo){
		userService.join(vo);
		return "redirect:/user/joinsuccess";
	}
	
	@RequestMapping("/login")
	public String login(@RequestParam(value="email",required=true, defaultValue="") String email , 
						@RequestParam(value="password",required=true, defaultValue="") String password,
						HttpSession session){
		UserVo userVo = userService.login(email, password);
		if(userVo == null){
			return "redirect:/user/loginform?result=fail";
		}
		
		System.out.println(userVo.toString());
		
		// 인증 처리
		session.setAttribute("authUser", userVo);
		return "redirect:/";
	}
	
	@RequestMapping("/joinsuccess")
	public String joinSuccess(){
		return "/user/joinsuccess";
	}	
	
	@RequestMapping("/logout")
	public String logout( HttpSession session){
		session.removeAttribute("authUser");
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping("/modifyform")
	public String modifyForm( HttpSession session, Model model){
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		//접근제한
		if(authUser == null){
			return "redirect:/user/loginform";
		}
		
		UserVo vo = userService.getUser(authUser.getNo());
		model.addAttribute("userVo",vo);
		return "/user/modifyform";
	}
	
	@RequestMapping("/modify")
	public String modify(HttpSession session, @ModelAttribute UserVo vo){
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		//접근 제한
		if(authUser == null){
			return "redirect:/user/loginform";
		}
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