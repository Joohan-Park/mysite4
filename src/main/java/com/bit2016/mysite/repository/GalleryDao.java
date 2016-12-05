package com.bit2016.mysite.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bit2016.mysite.vo.GalleryVo;

@Repository
public class GalleryDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public int upload(GalleryVo galleryVo){
		return sqlSession.insert("gallery.upload",galleryVo);
	}
	
	public List<GalleryVo> getList() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		return sqlSession.selectList("gallery.getList",map);
	}
	
}
