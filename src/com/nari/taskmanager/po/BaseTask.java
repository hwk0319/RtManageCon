package com.nari.taskmanager.po;

import java.sql.Timestamp;
import javax.validation.constraints.Size;
import com.nari.common.po.RowBounds;

public class BaseTask extends RowBounds implements Comparable<BaseTask>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	@Size(min=1,max=60,message="任务名称请输入1到60个字符！")
	protected String name;
	private int state = 1;
//	@Size(min=1,max=100,message="任务描述请输入0到100个字符！")
	private String poDesc;
	private String oper;// jqGrid 的自动提交的一个字段。做添加操作时为 add
	private int cloneType;//任务或模板是否为克隆。
	private Timestamp startTime;
	private Timestamp endTime;
	private String orderBy;
	
	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	/**
	 * 最近一次执行所使用的时间，单位s。
	 */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = Integer.valueOf(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getPoDesc() {
		return poDesc;
	}

	public void setPoDesc(String desc) {
		this.poDesc = desc;
	}

	@Override
	public int compareTo(BaseTask o) {

		return this.getId() - o.getId();
	}

	public int getCloneType() {
		return cloneType;
	}

	public void setCloneType(int cloneType) {
		this.cloneType = cloneType;
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

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

}
