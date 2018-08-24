package com.yhr.web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yhr.dao.GradeDao;
import com.yhr.dao.StudentDao;
import com.yhr.model.Grade;
import com.yhr.model.PageBean;
import com.yhr.util.DbUtil;
import com.yhr.util.JsonUtil;
import com.yhr.util.ResponseUtil;

public class GradeDeleteServlet extends HttpServlet{

	DbUtil dbUtil=new DbUtil();
	GradeDao gradeDao=new GradeDao();
	StudentDao studentDao=new StudentDao();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String delIds=request.getParameter("delIds");  
		JSONObject result=new JSONObject();
		Connection con=null;
		try{
			con=dbUtil.getCon();
			String str[]=delIds.split(",");
			for(int i=0;i<str.length;i++){
				boolean f=studentDao.getStudentByGradeId(con, str[i]);
				if(f){
					result.put("errorMsg","班级下面有学生不能删除");
					ResponseUtil.write(response, result);
					return;
				}
			}
			
			int delNums=gradeDao.gradeDelete(con, delIds); //删除的数量
			if(delNums>0){
				result.put("success", "true");
				result.put("delNums",delNums);
			}else{
				result.put("errorMsg", "删除失败");
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
