package com.ict.member.service;

import com.ict.member.vo.MemberVO;

public interface MemberService {
	public MemberVO getMemberLogin(String m_id) throws Exception;
}
