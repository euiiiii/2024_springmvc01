package com.ict.edu05.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.edu05.dao.EmpDAO;
import com.ict.edu05.vo.EmpVO;

// DB 처리가 아닌 것들은 Service에서 일처리 해야한다.

@Service
public class EmpServiceImpl implements EmpService {
	@Autowired
	private EmpDAO empDAO;
	
	@Override
	public List<EmpVO> getList() throws Exception {
		return empDAO.getList();
	}

	@Override
	public List<EmpVO> getSearch(String deptno) throws Exception {
		return empDAO.getSearch(deptno);
	}

	@Override
	public List<EmpVO> getSearch(EmpVO empvo) throws Exception {
		return empDAO.getSearch(empvo);
	}
	
	@Override
	public List<EmpVO> getSearch(String idx, String keyword) throws Exception {
		return empDAO.getSearch(idx, keyword);
	}
}