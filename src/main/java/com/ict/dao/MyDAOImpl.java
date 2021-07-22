package com.ict.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ict.vo.CVO;
import com.ict.vo.MVO;
import com.ict.vo.VO;

@Repository
public class MyDAOImpl implements MyDAO{
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public MVO selectShopLogIn(MVO mvo) throws Exception {
		return sqlSessionTemplate.selectOne("shopping.login", mvo);
	}

	@Override
	public List<VO> selectShopList(String category) throws Exception {
		return sqlSessionTemplate.selectList("shopping.list", category);
	}

	@Override
	public VO selectShopOneList(String idx) throws Exception {
		return sqlSessionTemplate.selectOne("shopping.onelist", idx);
	}

	@Override
	public CVO selectShopCartSearch(String id, String p_num) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("p_num", p_num);
		return sqlSessionTemplate.selectOne("shopping.selectCart", map);
	}

	@Override
	public int insertShopCartAdd(CVO cvo) throws Exception {
		return sqlSessionTemplate.insert("shopping.cartInsert", cvo);
	}
	
	// 카트 검사해서 있으면 제품의 갯수를 1증가 시킴
	@Override
	public int updateShopCartUp(CVO cvo) throws Exception {
		return sqlSessionTemplate.update("shopping.cartUpdate", cvo);
	}

	@Override
	public List<CVO> selectShopCartList(String id) throws Exception {
		return sqlSessionTemplate.selectList("shopping.cartList", id);
	}
	
	// 카트 제품 삭제 
	@Override
	public int deleteCartDel(CVO cvo) throws Exception {
		return sqlSessionTemplate.delete("shopping.cartDelete",cvo);
	}
	
	// 카트에서 수량을 변경하면 변경된 수량을 적용
	@Override
	public int updateCartAmount(CVO cvo) throws Exception {
		return sqlSessionTemplate.update("shopping.cartAmountUpdate", cvo);
	}
	// 관리자가 제품 등록
	@Override
	public int insertShopProductAdd(VO vo) throws Exception {
		return sqlSessionTemplate.insert("shopping.productAdd",vo);
	}

}
