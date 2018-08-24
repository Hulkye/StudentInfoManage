package com.yhr.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yhr.dao.StudentDao;
import com.yhr.model.Student;
import com.yhr.model.PageBean;
import com.yhr.util.DateUtil;
import com.yhr.util.DbUtil;
import com.yhr.util.JsonUtil;
import com.yhr.util.ResponseUtil;
import com.yhr.util.StringUtil;

public class StudentSaveServlet extends HttpServlet{

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
		request.setCharacterEncoding("utf-8");
		String stuNo=request.getParameter("stuNo");  
		String stuName=request.getParameter("stuName");  
		String sex=request.getParameter("sex");  
		String birthday=request.getParameter("birthday");  
		String gradeId=request.getParameter("gradeId");  
		String email=request.getParameter("email");  
		String stuDesc=request.getParameter("stuDesc");  
		String stuId=request.getParameter("stuId");  
		
		Student student=null;
		try {
			student = new Student(stuNo,stuName,sex,DateUtil.formatString(birthday, "yyyy-MM-dd"),Integer.parseInt(gradeId), email,stuDesc);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(StringUtil.isNotEmpty(stuId)){
			student.setStuId(Integer.parseInt(stuId));
		}
		Connection con=null;
		try{
			con=dbUtil.getCon();
			int saveNums=0;
			JSONObject result=new JSONObject();
			if(StringUtil.isNotEmpty(stuId)){    //id²»Îª¿Õ£¬±à¼­²Ù×÷
				 saveNums=studentDao.studentModify(con, student); 
			}else{
				saveNums=studentDao.studentAdd(con, student); 
			}
			
			if(saveNums>0){
				result.put("success", "true");
			}else{
				result.put("success", "true");
				result.put("errorMsg", "É¾³ýÊ§°Ü");
			}
		
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
