package com.bit2016.mysite.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bit2016.mysite.exception.UserDaoException;
import com.bit2016.mysite.vo.UserVo;

@Repository
public class UserDao {
	@Autowired
	private SqlSession sqlSession;
	
	// 인증(로그인)
	public UserVo get(String email, String password) 
		throws UserDaoException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("email", email);
		map.put("password",password);
		
		return sqlSession.selectOne("user.getByEmailAndPassword", map);
	}
	
	// 이메일 체크
	public UserVo get(String email){
		return sqlSession.selectOne("user.getByEmail", email);
	}
	
	// 사용자 가져오기
	public UserVo get(Long no) {
		return sqlSession.selectOne("user.getByNo", no);
	}
	
	// 회원가입
	public void insert( UserVo vo ) {
		sqlSession.insert( "user.insert" , vo );
	}
	
	public void update( UserVo vo ) {
		sqlSession.update("user.update" , vo);
	}	
}
