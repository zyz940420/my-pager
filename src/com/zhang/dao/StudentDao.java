package com.zhang.dao;

import com.zhang.model.Pager;
import com.zhang.model.Student;

public interface StudentDao {

	/**
	 * 根据查询条件，查询学生分页信息
	 * 
	 * @param searchModel
	 *            封装查询条件
	 * @param pageNum
	 *            查询第几页数据
	 * @param pageSize
	 *            每页显示多少条记录
	 * @return 查询结果
	 */
	Pager<Student> findStudent(Student searchModel, int pageNum, int pageSize);

}
