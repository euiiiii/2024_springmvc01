package com.ict.member.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ict.member.service.MemberService;
import com.ict.member.vo.MemberVO;


@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@PostMapping("/member_login")
	public ModelAndView getMemberLogin(MemberVO memvo, HttpSession session) {
		// Exception이 있어서 try catch 처리해줘야 함 => 꼭 필수적으로 해야함
		try {
			ModelAndView mv = new ModelAndView();
			MemberVO memvo2 = memberService.getMemberLogin(memvo.getM_id());
			
			if (memvo2 == null) { // 아이디 없으면
				return new ModelAndView("sns/login_error");
			} else { // 아이디 있으면
				if (passwordEncoder.matches(memvo.getM_pw(), memvo2.getM_pw())) { // 암호화X-암호화O 일치 시
					// 성공
					// 일반유저와 관리자 구분
					session.setAttribute("loginchk", "ok"); // 세션에 loginchk - ok를 가지고 있음
					if (memvo2.getM_id().equals("admin")) { // 아이디가 admin인 경우
						session.setAttribute("admin", "ok"); // 세션에 admin - ok를 가지고 있음
					}
					mv.setViewName("redirect:/shop");
					// mv.addObject("memvo2", memvo2); // request라 페이지 바뀌면 사라짐
					session.setAttribute("memvo2", memvo2); // 아이디라도 넣어줘야 함
					return mv;
				} else { // 암호화X-암호화O 불일치 시
					// 비밀번호가 불일치해서 실패
					return new ModelAndView("sns/login_error");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	@GetMapping("/member_logout")
	public ModelAndView getMemberLogout(HttpSession session) {
		// 세션 초기화
		session.invalidate(); // 세션 전체 삭제
		
		// 필요한 정보만 세션 삭제
		/*
		session.removeAttribute("loginchk");
		session.removeAttribute("admin");
		session.removeAttribute("memvo2");
		*/
		
		return new ModelAndView("redirect:/shop");
	}
}
















