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
import org.springframework.web.bind.annotation.ResponseBody;
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
	public ModelAndView logInOkCommand(MVO m_vo, HttpServletRequest request) {
		try {
			MVO mvo = myService.selectShopLogIn(m_vo);
			if(mvo == null) {
				request.setAttribute("log_in","0");
				return new ModelAndView("login_err");
			}else {
				request.setAttribute("log_in","1");
				request.setAttribute("log_id",mvo.getId());
				request.setAttribute("log_name",mvo.getName());
				
				// 관리자인 경우
				if(mvo.getId().equals("admin") && mvo.getPw().equals("admin")) {
					request.setAttribute("admin", "ok");
					return new ModelAndView("admin");
				}
				// 관리자가 아니면 일반 회원
				return new ModelAndView("product_list");
			}
		} catch (Exception e) {
			System.out.println(e);
			request.setAttribute("log_in","0");
		}
		return null;
	}
	@RequestMapping(value = "addCart.do",produces = "application/html; charset=utf-8" )
	@ResponseBody
	public String AddCart(@ModelAttribute("idx")String idx,
			HttpSession session) {
		try {
			// 제품 정보를 구하기 위해서 VO 구하자 
			MVO mvo = (MVO)session.getAttribute("mvo");
			String id = mvo.getId();
			
			// idx를 이용해서 제품 정보를 구하자 
			VO vo = myService.selectShopOneList(idx);
			
			// 해당 아이디를 가진 사람의 카트에  해당 제품이 있는지 검색 하자.
			// 없으면 카트에 추가 하고, 있으면 카트에 수량을 1 증가 시키자 
			CVO cvo = myService.selectShopCartSearch(id, vo.getP_num());
			int result = 0 ;
			if(cvo == null) {
				// 카트에 없으면
				CVO c_vo = new CVO();
				c_vo.setP_num(vo.getP_num());
				c_vo.setP_name(vo.getP_name());
				c_vo.setP_price(String.valueOf(vo.getP_price()));
				c_vo.setP_saleprice(String.valueOf(vo.getP_saleprice()));
				c_vo.setAmount(String.valueOf(1));
				c_vo.setId(id);
				result = myService.insertShopCartAdd(c_vo);
			}else {
				// 카트에 있으면
				 result = myService.updateShopCartUp(cvo);
			}
			return String.valueOf(result);
		} catch (Exception e) {
			return "0";
		}
		
	}
	@RequestMapping("showCart.do")
	public ModelAndView showCartCommand(@ModelAttribute("id") String id) {
		try {
			ModelAndView mv = new ModelAndView("cartList");
		
			// 카트 테이블에서 해당 아이디가 가진 모든 목록을 가져오기 
			List<CVO> cartList = myService.selectShopCartList(id);
			mv.addObject("cartList", cartList);
			return mv;
		} catch (Exception e) {
		}
		return null;
	}
	
	@RequestMapping(value = "cartList.do",produces = "application/json; charset=utf-8" )
	@ResponseBody
	public List<CVO> cartListCommand(HttpSession session) {
		try {
			MVO mvo = (MVO)session.getAttribute("mvo");
			String id = mvo.getId();
			// 카트 테이블에서 해당 아이디가 가진 모든 목록을 가져오기 
			List<CVO> cartList = myService.selectShopCartList(id);
			return cartList;
		} catch (Exception e) {
		}
		return null;
	}
	
	@RequestMapping("logout.do")
	public ModelAndView logoutCommand(HttpSession session) {
		session.invalidate();
		return new ModelAndView("redirect:product_list.do");
	}
	
	@RequestMapping(value = "deleteCart.do",produces = "application/json; charset=utf-8" )
	@ResponseBody
	public List<CVO> deleteCart2Command(CVO cvo, HttpSession session){
		try {
			int result = myService.deleteCartDel(cvo);
			MVO mvo = (MVO)session.getAttribute("mvo");
			String id = mvo.getId();
			// 카트 테이블에서 해당 아이디가 가진 모든 목록을 가져오기 
			List<CVO> cartList = myService.selectShopCartList(id);
			return cartList ;
		} catch (Exception e) {
			return null;
		}
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







