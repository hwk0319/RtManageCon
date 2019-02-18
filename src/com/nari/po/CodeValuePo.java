package com.nari.po;

import java.io.Serializable;

import com.nari.common.persistence.BaseEntity;

public class CodeValuePo extends BaseEntity<CodeValuePo>{

	private String code_type  ;   
	private String code       ;   
	private String name      ;    
	private String status    ;    
	private String name_en   ;    
	private String spell      ;   
	private String description ;

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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getName_en() {
		return name_en;
	}
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}
	public String getSpell() {
		return spell;
	}
	public void setSpell(String spell) {
		this.spell = spell;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}  

}
