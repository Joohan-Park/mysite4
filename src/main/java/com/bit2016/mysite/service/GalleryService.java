package com.bit2016.mysite.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bit2016.mysite.repository.GalleryDao;
import com.bit2016.mysite.vo.GalleryVo;

@Service
public class GalleryService {
	
	@Autowired
	private GalleryDao galleryDao; 
	
	private static final String SAVE_PATH = "/upload";
	private static final String URL = "/gallery/assets/";
	
	public Map<String, Object> getList(){
		
		List<GalleryVo> list = galleryDao.getList();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		for(GalleryVo vo : list){
			System.out.println(vo.getNo());
			System.out.println(vo.getSaveFileName());
			System.out.println(vo.getUserNo());
			System.out.println(vo.getOriginalFileName());
			System.out.println(vo.getFileSize());
			System.out.println(vo.getRegDate());
			System.out.println(vo.getComments());
			System.out.println(vo.getFileExtName());
		}
		
		
		map.put("list", list);
		
		return map;
	}
	
	public String restore(MultipartFile multipartFile,GalleryVo vo){
		String url = "";
		
		try{
		if(multipartFile.isEmpty()==true){
			return url;
		}
		String originalFileName = multipartFile.getOriginalFilename();
		String fileExtName = originalFileName.substring(originalFileName.lastIndexOf('.')+1, originalFileName.length());
		String saveFileName = generateSaveFileName(fileExtName);
		Long fileSize = multipartFile.getSize();
		
		System.out.println("########"+originalFileName);
		System.out.println("########"+saveFileName);
		System.out.println("########"+fileExtName);
		System.out.println("########"+fileSize);
		System.out.println("########"+vo.getNo());
		
		vo.setOriginalFileName(originalFileName);
		vo.setSaveFileName(saveFileName);
		vo.setFileExtName(fileExtName);
		vo.setFileSize(fileSize);
		vo.setComments("--");
		
		writeFile(multipartFile, saveFileName);
		
		galleryDao.upload(vo);
		
		url = URL + saveFileName;
		
		} catch (IOException e){
			//log 남기기
			throw new RuntimeException("upload file");
		}
		return url;
	}
	
	private void writeFile(MultipartFile multipartFile, String saveFileName) throws IOException{
		byte[] fileData = multipartFile.getBytes();
		FileOutputStream fos = new FileOutputStream(SAVE_PATH + "/" + saveFileName);
		
		fos.write(fileData);
		fos.close();
	}
	
	private String generateSaveFileName(String extName){
		String fileName = "";
		Calendar calendar = Calendar.getInstance();
		fileName += calendar.get(Calendar.YEAR);
		fileName += calendar.get(Calendar.MONTH);
		fileName += calendar.get(Calendar.DATE);
		fileName += calendar.get(Calendar.HOUR);
		fileName += calendar.get(Calendar.MINUTE);
		fileName += calendar.get(Calendar.SECOND);
		fileName += calendar.get(Calendar.MILLISECOND);
		fileName += ("." + extName);
		return fileName;
	}
}
