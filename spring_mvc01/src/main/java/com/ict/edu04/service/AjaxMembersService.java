package com.ict.edu04.service;

import java.util.List;

import com.ict.edu04.vo.MembersVO;

public interface AjaxMembersService {
	public List<MembersVO> getMemberList();
	public String getMemberIdChk(String m_id) ;
	public MembersVO getMemberDetail(String m_idx);
	public int getMemberInsert(MembersVO mvo);
	public int getMemberDelete(String m_idx);
	public int getMemberUpdate(MembersVO mvo);
	
}
