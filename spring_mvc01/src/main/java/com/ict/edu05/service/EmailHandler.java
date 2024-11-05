package com.ict.edu05.service;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

// Service에서 호출해서 사용할 클래스
public class EmailHandler {
	// 생성자에서 받는 건 전역변수로 빼라
	private JavaMailSender javaMailSender;
	private MimeMessage message;
	private MimeMessageHelper messageHelper; // 메세지를 좀 더 편하게 해주는 것
	
	public EmailHandler(JavaMailSender javaMailSender) throws Exception {
		this.javaMailSender = javaMailSender; // 전역변수로 빼주는 코드
		
		message = this.javaMailSender.createMimeMessage();
		// true => 멀티파트 메세지를 사용 가능(파일O)
		messageHelper = new MimeMessageHelper(message,true,"utf-8");
		
		// 단순한 텍스트 메세지만 사용(파일X)
		// messageHelper = new MimeMessageHelper(message,"utf-8");
	}
	
	// 제목
	public void setSubject(String subject) throws Exception{
		messageHelper.setSubject(subject);
	}
	
	// 내용
	public void setText(String text) throws Exception{
		// true => 태그 사용 가능
		messageHelper.setText(text, true);
	}
	
	// 보내는 사람의 이메일과 제목
	public void setFrom(String email, String name) throws Exception{
		messageHelper.setFrom(email, name);
	}
	
	// 받는 사람의 이메일
	public void setTo(String email) throws Exception{
		messageHelper.setTo(email);
	}
	
	// 보내기
	public void send() {
		javaMailSender.send(message);
	}
}