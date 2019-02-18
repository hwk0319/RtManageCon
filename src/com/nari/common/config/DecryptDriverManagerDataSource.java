package com.nari.common.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.nari.utils.RsaDecryptTool;

public class DecryptDriverManagerDataSource extends DriverManagerDataSource {

	RsaDecryptTool rsaTool = new RsaDecryptTool();

	/**
	 * dataSource 解密
	 */
	@Override
	protected Connection getConnectionFromDriver(Properties props) throws SQLException {
		  if (props.getProperty("password") != null && props.getProperty("password").toString().length() > 100){
			  String mm = rsaTool.decrypt(props.getProperty("password"));
			  props.setProperty("password", mm);
		  }
		  if (props.getProperty("user") != null && props.getProperty("user").toString().length() > 100){
			  String user = rsaTool.decrypt(props.getProperty("user"));
			  props.setProperty("user", user);
		  }
		return super.getConnectionFromDriver(props);
	}
}
