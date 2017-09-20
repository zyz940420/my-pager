package com.zhang.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zhang.Constant;
import com.zhang.model.Pager;
import com.zhang.model.Student;
import com.zhang.util.JdbcUtil;


/**
 * 使用mysql数据库limit关键字实现分页
 * 
 * @author zhangyuzhou
 *
 */
public class JdbcSqlStudentDaoImpl implements StudentDao {

	@Override
	public Pager<Student> findStudent(Student searchModel, int pageNum, int pageSize) {
		Pager<Student> result = null;// 返回的对象

		// 存放查询参数
		List<Object> paramList = new ArrayList<Object>();

		String stuName = searchModel.getStuName();
		int gender = searchModel.getGender();

		// 需要执行的sql
		StringBuilder sql = new StringBuilder("select * from t_student where 1=1");
		StringBuilder countSql = new StringBuilder("select count(id) as totalRecord from t_student where 1=1");

		// 需要拼接的参数
		if (stuName != null && !stuName.equals("")) {
			sql.append(" and stu_name like ?");
			countSql.append(" and stu_name like ?");
			paramList.add("%" + stuName + "%");
		}
		if (gender == Constant.GENDER_FEMALE || gender == Constant.GENDER_MALE) {
			sql.append(" and gender = ?");
			countSql.append(" and gender = ?");
			paramList.add(gender);
		}
		// 开始索引
		int fromIndex = pageSize * (pageNum - 1);

		// 使用limit 关键字，实现分页
		sql.append(" limit " + fromIndex + "," + pageSize);

		// 存放所有查询出的学生对象
		List<Student> studentList = new ArrayList<>();
		JdbcUtil jdbcUtil = null;
		try {
			jdbcUtil = new JdbcUtil();
			jdbcUtil.getConnection();

			// 获取总记录条数
			List<Map<String, Object>> countResult = jdbcUtil.findResult(countSql.toString(), paramList);
			Map<String, Object> countMap = countResult.get(0);
			int totalRecord = (int) countMap.get("totalRecord");

			// 获取查询的学生记录
			List<Map<String, Object>> studentResult = jdbcUtil.findResult(sql.toString(), paramList);
			if (studentResult != null) {
				for (Map<String, Object> map : studentResult) {
					Student student = new Student(map);
					studentList.add(student);
				}
			}

			// 获取总页数
			int totalPage = totalRecord / pageSize;
			if (totalRecord % pageSize != 0) {
				totalPage++;
			}

			// 组装pager对象
			result = new Pager<Student>(pageSize, pageNum, totalRecord, totalPage, studentList);
		} catch (SQLException e) {
			throw new RuntimeException("SQL执行出错！", e);
		} finally {
			if (jdbcUtil != null) {
				jdbcUtil.releaseConn();
			}
		}
		return result;
	}

}
