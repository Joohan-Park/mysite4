package com.bit2016.mysite.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handlerException(HttpServletRequest request, Exception e){
		//1. 로깅
		System.out.println("exception: "+e);
		
		
		//2. ajax 요청여부 판단
		//if("application/json".equals(request.getContentType())){
		//}
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("exceptionMessage",e.getMessage());
		mav.setViewName("error/exception");
		return mav;
	}
}
