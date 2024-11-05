package com.ict.edu05.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ict.edu05.service.EmpService;
import com.ict.edu05.vo.EmpVO;

@Controller
public class EmpController {
	@Autowired
	private EmpService empService;
	
	@RequestMapping("/emp")
	public ModelAndView emp_form() {
		ModelAndView mv = new ModelAndView("day05/emp_form");
		return mv;
	}
	
	@PostMapping("/emp_getlist")
	public ModelAndView emp_list() {
		try {
			ModelAndView mv = new ModelAndView("day05/emp_list");
			// list를 중복으로 쓸 수 있는 이유는 덮어쓰기 때문에 중복으로 사용 가능
			List<EmpVO> list = empService.getList();
			mv.addObject("list", list); // list에 넣기
			return mv;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	@PostMapping("/emp_search")
	// 파라미터 있는 값: @RequestParam("deptno") String deptno
	public ModelAndView emp_search(@RequestParam("deptno") String deptno) {
		try {
			ModelAndView mv = new ModelAndView("day05/emp_search");
			List<EmpVO> list = empService.getSearch(deptno);
			mv.addObject("list", list); // list에 넣기
			return mv;
		} catch (Exception e) {
			System.out.println();
			return null;
		}
	}
	
	/*
	// 파라미터가 vo에 존재하지 않을 경우 처리 방법
	// 1. vo에 넣는다.
	// 2. 별도로 받아서 나중에 Map으로 처리한다.
	@PostMapping("/emp_dynamic_search")
	public ModelAndView emp_dynamic_search(EmpVO empvo) {
		try {
			ModelAndView mv = new ModelAndView("day05/emp_dynamic");
			List<EmpVO> list = empService.getSearch(empvo);
			mv.addObject("list", list); // list에 넣기
			mv.addObject("idx", empvo.getIdx()); // idx 넘기기
			return mv;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	} */
	
	// 다음 jsp에 받은 idx를 그대로 넘기기 위해서 => @ModelAttribute("idx") String idx
	@PostMapping("emp_dynamic_search")
	public ModelAndView emp_dynamic_search(
			@ModelAttribute("idx") String idx,
			@RequestParam("keyword") String keyword) {
		try {
			ModelAndView mv = new ModelAndView("day05/emp_dynamic");
			List<EmpVO> list = empService.getSearch(idx, keyword);
			mv.addObject("list", list); // list에 넣기
			return mv;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
}