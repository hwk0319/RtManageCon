package com.nari.taskmanager.po;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;


@Component
public class TaskPagePo {	
	private int rows=16;//每页记录数
	private int page=1;//当前页码
	private int total=0;//分页总数
	private int totalCount=0;// 数据总记录数
	private int limitMin=0;
	private int limitMax = Integer.MAX_VALUE;
	private Timestamp startTime;
	private Timestamp endTime;
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		if(null ==startTime || "".equals(startTime))
		{
			this.startTime = null ;
		}
		else
		{
			this.startTime = Timestamp.valueOf(startTime);
		}
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		if("".equals(endTime))
		{
			this.endTime = null;
		}
		else
		{
			this.endTime = Timestamp.valueOf(endTime);
		}
	}
	public int getLimitMax() {
		return limitMax;
	}
	public void setLimitMax(int limitMax) {
		this.limitMax = limitMax;
	}
	public int getLimitMin() {
		return limitMin;
	}
	public void setLimitMin(int limitMin) {
		this.limitMin = limitMin;
	}
}
