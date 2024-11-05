package com.ict.shop.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ict.member.vo.MemberVO;
import com.ict.shop.service.ShopService;
import com.ict.shop.vo.CartVO;
import com.ict.shop.vo.ShopVO;

@Controller
public class ShopController {
	
	@Autowired
	private ShopService shopService;
	
	@GetMapping("/shop")
	public ModelAndView getShopList(String category) {
		try {
			ModelAndView mv = new ModelAndView("shop/product_list");
			if (category == null || category == "") {
				category = "ele002";
			}
			
			List<ShopVO> shop_list = shopService.getShopList(category);
			if (shop_list != null) {
				mv.addObject("shop_list", shop_list);
				return mv;
			}
			return null;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	@GetMapping("/shop_detail")
	public ModelAndView getShopDetail(@RequestParam("shop_idx") String shop_idx) {
		try {
			ModelAndView mv = new ModelAndView("shop/product_content");
			ShopVO svo = shopService.getShopDetail(shop_idx);
			if (svo != null) {
				mv.addObject("svo", svo);
				return mv;
			}
			return null;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	@GetMapping("/shop_addCart")
	public ModelAndView getShopAddCart(@ModelAttribute("shop_idx") String shop_idx, HttpSession session) {
		try {
			String loginchk = (String) session.getAttribute("loginchk"); // 로그인 성공하면 loginchk 가지고 있다
			if (loginchk.equals("ok")) {
				ModelAndView mv = new ModelAndView("redirect:/shop_detail");
				// 로그인 한 정보를 가져오자.
				MemberVO memvo = (MemberVO) session.getAttribute("memvo2"); // 세션은 무조건 Casting 해야함 => Object로 나와서 자료형이 없기에 자료형 맞춰줘야 함
				
				// 제품 정보 가져오자
				ShopVO svo = shopService.getShopDetail(shop_idx);
				
				// 카트 리스트에 로그인 한 사용자의 m_id와 해당 제품번호 있는 지(수량1 증가) 없는 지(카트 추가) 판별하자
				CartVO cartVO = shopService.getCartChk(memvo.getM_id(), svo.getP_num());
				
				if (cartVO == null) {
					// 카트가 비어 있으므로 카트 테이블에 추가(insert)
					CartVO cavo = new CartVO();
					cavo.setP_num(svo.getP_num());
					cavo.setP_name(svo.getP_name());
					cavo.setP_price(String.valueOf(svo.getP_price())); // 자료형이 맞지 않아서 맞춰줘야 함
					cavo.setP_saleprice(String.valueOf(svo.getP_saleprice())); // 자료형이 맞지 않아서 맞춰줘야 함
					cavo.setM_id(memvo.getM_id());
					
					int result = shopService.getCartInsert(cavo);
				} else {
					// 카트에 있으므로 수량 증가(update)
					int result = shopService.getCartUpdate(cartVO);
				}
				return mv;
			} else {
				return new ModelAndView("sns/login_error");
			}
		} catch (Exception e) {
			System.out.println(e);
			return new ModelAndView("sns/login_error");
		}
	}
	
	@GetMapping("/shop_showCart")
	public ModelAndView getShopShowCart(@ModelAttribute("shop_idx") String shop_idx, HttpSession session) {
		try {
			String loginchk = (String) session.getAttribute("loginchk"); // 로그인 성공하면 loginchk 가지고 있다
			if (loginchk.equals("ok")) {
				ModelAndView mv = new ModelAndView("shop/cartList");
				
				// 로그인한 사람의 정보를 가져와서 카트에 검색 후 cartlist에 내보내자
				MemberVO memvo = (MemberVO) session.getAttribute("memvo2");
				
				// 여러 개일 때
				List<CartVO> cart_list = shopService.getCartList(memvo.getM_id());
				if (cart_list != null) {
					mv.addObject("cart_list", cart_list);
				}
				return mv; // 카트 있든 없든 cart_list로 넘어가야 함
			} else {
				return new ModelAndView("sns/login_error");
			}
		} catch (Exception e) {
			System.out.println(e);
			return new ModelAndView("sns/login_error");
		}
	}
	
	@PostMapping("/cart_edit")
	public ModelAndView getShopCartEdit(CartVO cavo) {
		try {
			ModelAndView mv = new ModelAndView("redirect:/shop_showCart");
			int result = shopService.getCartEdit(cavo);
			if (result > 0) { // 성공 시
				return mv;
			}
			return null;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	@GetMapping("/cart_delete")
	public ModelAndView getShopCartDelete(String cart_idx) {
		try {
			ModelAndView mv = new ModelAndView("redirect:/shop_showCart");
			int result = shopService.getCartDelete(cart_idx);
			if (result > 0) {
				return mv;
			}
			
			return null;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	@GetMapping("/shop_add_form")
	public ModelAndView getShopAddForm() {
		return new ModelAndView("shop/product_insert");
	}
	
	@PostMapping("/shop_product_insert_ok")
	public ModelAndView getShopAddOK(ShopVO svo, HttpServletRequest request) {
		try {
			ModelAndView mv = new ModelAndView("redirect:/shop?category="+svo.getCategory());
			
			String path = request.getSession().getServletContext().getRealPath("/resources/images"); // multipart 쓰면 꼭 path 지정해줘야 함
			MultipartFile file_s = svo.getFile_s();
			MultipartFile file_l = svo.getFile_l();
			
			// 파일은 둘 다 required이기 때문에 무조건 넘어온다.
			UUID uuid = UUID.randomUUID(); // service 또는 common에 메서드로 미리 적어둔다.(refactoring) 가져다 써라 => 중복되는 거
			svo.setP_image_s(uuid.toString()+"_"+file_s.getOriginalFilename());
			svo.setP_image_l(uuid.toString()+"_"+file_l.getOriginalFilename());
			
			file_s.transferTo(new File(path, svo.getP_image_s()));
			file_l.transferTo(new File(path, svo.getP_image_l()));
			
			// DB에 저장
			int result = shopService.getProductInsert(svo);
			if (result > 0) {
				return mv;
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
}