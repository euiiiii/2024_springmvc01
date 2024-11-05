package com.ict.guestbook2.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ict.guestbook2.vo.ImgVO;

@RestController
public class SummernoteAjaxController {
	
	@RequestMapping(value="/saveImg", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> saveImg(ImgVO imgVO, HttpServletRequest request) { // request는 이미지 찾아갈 때 필요
		try {
			Map<String, String> map = new HashMap<String, String>();
			MultipartFile file = imgVO.getS_file();
			String fname = null;
			if(file.getSize() > 0) {
				String path = request.getSession().getServletContext().getRealPath("/resources/upload");
				UUID uuid = UUID.randomUUID();
				fname = uuid.toString()+"_"+file.getOriginalFilename();
				file.transferTo(new File(path, fname));
			}
			// 파일 업로드 여부 확인 경로
			map.put("path", "resources/upload");
			map.put("fname", fname);
			return map;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
}