package com.ict.dao;

import java.util.List;

import com.ict.vo.CVO;
import com.ict.vo.MVO;
import com.ict.vo.VO;

public interface MyDAO {
	
	MVO selectShopLogIn(MVO mvo) throws Exception;
	List<VO> selectShopList(String category) throws Exception;
	VO selectShopOneList(String idx) throws Exception;
	
	
	// 아이디와 제품번호 가지고 cart에 해당 제품이 있는지 검사 
	CVO selectShopCartSearch(String id,String p_num) throws Exception;
	// 없으면 카트에 추가
	int insertShopCartAdd(CVO cvo) throws Exception;
	// 있으면 카트 정보를 업데이트 
	int updateShopCartUp(CVO cvo) throws Exception;
	
	List<CVO> selectShopCartList(String id) throws Exception;
	int deleteCartDel(CVO cvo) throws Exception ;
	int updateCartAmount(CVO cvo) throws Exception;
	
	int insertShopProductAdd(VO vo) throws Exception;
}
