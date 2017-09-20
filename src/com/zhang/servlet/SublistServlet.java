package com.zhang.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhang.Constant;
import com.zhang.model.Pager;
import com.zhang.model.Student;
import com.zhang.service.StudentService;
import com.zhang.service.SublistStudentServiceImpl;
import com.zhang.util.StringUtil;

/**
 * Servlet implementation class SublistServlet
 */
@WebServlet("/SublistServlet")
public class SublistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentService studentService = new SublistStudentServiceImpl();

	public SublistServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 接收request里的参数
		String stuName = request.getParameter("stuName");// 学生姓名
		
		// 获取学生的性别,如果没传，那就默认
		int gender = Constant.DEFAULT_GENDER;
		String genderStr = request.getParameter("gender");
		if (genderStr != null && !"".equals(genderStr)) {
			gender = Integer.parseInt(genderStr);
		}
		
		// 校验pageNum参数的合法性
		String pageNumStr = request.getParameter("pageNum");
		if (pageNumStr != null && !StringUtil.isNum(pageNumStr)) {
			request.setAttribute("errorMsg", "参数传输错误！");
			request.getRequestDispatcher("sublistStudent.jsp").forward(request, response);
			return;
		}
		
		// 显示第几页数据，如果没有即默认
		int pageNum = Constant.DEFAULT_PAGE_NUM;
		if (pageNumStr != null && !"".equals(pageNumStr)) {
			pageNum = Integer.parseInt(pageNumStr);
		}
		
		int pageSize = Constant.DEFAULT_PAGE_SIZE;// 每页显示多少条记录
		String pageSizeStr = request.getParameter("pageSize");
		if (pageSizeStr != null && !"".equals(pageSizeStr)) {
			pageSize = Integer.parseInt(pageSizeStr);
		}

		// 组装查询条件
		Student searchModel = new Student();
		searchModel.setStuName(stuName);
		searchModel.setGender(gender);

		// 调用service获取查询条件
		Pager<Student> result = studentService.findStudent(searchModel, pageNum, pageSize);
		
		// 返回结果到页面
		request.setAttribute("result", result);
		request.getRequestDispatcher("sublistStudent.jsp").forward(request, response);
	}

}
