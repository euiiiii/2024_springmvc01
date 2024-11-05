package com.ict.edu04.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ict.edu04.vo.MembersVO;

@Repository
public class AjaxMembersDAOImpl implements AjaxMembersDAO{

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public List<MembersVO> getMemberList() {
		return sqlSessionTemplate.selectList("ajaxmembers.getMembersList");
	}

	@Override
	public MembersVO getMemberDetail(String m_idx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMemberInsert(MembersVO mvo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMemberDelete(String m_idx) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMemberUpdate(MembersVO mvo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMemberIdChk(String m_id) {
		// 있으면 1, 없으면 0
		int result = sqlSessionTemplate.selectOne("ajaxmembers.getMemberIdChk", m_id);
		return String.valueOf(result);
		
	}

}
