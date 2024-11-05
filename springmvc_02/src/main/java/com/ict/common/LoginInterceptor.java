package com.ict.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {
	// preHandle, postHandle, afterCompletion
	
	// 반환형은 boolean형
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 결과 true이면 controller, false이면 jsp로 이동
		
		// 로그인하면 세션에 로그인 성공했다는 정보를 담거나 사용자 정보를 담자
		// 로그인 체크를 해서 만약에 로그인이 안 된 상태이면 value에 false 저장
		// request에서 세션 정보 가져오기
		
		// 만약에 session이 삭제된 상태(브라우저 창 닫으면)라면 새로운 session을 생성해 준다. -> 로그인 체크하려고 하면 세션이 필요하다.
		// session이 삭제가 되지 않은 상태라면 사용하고 있는 그대로 전달해 준다.(기존 session 호출)
		Object obj = request.getSession(true).getAttribute("loginchk");

		if (obj == null) { // 로그인 안 된 상태
			// 자바스크립트 코드 작성
			String script = "<script>alert('로그인이 필요합니다.'); location.href='/sns_login';</script>";
			// response.sendRedirect("WEB-INF/views/sns/loginForm.jsp"); // 들어온 게 싹 다 날라감, jsp로 보내줌
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().write(script);
			
			return false;
		}
		return true;
	}
	
	/*
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	*/
}
