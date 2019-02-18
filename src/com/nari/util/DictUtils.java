package com.nari.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nari.common.utils.CacheUtils;
import com.nari.common.utils.SpringContextHolder;
import com.nari.dao.CodeValueDao;
import com.nari.po.CodeValuePo;




public class DictUtils {
	private static CodeValueDao dao =  SpringContextHolder.getBean(CodeValueDao.class);
	
	public static final String CACHE_DICT_MAP = "dictMap";

	/**
	 * 根据代码类型获取代码值
	 * @param type
	 * @return
	 */
	public static List<CodeValuePo> getDictList(String type){
		@SuppressWarnings("unchecked")
		Map<String, List<CodeValuePo>> dictMap = (Map<String, List<CodeValuePo>>)CacheUtils.get(CACHE_DICT_MAP);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			for (CodeValuePo dict : dao.search(new CodeValuePo())){
				List<CodeValuePo> dictList = dictMap.get(dict.getCode_type());
				if (dictList != null){
					dictList.add(dict);
				}else{
					dictMap.put(dict.getCode_type(), Lists.newArrayList(dict));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP, dictMap);
		}
		List<CodeValuePo> dictList = dictMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}
	
	/**
	 * 根据字典中文获取字典代码
	 * @param label
	 * @param type
	 * @param defaultLabel
	 * @return
	 */
	public static String getDictValue(String label, String type, String defaultLabel){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)){
			for (CodeValuePo dict : getDictList(type)){
				if (type.equals(dict.getCode_type()) && label.equals(dict.getName())){
					
					return dict.getCode();
				}
			}
		}
		return defaultLabel;
	}	
	
	/**
	 * 获取下拉列表选项
	 * @param type
	 * @return
	 */
	public static String getDictOptions(String type){
		String returnValue = "<option value=''>请选择...</option>";
		for (CodeValuePo dict : getDictList(type)){
			returnValue =returnValue + "<option value='"+dict.getCode()+"'>"+dict.getName()+"</option>";
		}
		return returnValue;
	}
	
}
