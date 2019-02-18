package com.nari.util;

import org.apache.log4j.Logger;

public class Logs {
	private static Logger logger = Logger.getLogger(Logs.class);

	// debug方法，其他error、warn类似定义
	public static void debug(Object obj) {
		logger.debug(obj);
	}

	// debug方法，其他error、warn类似定义
	public static void error(Object obj) {
		logger.error(obj);
	}

}