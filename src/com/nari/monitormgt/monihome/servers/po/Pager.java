package com.nari.monitormgt.monihome.servers.po;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Pager implements Serializable
{

	public Pager()
	{

	}

	private static final long serialVersionUID = 1L;

	/**
	 * @Fields pageNo : 页码
	 */
	private Integer pageNo;
	/**
	 * @Fields pageSize : 每页记录条数
	 */
	@JsonIgnore
	private Integer pageSize;
	/**
	 * @Fields total : 总记录数
	 */
	@JsonIgnore
	private Integer total;
	@SuppressWarnings("rawtypes")
	private List rows;
	private Integer pageCount;//总页数
	
	public Integer getPageNo()
	{
		return pageNo;
	}

	// 从第几条开始
	public void setPageNo(Integer pageNo)
	{
		this.pageNo = pageNo;
	}

	public Integer getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(Integer pageSize)
	{
		this.pageSize = pageSize;
	}

	public Integer getTotal()
	{
		return total;
	}

	public void setTotal(Integer total)
	{
		this.total = total;
	}

	public List getRows()
	{
		return rows;
	}

	/**
	 * @功能简介：存放数据库中的数据，多行，每行为一个map对象
	 * @param rows：
	 */
	public void setRows(List rows)
	{
		this.rows = rows;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
}
