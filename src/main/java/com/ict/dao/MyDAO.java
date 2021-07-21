package com.ict.dao;

import java.util.List;

import com.ict.vo.CVO;
import com.ict.vo.MVO;
import com.ict.vo.VO;

public interface MyDAO {
	
	VO selectShopLogIn(MVO mvo) throws Exception;
	List<VO> selectShopList(String category) throws Exception;
	VO selectShopOneList(String idx) throws Exception;
	int insertShopProductAdd(VO vo) throws Exception;
	VO selectShopCartSearch(CVO cvo) throws Exception;
	int insertShopCartAdd(CVO cvo) throws Exception;
	int updateShopCartUp(CVO cvo) throws Exception;
	List<CVO> selectShopCartList(String id) throws Exception;
	int deleteCartDel(CVO cvo) throws Exception ;
	
}