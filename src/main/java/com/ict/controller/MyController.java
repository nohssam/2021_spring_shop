package com.ict.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ict.service.MyService;
import com.ict.vo.CVO;
import com.ict.vo.MVO;
import com.ict.vo.VO;

@Controller
public class MyController {
	@Autowired
	private MyService myService;
	
	@RequestMapping("product_list.do")
	public ModelAndView listCommand(HttpServletRequest request) {
		try {
			ModelAndView mv = new ModelAndView("product_list");
			String category = request.getParameter("category");
			// 첫번째 들어올때는 파라미터가 null 이다.
			if(category == null) {
				category = "ele002";
			}
			List<VO> list = myService.selectShopList(category);
			mv.addObject("list", list);
			return mv;
		} catch (Exception e) {
		}
		return null;
	}
	@RequestMapping("onelist.do")
	public ModelAndView onelistCommand(@RequestParam("idx")String idx) {
		try {
			ModelAndView mv = new ModelAndView("product_content");
			VO vo = myService.selectShopOneList(idx);
			mv.addObject("vo", vo);
			return mv;
		} catch (Exception e) {
		}
		return null;
	}
	@RequestMapping("login.do")
	public ModelAndView logInCommand() {
		return new ModelAndView("login");
	}
	 
	@RequestMapping("login_ok.do")
	public ModelAndView logInOkCommand(MVO m_vo, HttpSession session) {
		try {
			MVO mvo = myService.selectShopLogIn(m_vo);
			if(mvo == null) {
				return new ModelAndView("login_err");
			}else {
				session.setAttribute("mvo", mvo);
				session.setAttribute("login","ok");
				// 관리자인 경우
				if(mvo.getId().equals("admin")&&mvo.getPw().equals("admin")) {
					session.setAttribute("admin", "ok");
					return new ModelAndView("admin");
				}
				// 관리자가 아니면 일반 회원
				return new ModelAndView("redirect:product_list.do");
				
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	
	@RequestMapping("addCart.do")
	public ModelAndView AddCart(@ModelAttribute("idx")String idx,
			HttpSession session) {
		try {
			ModelAndView mv = new ModelAndView("redirect:onelist.do");
			// 제품 정보를 구하기 위해서 VO 구하자 
			MVO mvo = (MVO)session.getAttribute("mvo");
			String id = mvo.getId();
			
			// idx를 이용해서 제품 정보를 구하자 
			VO vo = myService.selectShopOneList(idx);
			
			// 해당 아이디를 가진 사람의 카트에  해당 제품이 있는지 검색 하자.
			// 없으면 카트에 추가 하고, 있으면 카트에 수량을 1 증가 시키자 
			CVO cvo = myService.selectShopCartSearch(id, vo.getP_num());
			if(cvo == null) {
				// 카트에 없으면
				CVO c_vo = new CVO();
				c_vo.setP_num(vo.getP_num());
				c_vo.setP_name(vo.getP_name());
				c_vo.setP_price(String.valueOf(vo.getP_price()));
				c_vo.setP_saleprice(String.valueOf(vo.getP_saleprice()));
				c_vo.setAmount(String.valueOf(1));
				c_vo.setId(id);
				int result = myService.insertShopCartAdd(c_vo);
			}else {
				// 카트에 있으면
				int result = myService.updateShopCartUp(cvo);
			}
			return mv;
		} catch (Exception e) {
		}
		return null;
	}
	@RequestMapping("showCart.do")
	public ModelAndView showCartCommand(HttpSession session) {
		try {
			ModelAndView mv = new ModelAndView("viewcart");
			MVO mvo = (MVO)session.getAttribute("mvo");
			String id = mvo.getId();
			// 카트 테이블에서 해당 아이디가 가진 모든 목록을 가져오기 
			List<CVO> cartList = myService.selectShopCartList(id);
			mv.addObject("cartList", cartList);
			System.out.println(cartList.size());
			return mv;
		} catch (Exception e) {
		}
		return null;
	}
	@RequestMapping("logout.do")
	public ModelAndView logoutCommand(HttpSession session) {
		session.invalidate();
		return new ModelAndView("redirect:product_list.do");
	}
	
	@RequestMapping("deleteCart.do")
	public ModelAndView deleteCartCommand(CVO cvo) {
		try {
			int result = myService.deleteCartDel(cvo);
			return new ModelAndView("redirect:showCart.do");
		} catch (Exception e) {
		}
		return null;
	}
	@RequestMapping("editCart.do")
	public ModelAndView editCartCommand(CVO cvo) {
		try {
			int result = myService.updateCartAmount(cvo);
			return new ModelAndView("redirect:showCart.do");
		} catch (Exception e) {
		}
		return null;
	}
	@RequestMapping(value = "product_add", method = RequestMethod.POST)
	public ModelAndView productAddCommand(VO vo,HttpServletRequest request) {
		try {
			ModelAndView mv = new ModelAndView("redirect:product_list.do?category="+vo.getCategory());
			String path = request.getSession().getServletContext().getRealPath("/resources/images");
			MultipartFile file_1 = vo.getP_image1();
			MultipartFile file_2 = vo.getP_image2();
			
			vo.setP_image_s(file_1.getOriginalFilename());
			vo.setP_image_l(file_2.getOriginalFilename());
			int result = myService.insertShopProductAdd(vo);
			if(result>0) {
				byte[] in = file_1.getBytes();
				File out = new File(path, vo.getP_image_s());
				FileCopyUtils.copy(in, out);
				
				byte[] in2 = file_2.getBytes();
				File out2 = new File(path, vo.getP_image_l());
				FileCopyUtils.copy(in2, out2);
			}
			
			return mv;
		} catch (Exception e) {
		}
		return null;
	}
}







