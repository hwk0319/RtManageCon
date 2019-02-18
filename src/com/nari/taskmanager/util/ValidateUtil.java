package com.nari.taskmanager.util;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {
	
	//IPV4
    private static String IPV4_REGEX = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$"; 
    
    private static String PORT_REGEX = "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]{1}|6553[0-5])$";
    
    private static String UID_REGEX="[1-9][0-9]{8}";
	public static boolean checkCronExpress(String cronException) {

		return true;
	}
	
	/**
	 *  校验集合是否为空
	 * @param lists
	 * @return
	 * false  集合为null或空</br>
	 * true   不为空
	 */
	public static boolean checkNotEmpty(List<?> lists)
	{
		if(null == lists || lists.isEmpty())
		{
			return false;
		}
		return true;
	}
	/**
	 *  校验集合是否为空
	 * @param lists
	 * @return
	 * false  集合为null或空</br>
	 * true   不为空
	 */
	public static boolean checkNotEmpty(Set<?> lists)
	{
		if(null == lists || lists.isEmpty())
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 校验IPV4
	 * @param ip
	 * @return
	 * 	true 通过</br>
	 * 	false 不通过</br>
	 */
	public static boolean checkIpV4(String ip)
	{
		Pattern p = Pattern.compile(IPV4_REGEX);
		Matcher m = p.matcher(ip);
		boolean b = m.matches();
		return b;
	}
	
	/**
	 * 校验代端口的IPV4。</br>
	 * 192.168.10.31:2181
	 * @param ip
	 * @return
	 */
	public static boolean checkIpV4WithPort(String ip)
	{
		if(!isNullString(ip))
		{
			return false;
		}
		String[] ipRegs = ip.split(":");
		if(ipRegs.length!=2)
		{
			return false;
		}
		if(!checkIpV4(ipRegs[0]))
		{
			return false;
		}
		
		Pattern p = Pattern.compile(PORT_REGEX);
		Matcher m = p.matcher(ipRegs[1]);
		boolean b = m.matches();
		return b;
	}
	
	/**
	 * @param str
	 * @return
	 * false 如果str为null或长度为0</tr>
	 * true 
	 */
	public static boolean isNullString(String str)
	{
		if(null == str || str.length()==0)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 如果字符串为空或全为不可见字符，返回false。
	 * @param str
	 * @return
	 */
	public static boolean checkSpaceString(String str) {
		if (null == str || "".equals(str) || str.replaceAll("\\s", "").length() == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 校验统一的uid。</br>
	 * uid9位的数字,不以0开头,</br>
	 * @param uid
	 * @return
	 * 	true 匹配</br>
	 * 	false 不匹配</br>
	 */
	public static boolean validateUid(String uid)
	{
		Pattern p = Pattern.compile(UID_REGEX);
		Matcher m = p.matcher(uid);
		boolean b = m.matches();
		return b;
	}
	
}
