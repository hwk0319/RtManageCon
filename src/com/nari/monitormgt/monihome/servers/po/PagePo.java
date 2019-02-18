package com.nari.monitormgt.monihome.servers.po;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Administrator
 * @date 2017年5月22日
 * @Description 分页po
 *
 */
public class PagePo {
	@JsonIgnore
	private int pageSize=100;//每页记录数
	private int pageNum=1;//当前页码
	@JsonIgnore
	private int pageCount=0;//分页总数
	@JsonIgnore
	private int total=0;// 数据总记录数
	@JsonIgnore
	private int startTotal;//从第几条记录开始
	@JsonIgnore
	private int endTotal;//到第几条结束

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public int getPageNum()
	{
		return pageNum;
	}

	public void setPageNum(int pageNum)
	{
		this.pageNum = pageNum;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getStartTotal() {
		return startTotal;
	}

	public void setStartTotal(int startTotal) {
		this.startTotal = startTotal;
	}

	public int getEndTotal() {
		return endTotal;
	}

	public void setEndTotal(int endTotal) {
		this.endTotal = endTotal;
	}
	
}
