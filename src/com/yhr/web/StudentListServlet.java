package com.yhr.web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yhr.dao.StudentDao;
import com.yhr.model.Student;
import com.yhr.model.PageBean;
import com.yhr.util.DbUtil;
import com.yhr.util.JsonUtil;
import com.yhr.util.ResponseUtil;
import com.yhr.util.StringUtil;

public class StudentListServlet extends HttpServlet{

	DbUtil dbUtil=new DbUtil();
	StudentDao studentDao=new StudentDao();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String stuNo=request.getParameter("stuNo");  //
		String stuName=request.getParameter("stuName");  //
		String sex=request.getParameter("sex");  //
		String bbirthday=request.getParameter("bbirthday");  //
		String ebirthday=request.getParameter("ebirthday");  //
		String gradeId=request.getParameter("gradeId");  //
		
		Student student=new Student();
		if(stuNo!=null){
			student.setStuNo(stuNo);
			student.setStuName(stuName);
			student.setSex(sex);
			if(StringUtil.isNotEmpty(gradeId)){
				student.setGradeId(Integer.parseInt(gradeId));
			}
		}
		
		String page=request.getParameter("page");  //获取当前页
		String rows=request.getParameter("rows");  //获取每页的记录数
		
		PageBean pageBean=new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		Connection con=null;
		try{
			con=dbUtil.getCon();
			JSONObject result=new JSONObject();
			JSONArray jsonArray=JsonUtil.formatRsToJsonArray(studentDao.studentList(con, pageBean,student,bbirthday,ebirthday));
			int total=studentDao.studentCount(con,student,bbirthday,ebirthday);
			result.put("rows",jsonArray);
			result.put("total",total);
			ResponseUtil.write(response, result);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
