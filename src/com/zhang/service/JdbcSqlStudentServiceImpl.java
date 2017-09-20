package com.zhang.service;

import com.zhang.dao.JdbcSqlStudentDaoImpl;
import com.zhang.dao.StudentDao;
import com.zhang.model.Pager;
import com.zhang.model.Student;

public class JdbcSqlStudentServiceImpl implements StudentService {

	private StudentDao studentDao;

	public JdbcSqlStudentServiceImpl() {
		studentDao = new JdbcSqlStudentDaoImpl();
	}

	@Override
	public Pager<Student> findStudent(Student searchModel, int pageNum, int pageSize) {
		Pager<Student> result = studentDao.findStudent(searchModel, pageNum, pageSize);
		return result;
	}

}
