package com.nari.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class Util {
	/**
	 * 特殊字符校验
	 * @param str
	 * @return true 含有特殊字符，false 不含有特殊字符
	 */
	public static final boolean validationStr(String str){
		if(str != null && !"".equals(str)){
			String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
			Pattern p=Pattern.compile(regEx);
			Matcher m=p.matcher(str);
			return m.find();
		}else{
			return false;
		}
	}
	public static final boolean validationStrZhouqi(String str){
		String regEx="[`~!@#$%^&()+=|{}':;',\\[\\].<>~！@#￥%……&（）+|{}【】‘；：”“’。，、？]";
		Pattern p=Pattern.compile(regEx);
		Matcher m=p.matcher(str);
		return m.find();
	}
	
	/**
	 * sql注入
	 * @param str
	 * @return
	 * date 2018年5月25日18:10:46
	 */
	public static boolean sql_inj(String str) {
		str = str.toLowerCase();
		String[] inj_str = {"'","and ","exec ","insert ","select ","delete ","update ","count"," * ","%","chr","mid","master","truncate","char","declare",";"," or ","-","+"};
		for (int i = 0; i < inj_str.length; i++) {
			if (str.indexOf(inj_str[i]) >= 0) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 根据jwt判断是否重复提交
	 * @param request
	 * @param jwtStr
	 * @return
	 */
	public static boolean getSessionJWT(HttpServletRequest request, String jwtStr){
		boolean res = true;
		String jwt = (String) request.getSession().getAttribute("jwt");
		if(jwt != null && !"".equals(jwtStr)){
			if(jwt.equals(jwtStr)){
				
			}else{
				res = false;
			}
		}else{
			res = false;
		}
		return res;
	}
	
	/**
	 * 文件路径检查，防止用户通过../等方式取得其他文件
	 * @param filePath
	 * @return
	 */
	public static String filePathCheck(String filePath){
		while(filePath.contains("..")){
			filePath = filePath.replace("..", "");
		}
		return filePath;
	}
	
	/**  
     * 将容易引起xss漏洞的半角字符直接替换成全角字符 在保证不删除数据的情况下保存  
     * @return 过滤后的值  
     */  
    public static String xssEncode(String value) {  
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
                // Avoid document.cookie
                scriptPattern = Pattern.compile("document.cookie", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");
            }
            return value;
        } 
   }  
    
    /**
	 * 读取iscip.properties配置文件信息
	 * @param req
	 * @return
	 * @throws IOException
	 */
	public static Properties getProperties(String path) throws IOException {  
	   	 Properties prop = new Properties();  
	   	 try(
	   		 InputStream in = new BufferedInputStream (new FileInputStream(path));  
	   	 ) {
	   		 prop.load(in);  
	   	 } catch (IOException e) {  
	   		 throw e;
	   	 }  
	   	 return prop;
	}
	
	/**
	 * 读取配置文件判断内外网
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getInOrOut(HttpServletRequest request) throws Exception{
		String path = request.getSession().getServletContext().getRealPath("/")+"WEB-INF/classes/config/systemLog.properties";
		Properties pps = getProperties(path);
		String inOrOut = pps.getProperty("inOut");
		return inOrOut;
	}
	
	/**
	 * 读取src下面config包内的配置文件
	 * @return
	 * @throws IOException
	 */
	public static String getInOrOut() throws IOException{
		 InputStream in = Util.class.getClassLoader().getResourceAsStream("config/systemLog.properties");
		 BufferedReader br = new BufferedReader(new InputStreamReader(in));
		 Properties props = new Properties();
		 props.load(br);
		 String inOrOut = props.getProperty("inOut");
		return inOrOut;
	 }

}
