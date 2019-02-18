package com.nari.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class BaseController {
	
	private Logger logger = Logger.getLogger(BaseController.class);

	public String search() {
		return null;
	}

	public String getObjectValues(Object model) throws NoSuchMethodException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		StringBuffer returnValue = new StringBuffer();
		Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
		for (int j = 0; j < field.length; j++) { // 遍历所有属性
			String name = field[j].getName(); // 获取属性的名字
			returnValue.append(name+":");
			name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
			String type = field[j].getGenericType().toString(); // 获取属性的类型
			if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
				Method m = model.getClass().getMethod("get" + name);
				String value = (String) m.invoke(model); // 调用getter方法获取属性值
				returnValue.append(value+";");
			}
			if (type.equals("class java.lang.Integer")) {
				Method m = model.getClass().getMethod("get" + name);
				Integer value = (Integer) m.invoke(model);
				if (value != null) {
					returnValue.append(value+";");
				}
			}
			if (type.equals("class java.lang.Short")) {
				Method m = model.getClass().getMethod("get" + name);
				Short value = (Short) m.invoke(model);
				if (value != null) {
					returnValue.append(value+";");
				}
			}
			if (type.equals("class java.lang.Double")) {
				Method m = model.getClass().getMethod("get" + name);
				Double value = (Double) m.invoke(model);
				if (value != null) {
					returnValue.append(value+";");
				}
			}
			if (type.equals("class java.lang.Boolean")) {
				Method m = model.getClass().getMethod("get" + name);
				Boolean value = (Boolean) m.invoke(model);
				if (value != null) {
					returnValue.append(value+";");
				}
			}
			if (type.equals("class java.util.Date")) {
				Method m = model.getClass().getMethod("get" + name);
				Date value = (Date) m.invoke(model);
				if (value != null) {
					returnValue.append(value.toLocaleString()+";");
				}
			}
		}
		return returnValue.toString();
	}
	
	public static String toString(Object obj) {
    	String str="";
    	try {
    		str=String.valueOf(obj);
			if(str.equals("null")||str==null) {
				return "";
			}
		} catch (Exception e) {
			str="";
		}
    	return str;
   }
	
	public static int toInteger(Object obj) {
    	int str=0;
    	try {
    		str=Integer.parseInt(toString(obj));
		} catch (Exception e) {
		}
    	return str;
   }
	
	public static double toDouble(Object obj) {
    	double str=0;
    	try {
    		str=Double.parseDouble(toString(obj));
		} catch (Exception e) {
		}
    	return str;
   }
	/**
	 * 保留位数
	 */
	public String RoundX(double num, int ws) {
		String retval="";
		try {
		     BigDecimal bg = new BigDecimal(num);  
		     retval=bg.setScale(ws, BigDecimal.ROUND_HALF_UP).toString();
		} catch (Exception e) {
			logger.error("保留位数方法出错", e);
		}
		return retval;
	}
	/**
	 * 四舍五入
	 */
	public String Num45(String num, int ws) {
		String retval="";
		try {
			BigDecimal bd=new BigDecimal(num);
			retval=bd.setScale(ws,BigDecimal.ROUND_HALF_UP).toString();
		} catch (Exception e) {
			return "0";
		}
		return retval;
	}
	
	public <E> List<Object> toObject(List<E> list){  
        List<Object> objlist = new ArrayList<Object>();  
        for(Object e : list){  
            Object obj = (Object)e;  
            objlist.add(obj);  
        }  
        return objlist;  
	 }  
}
