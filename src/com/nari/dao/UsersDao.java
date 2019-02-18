package com.nari.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.po.User;

@Repository(value="usersDao")
public interface UsersDao{
	public	User findUserByName(String name);
	public	User findUserByStatus(String name);
	public	User findUserByStatusLock(String name);
	//public int saveUser(User user);
	public User getUser(User user);
	
	public List<User> search(@Param("po") User po);
	public int delete(@Param("po") User po);
	public int insert(@Param("po") User po);
	public int insert_isc(@Param("po") User po);
	public int update(@Param("po") User po);
	public int updatePass(@Param("po") User po);
	public int updateErrNum(@Param("po") User po);
	public int updatefirstLogin(int id);
	public int updatePwdTime(@Param("po") User po);
	public int updateStatus(String name);
	public	User findUserByID(int id);
	public int accredit(@Param("po") User po);
	//public int astrict(@Param("po") User po);
	public int lock(@Param("po") User po);
	public int unlock(@Param("po") User po);
	
	public List<String> getPermissions(@Param("menucode")String menucode,@Param("DBTYPE")String DBTYPE);
}
