package com.ict.guestbook2.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.OS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ict.guestbook2.service.GuestBook2Service;
import com.ict.guestbook2.vo.GuestBook2VO;

@Controller
public class GuestBook2Controller {

	@Autowired
	private GuestBook2Service guestBook2Service;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/gb2_list")
	public ModelAndView guestbook2List() {
		ModelAndView mv = new ModelAndView("guestbook2/list");
		List<GuestBook2VO> gb2_list = guestBook2Service.getGuestBook2List();
		mv.addObject("gb2_list", gb2_list);
		return mv;
	}

	@GetMapping("/gb2_write")
	public ModelAndView guestbook2Write() {
		ModelAndView mv = new ModelAndView("guestbook2/write");
		return mv;
	}

	// 파라미터 받을 때: vo, @RequestParam, HttpServletRequest
	@GetMapping("/gb2_detail")
	public ModelAndView guestbook2Detail(@RequestParam("gb2_idx") String gb2_idx) {
		ModelAndView mv = new ModelAndView("guestbook2/onelist");
		GuestBook2VO gb2vo = guestBook2Service.getGuestBook2Detail(gb2_idx);
		if (gb2vo != null) {
			mv.addObject("gb2VO", gb2vo);
			return mv;
		}
		return new ModelAndView("guestbook2/error");
	}

	@RequestMapping("/gb2_write_ok")
	public ModelAndView guestbook2WriteOK(GuestBook2VO gb2vo, HttpServletRequest request) {
		try {
			ModelAndView mv = new ModelAndView("redirect:/gb2_list");

			// 파일 정보
			// System.out.println("파일정보: " +
			// gb2vo.getGb2_file_name().getOriginalFilename());
			// summernote 정보까지
			// System.out.println("썸머노트정보: " + gb2vo.getGb2_content());

			// 파일 업로드
			String path = request.getSession().getServletContext().getRealPath("/resources/upload");
			MultipartFile file = gb2vo.getGb2_file_name();

			if (file.isEmpty()) {
				gb2vo.setGb2_f_name("");
			} else {
				UUID uuid = UUID.randomUUID();
				String f_name = uuid.toString() + "_" + file.getOriginalFilename();
				gb2vo.setGb2_f_name(f_name);
				// 업로드
				file.transferTo(new File(path, f_name));
			}

			// 비밀번호 암호화
			String pwd = passwordEncoder.encode(gb2vo.getGb2_pw());
			gb2vo.setGb2_pw(pwd); // pwd를 setGb2_pw에 저장
			
			int result = guestBook2Service.getGuestBook2Insert(gb2vo); // 삽입
			if (result > 0) {
				return mv;
			}
			return new ModelAndView("guestbook2/error");
		} catch (Exception e) {
			System.out.println(e);
			return new ModelAndView("guestbook2/error");
		}
	}

	// 파일 다운로드
	@GetMapping("guestbook2_down")
	public void guestBook2Down(HttpServletRequest request, HttpServletResponse response) {
		try {
			String f_name = request.getParameter("f_name");
			String path = request.getSession().getServletContext().getRealPath("/resources/upload/" + f_name);
			String r_path = URLEncoder.encode(f_name, "UTF-8");

			// 브라우저 설정 => response
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment; filename=" + r_path);

			File file = new File(new String(path.getBytes(), "utf-8"));
			FileInputStream in = new FileInputStream(file);

			OutputStream out = response.getOutputStream();

			FileCopyUtils.copy(in, out);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@PostMapping("/gb2_delete")
	public ModelAndView guestbook2Delete(@ModelAttribute("gb2_idx") String gb2_idx) { // ModelAttribute는 여기에서도 쓰고
																						// delete.jsp 갈 때 가지고 감
		return new ModelAndView("guestbook2/delete");
	}

	@PostMapping("/gb2_delete_ok")
	public ModelAndView guestbook2DeleteOK(GuestBook2VO gb2vo) {
		ModelAndView mv = new ModelAndView();

		// 비밀번호가 일치 여부 검사
		GuestBook2VO gb2vo2 = guestBook2Service.getGuestBook2Detail(gb2vo.getGb2_idx());

		// jsp에서 입력한 password(암호화X)
		String pw = gb2vo.getGb2_pw();
		// DB에서 가져온 password(암호화O)
		String pw2 = gb2vo2.getGb2_pw();

		if (passwordEncoder.matches(pw, pw2)) {
			int result = guestBook2Service.getGuestBook2Delete(gb2vo.getGb2_idx());
			if (result > 0) {
				mv.setViewName("redirect:/gb2_list");
				return mv;
			}
		} else {
			mv.setViewName("guestbook2/delete");
			mv.addObject("pwdchk", "fail");
			mv.addObject("gb2_idx", gb2vo.getGb2_idx());
			return mv;
		}
		return new ModelAndView("guestbook2/delete");
	}

	@PostMapping("/gb2_update")
	public ModelAndView guestbook2Update(@RequestParam("gb2_idx") String gb2_idx) {
		ModelAndView mv = new ModelAndView("guestbook2/update");
		GuestBook2VO gb2vo = guestBook2Service.getGuestBook2Detail(gb2_idx);
		if (gb2vo != null) {
			mv.addObject("gb2VO", gb2vo);
			return mv;
		}
		return new ModelAndView("guestbook2/error");
	}

	@PostMapping("/gb2_update_ok")
	public ModelAndView guestbook2UpdateOK(GuestBook2VO gb2vo, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		// 비밀번호가 일치 여부 검사
		GuestBook2VO gb2vo2 = guestBook2Service.getGuestBook2Detail(gb2vo.getGb2_idx());

		// jsp에서 입력한 password(암호화X)
		String pw = gb2vo.getGb2_pw();
		// DB에서 가져온 password(암호화O)
		String pw2 = gb2vo2.getGb2_pw();

		if (passwordEncoder.matches(pw, pw2)) {
			try {
				// 첨부파일 유무
				String path = request.getSession().getServletContext().getRealPath("/resources/upload");
				MultipartFile file = gb2vo.getGb2_file_name();
				String old_f_name = gb2vo.getOld_f_name();
				
				if (file.isEmpty()) {
					gb2vo.setGb2_f_name(old_f_name);
				} else {
					UUID uuid = UUID.randomUUID();
					String f_name = uuid.toString()+"_"+file.getOriginalFilename();
					gb2vo.setGb2_f_name(f_name);
					
					// 파일 업로드
					file.transferTo(new File(path, f_name));
				}
				
				// 비밀번호 암호화
				String pwd = passwordEncoder.encode(gb2vo.getGb2_pw());
				gb2vo.setGb2_pw(pwd);
				
				int result = guestBook2Service.getGuestBook2Update(gb2vo);
				if (result > 0) {
					mv.setViewName("redirect:/gb2_detail?gb2_idx="+gb2vo.getGb2_idx());
					return mv;
				}
				return new ModelAndView("guestbook2/error");
			} catch (Exception e) {
				System.out.println(e);
				return new ModelAndView("guestbook2/error");
			}
		} else {
			mv.setViewName("guestbook2/update");
			mv.addObject("pwdchk", "fail");
			mv.addObject("gb2VO", gb2vo2); // 내용 다시 입력해라.
			return mv;
		}
	}
}