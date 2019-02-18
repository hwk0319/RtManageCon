package com.nari.po;

import com.nari.common.persistence.BaseEntity;

public class SysParamPo extends BaseEntity<SysParamPo> {

	private String code;
	private String name;
	private String value;
	private String value_code;
	private String unit;
	private String description;
	private String code_type;
	private String paramOptions;

	public String getParamOptions() {
		return paramOptions;
	}
	public void setParamOptions(String paramOptions) {
		this.paramOptions = paramOptions;
	}
	public String getCode_type() {
		return code_type;
	}
	public void setCode_type(String code_type) {
		this.code_type = code_type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue_code() {
		return value_code;
	}
	public void setValue_code(String value_code) {
		this.value_code = value_code;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
