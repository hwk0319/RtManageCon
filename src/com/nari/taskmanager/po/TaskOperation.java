package com.nari.taskmanager.po;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.taskmanager.util.ValidateUtil;

@Component
public class TaskOperation extends BaseTask {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<TaskStep> steps = new ArrayList<TaskStep>();
	private int type;
	private Timestamp createTime;
	private Timestamp lastTime;// 上次的执行时间。
	private String modul;
	private String cronExpress;
	private String resultDesc;
	private List<TaskParam> params = new ArrayList<TaskParam>();
	private long costTime;// 上次执行所用时间
	@JsonIgnore
	private String path;
	private int percent;//任务执行进度百分比 0-100;
	private String sql;//从数据库获取参数值的sql，多个以;间隔
	private int presetId;//任务的预设ID，不为0说明为预设任务。
	private String userId;//任务创建者的Id。
	private volatile int dstHostNum;//当前任务或当前任务的步骤所需要执行的主机的数目，一个任务或步骤对应多个主机，只有所有主机都返回成功才算执行成功，此字段用于标识成功的主机数目。
	private  Timestamp nextTime;//当前任务或当前任务的步骤所需要执行的主机的数目，一个任务或步骤对应多个主机，只有所有主机都返回成功才算执行成功，此字段用于标识成功的主机数目。
	private String mData;
	private String tplSel;
	private String taskTypeName;
	private String taskStatusName;
	private Integer task_opera_state;
	//位置信息
	private String position;
	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("operation_name: ").append(this.getName()).append(" | ").append("task_id: ").append(this.getId())
				.append(" | ").append("operation_state: ").append(this.getState()).append(" | ")
				.append("operation_type: ").append(this.getType()).append(" | ").append("operation_modul: ")
				.append(this.getModul()).append(" | ").append("presetId: ").append(presetId);
		if (null != this.getCreateTime()) {
			sbuf.append(" | ").append("operation_createTime: ").append(this.getCreateTime().toString()).append(" | ");
		}

		return sbuf.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TaskOperation) {
			if (this.getId() == ((TaskOperation) obj).getId()) {
				return true;
			}
		}
		return false;
	}

	public List<TaskStep> getSteps() {
		return steps;
	}

	public void setSteps(List<TaskStep> steps) {
		this.steps = steps;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getModul() {
		return modul;
	}

	public void setModul(String modul) {
		this.modul = modul;
	}

	public String getCronExpress() {
		return cronExpress;
	}

	public void setCronExpress(String cronExpress) {
		this.cronExpress = cronExpress;
	}

	public List<TaskParam> getParams() {
		return params;
	}

	public void setParams(List<TaskParam> params) {
		this.params = params;
	}

	public Timestamp getLastTime() {
		return lastTime;
	}

	public void setLastTime(Timestamp lastTime) {
		this.lastTime = lastTime;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public Long getCostTime() {
		return costTime;
	}

	public void setCostTime(Long costTime) {
		this.costTime = costTime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public int getMaxStep(){
		if(ValidateUtil.checkNotEmpty(steps))
		{
			return steps.get(steps.size()-1).getStepOrder();
		}
		return 0;
	}

	public int getPresetId() {
		return presetId;
	}

	public void setPresetId(int presetId) {
		this.presetId = presetId;
	}

	public int getDstHostNum() {
		return dstHostNum;
	}

	public void setDstHostNum(int dstHostNum) {
		this.dstHostNum = dstHostNum;
	}

	/**
	 * 任务的发起者，如双活id，相同userid的任务不能并行执行。
	 * @param userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 任务的发起者，如双活id，相同userid的任务不能并行执行。
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Timestamp getNextTime() {
		return nextTime;
	}

	public void setNextTime(Timestamp nextTime) {
		this.nextTime = nextTime;
	}

	public String getmData() {
		return mData;
	}

	public void setmData(String mData) {
		this.mData = mData;
	}

	public String getTplSel() {
		return tplSel;
	}

	public void setTplSel(String tplSel) {
		this.tplSel = tplSel;
	}

	public String getTaskTypeName() {
		return taskTypeName;
	}

	public void setTaskTypeName(String taskTypeName) {
		this.taskTypeName = taskTypeName;
	}

	public String getTaskStatusName() {
		return taskStatusName;
	}

	public void setTaskStatusName(String taskStatusName) {
		this.taskStatusName = taskStatusName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getTask_opera_state() {
		return task_opera_state;
	}

	public void setTask_opera_state(Integer task_opera_state) {
		this.task_opera_state = task_opera_state;
	}


}