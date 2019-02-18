package com.nari.module.heathScore.po;
/**
 * 
 * @author Administrator
 * @date 2017年8月7日
 * @Description 评分扣分历史表
 *
 */
public class HeathcheckDeductPo {
	private Integer health_check_id;
	private Integer deduct;
	private String record_time;
	public Integer getHealth_check_id() {
		return health_check_id;
	}
	public void setHealth_check_id(Integer health_check_id) {
		this.health_check_id = health_check_id;
	}
	public Integer getDeduct() {
		return deduct;
	}
	public void setDeduct(Integer deduct) {
		this.deduct = deduct;
	}
	public String getRecord_time() {
		return record_time;
	}
	public void setRecord_time(String record_time) {
		this.record_time = record_time;
	}
	@Override
	public String toString() {
		return "HeathcheckDeductPo [health_check_id=" + health_check_id
				+ ", deduct=" + deduct + ", record_time=" + record_time + "]";
	}
	
}
