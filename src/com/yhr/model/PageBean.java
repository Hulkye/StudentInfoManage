package com.yhr.model;

public class PageBean {   //封装page
	
	private int page; //班级信息第几页
	private int rows; //每页记录数
	private int start; //起始页
	
	public PageBean(int page, int rows) {
		super();
		this.page = page;
		this.rows = rows;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getStart() {
		return (page-1)*rows;  //计算每页的初始记录
	}

	
	
}
