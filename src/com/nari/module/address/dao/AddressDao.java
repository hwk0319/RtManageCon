package com.nari.module.address.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.address.po.AddressPo;

@Repository(value="addressDao")
public interface AddressDao{
	public List<AddressPo> search(@Param("po") AddressPo po);	
	public int insert(@Param("po") AddressPo po);
	public int update(@Param("po") AddressPo po);
	public int delete(@Param("po") AddressPo po);
}
