package com.bit2016.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.bit2016.mysite.vo.UserVo;

public class AuthUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public Object resolveArgument(
			MethodParameter parameter, 
			ModelAndViewContainer mavContainer, 
			NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) 
					throws Exception {
		if(this.supportsParameter(parameter)==false){
			return WebArgumentResolver.UNRESOLVED;
		}
		
		HttpServletRequest httpServletRequest =
				webRequest.getNativeRequest(HttpServletRequest.class);
		HttpSession sesstion = httpServletRequest.getSession();
		
		UserVo authUser = (UserVo)sesstion.getAttribute("authUser");
		return authUser;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		
		AuthUser authUser = parameter.getParameterAnnotation(AuthUser.class);
		if(authUser == null){
			// @AuthUser 이 안붙어 있음
			return false;
		}
		
		if(parameter.getParameterType().equals(UserVo.class) == false){
			return false;
		}
		
		return true;
	}

}
