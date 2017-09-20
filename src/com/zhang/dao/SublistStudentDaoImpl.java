package com.zhang.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zhang.Constant;
import com.zhang.model.Pager;
import com.zhang.model.Student;
import com.zhang.util.JdbcUtil;

public class SublistStudentDaoImpl implements StudentDao {

	@Override
	public Pager<Student> findStudent(Student searchModel, int pageNum, int pageSize) {
		List<Student> allStudentList = this.getAllStudent(searchModel);
		if (allStudentList == null || allStudentList.isEmpty()) {
			return null;
		}
		
		Pager<Student> pager = new Pager<>(pageNum, pageSize, allStudentList);
		
		return pager;
	}

	/**
	 * 模仿获取所有学生数据
	 * 
	 * @param searchModel
	 *            查询参数
	 * @return 查询结果
	 */
	public List<Student> getAllStudent(Student searchModel) {
		List<Student> result = new ArrayList<Student>();
		List<Object> paramList = new ArrayList<>();
		
		String stuName = searchModel.getStuName();
		int gender = searchModel.getGender();
		
		StringBuilder sql = new StringBuilder("select * from t_student where 1=1");
		if(stuName!=null&&!stuName.equals("")) {
			sql.append(" and stu_name like ?");
			paramList.add("%"+stuName.trim()+"%");
		}
		if (gender == Constant.GENDER_MALE || gender == Constant.GENDER_FEMALE) {
			sql.append(" and gender = ?");
			paramList.add(gender);
		}
		
		JdbcUtil jdbcUtil = null;
		try {
			jdbcUtil = new JdbcUtil();
			jdbcUtil.getConnection();
			List<Map<String, Object>> mapList = jdbcUtil.findResult(sql.toString(), paramList);
			if (mapList != null && !mapList.isEmpty()) {
				for (Map<String, Object> map : mapList) {
					Student student = new Student(map);
					result.add(student);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("获取数据异常！");
		}finally {
			if(jdbcUtil!=null) {
				jdbcUtil.releaseConn();
			}
		}
		return result;
	}

}
