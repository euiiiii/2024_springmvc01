package com.ict.guestbook2.vo;

import org.springframework.web.multipart.MultipartFile;

public class ImgVO {
	private MultipartFile s_file; // ajax와 연결되어 있음

	public MultipartFile getS_file() {
		return s_file;
	}

	public void setS_file(MultipartFile s_file) {
		this.s_file = s_file;
	}
}