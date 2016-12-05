package com.bit2016.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit2016.mysite.repository.GuestbookDao;
import com.bit2016.mysite.vo.GuestbookVo;

@Service
public class GuestbookService {
	
	@Autowired
	private GuestbookDao guestbookDao;
	
	public List<GuestbookVo> getMessageList() {
		return guestbookDao.getList();
	}
	
	public List<GuestbookVo> getMessageList(int page) {
		
		return guestbookDao.getList(page);
	}
	
	public void deleteMessage( GuestbookVo vo ){
		guestbookDao.delete( vo );
	}
	
	public void writeMessage( GuestbookVo vo ) {
		Long no = guestbookDao.insert(vo);
		System.out.println(no);
		//get 만들기 guestbookDao.get();
	}
	
	public GuestbookVo writeMessage2( GuestbookVo vo ) {
		Long no = guestbookDao.insert(vo);
		System.out.println(no);
		//get 만들기 guestbookDao.get();
		
		return null;
	}
}
