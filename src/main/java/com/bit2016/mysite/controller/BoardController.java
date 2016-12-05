package com.bit2016.mysite.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bit2016.mysite.service.BoardService;
import com.bit2016.mysite.vo.BoardVo;
import com.bit2016.mysite.vo.UserVo;
import com.bit2016.security.Auth;
import com.bit2016.web.util.WebUtil;

@Controller
@RequestMapping( "/board" )
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@RequestMapping( "" )
	public String index(
		@RequestParam( value="p", required=true, defaultValue="1") Integer page,
		@RequestParam( value="kwd", required=true, defaultValue="") String keyword,
		Model model ) {
		
		Map<String, Object> map = 
				boardService.getList( page, keyword );
		
		model.addAttribute( "map", map );
		return "board/list";
	}

	
	@Auth
	@RequestMapping( value="/write", method=RequestMethod.GET )
	public String write() {	
		return "board/write";
	}
	
	@RequestMapping( value="/write", method=RequestMethod.POST )
	public String write( 
			HttpSession session, 
			@ModelAttribute BoardVo vo,
			@RequestParam( value="p", required=true, defaultValue="1") Integer page,
			@RequestParam( value="kwd", required=true, defaultValue="") String keyword
			) {
		
		UserVo authUser = (UserVo)session.getAttribute( "authUser" );
		// 권한 체크
		if( authUser == null ){
			return "redirect:/user/loginform";
		}
			
		vo.setUserNo( authUser.getNo() );
		boardService.write( vo );
		
		return "redirect:/board" +
			   "?p=" + page +
			   "&kwd=" + WebUtil.encodeURL(keyword, "UTF-8");
	}
	
	@RequestMapping("/view")
	public String view(
			@RequestParam( value="no", required=true, defaultValue="0") Long no,
			@RequestParam( value="p", required=true, defaultValue="1") Integer page,
			@RequestParam( value="kwd", required=true, defaultValue="") String keyword,
			Model model ){
		
		BoardVo vo = boardService.view(no);
		
		model.addAttribute("boardVo",vo);
		model.addAttribute("page",page);
		model.addAttribute("keyword",keyword);
			
		return "board/view";
	}
	
	@Auth
	@RequestMapping(value="/modify", method = RequestMethod.GET)
	public String modify(
			@RequestParam( value="no", required=true, defaultValue="0") Long no,
			@RequestParam( value="p", required=true, defaultValue="1") Integer page,
			@RequestParam( value="kwd", required=true, defaultValue="") String keyword,
			Model model){
		
		BoardVo boardVo = boardService.view(no);
		model.addAttribute("boardVo",boardVo);
		model.addAttribute("page",page);
		model.addAttribute("keyword",keyword);
		
		return "board/modify";
	}
	
	@Auth
	@RequestMapping(value="/modify", method = RequestMethod.POST)
	public String modify(
			@RequestParam( value="p", required=true, defaultValue="1") Integer page,
			@RequestParam( value="kwd", required=true, defaultValue="") String keyword,
			HttpSession session,
			@ModelAttribute BoardVo boardVo
			){
		
		UserVo authUser = (UserVo)session.getAttribute( "authUser" );
		if(authUser == null){
			return "redirct:/user/loginform";
		}
		System.out.println(boardVo.toString());
		boardVo.setUserNo(authUser.getNo());
		boardService.modify(boardVo);
		return "redirect:/board/view" + 
				"?no=" + boardVo.getNo() +
				"&kwd=" + WebUtil.encodeURL(keyword,"UTF-8");
	}
	
	@Auth
	@RequestMapping(value="/reply", method=RequestMethod.GET)
	public String replyform(
			@RequestParam( value="no", required=true, defaultValue="0") Long no,
			@RequestParam( value="p", required=true, defaultValue="1") Integer page,
			@RequestParam( value="kwd", required=true, defaultValue="") String keyword,
			HttpSession session,
			Model model ){
		
		BoardVo vo = boardService.view(no);
		model.addAttribute("boardVo",vo);
		model.addAttribute("page",page);
		model.addAttribute("keyword",keyword);
		return "board/reply";
	}
	
	@RequestMapping("/delete")
	public String delete(
			@RequestParam( value="no", required=true, defaultValue="0") Long no,
			@RequestParam( value="p", required=true, defaultValue="1") Integer page,
			@RequestParam( value="kwd", required=true, defaultValue="") String keyword,
			HttpSession session,
			@ModelAttribute BoardVo boardVo
			){
		
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser==null){
			return "redirect:/user/loginform";
		}
		boardVo.setNo(no);
		boardVo.setUserNo(authUser.getNo());
		boardService.delete(boardVo);
		
		return "redirect:/board" +
				"?p=" + page +
				"&kwd=" + WebUtil.encodeURL(keyword, "UTF-8");
	}
}