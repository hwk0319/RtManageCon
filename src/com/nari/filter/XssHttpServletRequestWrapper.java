package com.nari.filter;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {  
    HttpServletRequest orgRequest = null;  
    
    private static String[] filterChars;  
    private static String[] replaceChars;
  
    public XssHttpServletRequestWrapper(HttpServletRequest request,String filterChar,String replaceChar,String splitChar) {  
        super(request);  
        
        if(filterChar!=null&&filterChar.length()>0){  
            filterChars=filterChar.split(splitChar);  
        }  
        if(replaceChar!=null&&replaceChar.length()>0){  
            replaceChars=replaceChar.split(splitChar);  
        }  
    }  
    /**  
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。  
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取  
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖  
     */  
    @Override  
    public String getParameter(String name) {  
        String value = super.getParameter(xssEncode(name));  
        if (value != null) {  
            value = xssEncode(value);  
        }  
        return value;  
    }  
    @Override  
    public String[] getParameterValues(String name) {  
        String[] value = super.getParameterValues(name);  
        if(value != null){  
        	//判断不需要过滤的参数
        	if("period".equals(name) || "cron".equals(name)){
        		return value;
        	}else{
        		for (int i = 0; i < value.length; i++) {  
        			value[i] = xssEncode(value[i]);  
        		}  
        	}
        }  
        return value;  
    }  
    @Override  
    public Map getParameterMap() {  
        return super.getParameterMap();  
    }  
  
    /**  
     * 将容易引起xss漏洞的半角字符直接替换成全角字符 在保证不删除数据的情况下保存  
     * @return 过滤后的值  
     */  
    private static String xssEncode(String value) {  
        if(StringUtils.isEmpty(value)){
            return value;
        }else{
            if (value != null) {
                Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");
                // 会误伤百度富文本编辑器
                // Avoid anything in a src="http://www.yihaomen.com/article/java/..." type of e­xpression
                scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                // Remove any lonesome </script> tag
                scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");
                // Remove any lonesome <script ...> tag
                scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                // Avoid eval(...) e­xpressions
                scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                // Avoid e­xpression(...) e­xpressions
                scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                // Avoid javascript:... e­xpressions
                scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");
                // Avoid vbscript:... e­xpressions
                scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");
                // Avoid onload= e­xpressions
                scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                 scriptPattern = Pattern.compile("document(.*?)=", Pattern.CASE_INSENSITIVE);
                 value = scriptPattern.matcher(value).replaceAll("");
                 // Avoid document.cookie
                 scriptPattern = Pattern.compile("document.cookie", Pattern.CASE_INSENSITIVE);
                 value = scriptPattern.matcher(value).replaceAll("");
                 
//                 value = xssEncodeStr(value);
                 
//                 value = sqlInject(value);
             }
             return value;
         } 
    }  
    
    /** 
     * 将容易引起xss漏洞的半角字符直接替换成全角字符 
     * @param s 
     * @return 
     */  
    private static String xssEncodeStr(String s) {  
        if (s == null || s.equals("")) {  
            return s;  
        }  
        try {  
            s = decode(s, "UTF-8");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        for (int i = 0; i < filterChars.length; i++) {  
            if(s.contains(filterChars[i])){  
                s=s.replace(filterChars[i], replaceChars[i]);  
            }  
        }  
        return s;  
    }  
    
    /**
     * 将容易引起sql注入的特殊字符过滤掉
     * @param str
     * @return
     */
    private final static String SQL_REG = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(?:\\*)|(?:;)|"
			+ "(\\b(select|update|delete|create|insert|into|from|where|order|by|and|"
			+ "or|trancate|char|substr|ascii|declare|exec|count|master|drop|execute|"
			+ "user|table|use|union)\\b)";
    private static String sqlInject(String str){
    	if (str == null || "".equals(str)) {  
            return str;  
        }
		str = Pattern.compile(SQL_REG, Pattern.CASE_INSENSITIVE).matcher(str).replaceAll("");
    	return str;
    }
    
    
    /**
     * 重写URLDecoder.decode方法
     * @param s
     * @param enc
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String decode(String s, String enc)
            throws UnsupportedEncodingException{
            boolean needToChange = false;
            int numChars = s.length();
            StringBuffer sb = new StringBuffer(numChars > 500 ? numChars / 2 : numChars);
            int i = 0;
            if (enc.length() == 0) {
                throw new UnsupportedEncodingException ("URLDecoder: empty string enc parameter");
            }
            char c;
            byte[] bytes = null;
            while (i < numChars) {
                c = s.charAt(i);
                switch (c) {
                case '%':
                    try {
                        if (bytes == null)
                            bytes = new byte[(numChars-i)/3];
                        int pos = 0;

                        while ( ((i+2) < numChars) &&
                                (c=='%')) {
                            int v = Integer.parseInt(s.substring(i+1,i+3),16);
                            if (v < 0)
                                throw new IllegalArgumentException("URLDecoder: Illegal hex characters in escape (%) pattern - negative value");
                            bytes[pos++] = (byte) v;
                            i+= 3;
                            if (i < numChars)
                                c = s.charAt(i);
                        }
                        if ((i < numChars) && (c=='%'))
                            throw new IllegalArgumentException(
                             "URLDecoder: Incomplete trailing escape (%) pattern");
                        sb.append(new String(bytes, 0, pos, enc));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(
                        "URLDecoder: Illegal hex characters in escape (%) pattern - "
                        + e.getMessage());
                    }
                    needToChange = true;
                    break;
                default:
                    sb.append(c);
                    i++;
                    break;
                }
            }
            return (needToChange? sb.toString() : s);
        }
}
