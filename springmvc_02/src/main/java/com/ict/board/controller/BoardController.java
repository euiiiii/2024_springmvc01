package com.ict.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.ict.board.service.BoardService;
import com.ict.board.vo.BoardVO;
import com.ict.common.Paging;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private Paging paging;
	
	@RequestMapping("/board_list")
	public ModelAndView boardList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("board/list");
		
		// 페이징 기법
		
		// 전체 게시물의 수
		int count =  boardService.getTotalCount();
		paging.setTotalRecord(count);
		
		// 전체 페이지의 수를 구한다.
		if (paging.getTotalRecord() <= paging.getNumPerPage()) { // 게시글 6개보다 작으면
			paging.setTotalPage(1); // 6개까지는 1페이지
		} else { // 게시글 6개보다 크면
			paging.setTotalPage(paging.getTotalRecord() / paging.getNumPerPage());
			if (paging.getTotalRecord() % paging.getNumPerPage() != 0) { // 나머지가 0이 아니면
				paging.setTotalPage(paging.getTotalPage()+1); // 페이지 수 +1을 해준다.
			}
		}
		
		// 파라미터에서 넘어오는 cPage(보고 싶은 페이지번호)를 구하자
		String cPage = request.getParameter("cPage");
		
		// 만약에 cPage가 null이면 무조건 1page로 보내자
		if (cPage == null) {
			paging.setNowPage(1);
		} else {
			paging.setNowPage(Integer.parseInt(cPage)); // nowPage는 int이므로 자료형을 맞춰줘야 한다.
		}
		
		// cPage 기준으로 begin, end, beginBlock, endBlock
		// MySQL이나 MariaDB는 limit, offset을 이용하자 (begin, end는 필요 없음)
		// 오라클에선 begin, end 필요
		// offset = limit * (현재 페이지 - 1)
		// limit = numPerPage
		// select * from 테이블 order by pk desc limit 6 offset 0 | 6 | 12 | 18...
		
		paging.setOffset(paging.getNumPerPage() * (paging.getNowPage()-1)); // offset = limit * (현재 페이지 - 1)
		
		// 시작 블록, 끝 블록
		paging.setBeginBlock(
				(int)(((paging.getNowPage()-1) / paging.getPagePerBlock()) * paging.getPagePerBlock()+1));
		paging.setEndBlock(paging.getBeginBlock() + paging.getPagePerBlock()-1);
		
		// 주의 사항
		// endBloc(3,6,9...)으로 나오고 있으나 실제 가지고 있는 총 페이지는 20개일 경우 4페이지까지
		if (paging.getEndBlock() > paging.getTotalPage()) { // 끝 블록이 총 페이지보다 클 경우
			paging.setEndBlock(paging.getTotalPage()); 
		}
		
		// DB 처리
		List<BoardVO> board_list = boardService.getBoardList(paging.getOffset(), paging.getNumPerPage());
		
		if (board_list != null) {
			// 성공
			mv.addObject("board_list", board_list);
			mv.addObject("paging", paging);
			return mv;
		}
		return null;
	}
	
	@GetMapping("/board_write")
	// jsp에서 hidden으로 넘긴 cPage를 파라미터로 받을 수 있음
	public ModelAndView boardWrite(@ModelAttribute("cPage") String cPage) {
		return new ModelAndView("board/write");
	}
	
	@PostMapping("/board_write_ok")
	public ModelAndView boardWriteOk(BoardVO bovo, HttpServletRequest request) {
		try {
			ModelAndView mv = new ModelAndView("redirect:/board_list");
			
			// 파일 관련
			String path = request.getSession().getServletContext().getRealPath("/resources/upload");
			MultipartFile file = bovo.getFile_name();
			if (file.isEmpty()) {
				bovo.setF_name(""); // DB에 들어갈 것
			} else {
				UUID uuid = UUID.randomUUID();
				String f_name = uuid.toString()+"_"+file.getOriginalFilename();
				bovo.setF_name(f_name);
				
				// 업로드
				file.transferTo(new File(path, f_name));
			}
			
			// 비밀번호 암호화
			String pwd = passwordEncoder.encode(bovo.getPwd());
			bovo.setPwd(pwd); // 암호화한 걸 DB에 저장
			
			int result = boardService.getBoardInsert(bovo);
			
			if (result > 0) {
				return mv;
			}
			return null;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	@GetMapping("/board_detail")
	public ModelAndView boardDetail(@ModelAttribute("cPage") String cPage, @RequestParam("idx") String idx) {
		ModelAndView mv = new ModelAndView("board/view");
		
		// hit 업데이트
		int result = boardService.getBoardHit(idx);
		
		// 상세보기
		BoardVO bovo = boardService.getBoardDetail(idx);
		
		if (bovo != null) {
			mv.addObject("bovo", bovo);
			return mv;
		}
		return null;
	}
	
	// 다운로드
	@GetMapping("/board_down")
	public void boadDown(HttpServletRequest request, HttpServletResponse response) {
		try {
			String f_name = request.getParameter("f_name");
			String path = request.getSession().getServletContext().getRealPath("/resources/upload/"+f_name); // 찍어서 다운로드 해야하기 때문에 f_name을 먼저 받음
			String r_path = URLEncoder.encode(path, "utf-8");
			
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment; filename="+r_path);
			
			File file = new File(new String(path.getBytes(),"utf-8"));
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
			
			FileCopyUtils.copy(in, out);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@PostMapping("/ans_write")
	public ModelAndView ansWrite(@ModelAttribute("idx") String idx, @ModelAttribute("cPage") String cPage) {
		return new ModelAndView("board/ans_write");
	}
	
	@PostMapping("/board_ans_write_ok")
	public ModelAndView ansWriteOK(BoardVO bovo, HttpServletRequest request, @ModelAttribute("cPage") String cPage) {
		try {
			// 답글에서만 처리할 일들
			
			//1. 원글의 b_group, b_step, b_level을 가져오자
			// 상세보기
			BoardVO bovo2 = boardService.getBoardDetail(bovo.getIdx()); // 그룹, 스텝, 레벨이 필요해서 Idx를 가져옴
			int b_group = Integer.parseInt(bovo2.getB_group());
			int b_step = Integer.parseInt(bovo2.getB_step()); // 원글, 댓글, 댓글의 댓글, 댓글의 댓글의 댓글 ...
			int b_lev = Integer.parseInt(bovo2.getB_lev()); // step을 정렬하기 위한 것
			
			// 2. step, lev를 하나씩 증가
			b_step++;
			b_lev++;
			
			// 3. DB에 b_group을 기준으로 b_lev을 업데이트 하자
			// ** b_group이 같은 글을 찾아서 기존 데이터의 b_lev이 같거나 크면 기존 데이터의 b_lev을 증가시키자
			// 첫 번째 댓글은 b_lev 증가시키지 않고, 두 번째 댓글부터 b_lev 증가시킨다.
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("b_group", b_group); // 그룹이 같은 거에서 레벨 증가 시키기
			map.put("b_lev", b_lev);
			
			int result = boardService.getLevUpdate(map);
			
			// 4. 받은 정보와 함께 bovo에 넣고 insert 하기
			bovo.setB_group(String.valueOf(b_group));
			bovo.setB_step(String.valueOf(b_step));
			bovo.setB_lev(String.valueOf(b_lev));
			
			ModelAndView mv = new ModelAndView("redirect:/board_list");
			
			String path = request.getSession().getServletContext().getRealPath("/resources/upload");
			MultipartFile file = bovo.getFile_name();
			if (file.isEmpty()) {
				bovo.setF_name("");
			} else {
				UUID uuid = UUID.randomUUID();
				String f_name = uuid.randomUUID()+"_"+file.getOriginalFilename();
				bovo.setF_name(f_name);
				
				file.transferTo(new File(path, f_name));
			}
			
			String pwd = passwordEncoder.encode(bovo.getPwd());
			bovo.setPwd(pwd);
			
			int result2 = boardService.getAnsInsert(bovo);
			if (result2 > 0) {
				return mv;
			}
			return null;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	@PostMapping("/board_delete")
	public ModelAndView boardDelete(
			@ModelAttribute("cPage") String cPage,
			@ModelAttribute("idx") String idx) {
		return new ModelAndView("board/delete");
	}
	
	@PostMapping("/board_delete_ok")
	public ModelAndView boardDeleteOK(
			@RequestParam("pwd") String pwd,
			@ModelAttribute("idx") String idx,
			@ModelAttribute("cPage") String cPage) {
		ModelAndView mv = new ModelAndView();
		
		// 비밀번호 체크
		BoardVO bovo = boardService.getBoardDetail(idx);
		String dbpwd = bovo.getPwd();
		if(passwordEncoder.matches(pwd, dbpwd)) {
			// 글삭제
			// active 컬럼을 0 -> 1 로 변경
			int result = boardService.getBoardDelete(idx);
			if(result>0) {
				mv.setViewName("redirect:/board_list");
				return mv;
			}
		}else {
			// 비빌번호가 틀린 경우
			mv.setViewName("board/delete");
			mv.addObject("pwdchk", "fail");
			return mv;
		}
		return null;
	}
	
	@PostMapping("/board_update")
	public ModelAndView boardUpdate(
			@ModelAttribute("cPage") String cPage,
			@ModelAttribute("idx") String idx) {
		ModelAndView mv = new ModelAndView("board/update");
		BoardVO bovo = boardService.getBoardDetail(idx);
		if(bovo != null) {
			mv.addObject("bovo", bovo);
			return mv;
		}
		return null;
	}
	
	@PostMapping("/board_update_ok")
	public ModelAndView boardUpdateOK(
			@ModelAttribute("cPage") String cPage,
			@ModelAttribute("idx") String idx,
			BoardVO bovo, HttpServletRequest request
			) {
		
		ModelAndView mv = new ModelAndView();
		BoardVO bovo2 = boardService.getBoardDetail(idx);
		String dbpwd = bovo2.getPwd();
		
		if(passwordEncoder.matches(bovo.getPwd(), dbpwd)) {
			// 비밀번호가 맞음
			try {
			    mv.setViewName("redirect:/board_detail");
				String path = request.getSession().getServletContext().getRealPath("/resources/upload");
				MultipartFile file = bovo.getFile_name();
				if(file.isEmpty()) {
					bovo.setF_name(bovo.getOld_f_name());
				}else {
					UUID uuid = UUID.randomUUID();
					String f_name = uuid.toString() + "_" + file.getOriginalFilename();
					bovo.setF_name(f_name);
				
					// 업로드
					file.transferTo(new File(path, f_name));
				}
				// 암호화
				String pwd = passwordEncoder.encode(bovo.getPwd());
				bovo.setPwd(pwd);
				
				int result = boardService.getBoardUpdate(bovo);
				if(result > 0) {
					return mv;
				}
				return null;
				
			} catch (Exception e) {
				System.out.println(e);
				return null;
			}
			
		}else {
			// 비빌번호가 틀린 경우
			mv.setViewName("board/update");
			mv.addObject("pwdchk", "fail");
			mv.addObject("bovo", bovo);
			return mv;
		}
	}
}