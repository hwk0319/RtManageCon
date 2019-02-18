package com.nari.common.service;


import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service基类
 * @author lirh
 *
 */
@Transactional(readOnly = true)
public abstract class BaseService {
	/**
	 * 日志对象
	 */
	protected Logger logger = Logger.getLogger(getClass());

}
