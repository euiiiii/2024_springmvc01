package com.ict.edu04.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.edu04.dao.AjaxMembersDAO;
import com.ict.edu04.vo.MembersVO;

@Service
public class AjaxMembersServiceImpl implements AjaxMembersService{

	@Autowired
	private AjaxMembersDAO ajaxMembersDAO;
	
	@Override
	public List<MembersVO> getMemberList() {
		return ajaxMembersDAO.getMemberList();
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
		return ajaxMembersDAO.getMemberIdChk(m_id);
	}

}
