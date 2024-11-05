package com.ict.member.dao;

import com.ict.member.vo.MemberVO;

public interface MemberDAO {
	public MemberVO getMemberLogin(String m_id) throws Exception;
}