package com.nari.module.address.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.address.dao.AddressDao;
import com.nari.module.address.po.AddressPo;

@Service(value="addressService")
public class AddressService {
	@Resource(name = "addressDao")
	private  AddressDao dao;
	
	public List<AddressPo> search(@Param("po") AddressPo po){
		return dao.search(po);
	}
	
	public int insert(@Param("po")   AddressPo po) {
		return dao.insert(po);
	}	
	
	public int update(@Param("po")   AddressPo po) {
		return dao.update(po);
	}		

	public int delete(@Param("po")   AddressPo po) {
		return dao.delete(po);
	}
}
