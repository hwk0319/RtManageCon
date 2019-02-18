package com.nari.taskmanager.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.po.TaskParam;
import com.nari.taskmanager.po.TaskStep;

/**
 * 导出一个task的参数文件到相应task的目录下。
 * 
 * @author admin
 *
 */
public class TaskFileUtil {

	private static Logger logger = Logger.getLogger(TaskFileUtil.class);

	/**
	 * 保存配置项到task的目录下。</br>
	 * 会删除原有的配置文件，重新创建。</br>
	 * 
	 * @param task
	 * @throws IOException
	 */
	public static void writeParamToFile(TaskOperation task) throws IOException {
		String fileName = TaskConfig.TASK_PARAM_FILE_NAME;
		Properties prop = new Properties();
		for (TaskParam param : task.getParams()) {
			String key=param.getName();
			String value=param.getValue();
			if(null == key ){
				key="";
			}
			if(null == value){
				value="";
			}
			if(key.startsWith(TaskConfig.USER_TASK_REMOTEIP_SUFFIX) || key.startsWith(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX)){
				continue;
			}
			prop.put(key, value);
		}
		FileOutputStream oFile = null;
		try {
			String filePath = TaskConfig.TASK_SHELL_BASE_PATH + task.getId() + File.separator + fileName;
			File f = new File(filePath);
			createFile(filePath);
			oFile = new FileOutputStream(f, false);
			prop.store(oFile, "task param.");
			logger.info("write param to file success, taskId:" + task.getId() + " taskName:" + task.getName());
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		} finally {
			if (null != oFile) {
				oFile.flush();
				oFile.close();
			}
		}

	}

	/**
	 * 读取原来的配置文件，组合以后进行覆盖。
	 * 
	 * @param paramMap
	 * @param task
	 * @throws IOException
	 */
	public static void appendParamToFile(Map<String, String> paramMap, TaskOperation task) throws IOException {
		String fileName = TaskConfig.TASK_PARAM_FILE_NAME;
		String filePath = TaskConfig.TASK_SHELL_BASE_PATH + task.getId() + File.separator + fileName;
		File f = new File(filePath);
		FileInputStream inFile = null;
		FileOutputStream outFile = null;
		try {
			inFile = new FileInputStream(f);
			Properties prop = new Properties();
			prop.load(inFile);
			for (Object obj : prop.keySet()) {
				String key = (String) obj;
				if (null != paramMap.get(key)) {
					logger.warn("append param to file Error param duplicate key:" + key + " valueOld:" + prop.getProperty(key) + " valueNew:" + paramMap.get(key));
				}
			}
			outFile = new FileOutputStream(f, true);
			prop.clear();
			for (String key : paramMap.keySet()) {
				if(key.startsWith(TaskConfig.USER_TASK_REMOTEIP_SUFFIX) || key.startsWith(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX)){
					continue;
				}
				prop.put(key, paramMap.get(key));
			}
			prop.store(outFile, "param by uid");
		} finally {
			if (null != inFile) {
				inFile.close();
			}

			if (null != outFile) {
				outFile.close();
			}
		}
	}
	
	/**
	 * 追加一个参数到ini配置文件，如果参数名相同则覆盖。
	 * @param param
	 * @param task
	 * @throws IOException
	 */
	public static void appendParamToFile(TaskParam param, TaskOperation task) throws IOException {
		String fileName = TaskConfig.TASK_PARAM_FILE_NAME;
		String filePath = TaskConfig.TASK_SHELL_BASE_PATH + task.getId() + File.separator + fileName;
		File f = new File(filePath);
		FileInputStream inFile = null;
		FileOutputStream outFile = null;
		try {
			inFile = new FileInputStream(f);
			Properties prop = new Properties();
			prop.load(inFile);
			for (Object obj : prop.keySet()) {
				String key = (String) obj;
				if (key.equals(param.getName())) {
					logger.warn("append param to file Error param duplicate key:" + key + " valueOld:" + prop.getProperty(key) + " valueNew:" + param.getName());
				}
			}
			if(param.getName().startsWith(TaskConfig.USER_TASK_REMOTEIP_SUFFIX) || param.getName().startsWith(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX)){
				return;
			}
			outFile = new FileOutputStream(f, true);
			prop.put(param.getName(), param.getValue());
			prop.store(outFile, "param by uid");
		} finally {
			if (null != inFile) {
				inFile.close();
			}
			
			if (null != outFile) {
				outFile.close();
			}
		}
	}

	public static String createDir(String path) {
		File file = new File(path);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				return "无法创建文件。";
			}
		}
		return TaskConfig.SUCCESS;
	}

	/**
	 * 保存字符串到指定文件</br>
	 * 如果文件存在则删除，不存在则新建。</br>
	 * 
	 * @param path
	 *            文件路径</br>
	 * @param str
	 *            要保存的字符串</br>
	 * @return
	 * @throws Exception
	 */
	public static String savefile(String path, String str) throws IOException {
		File f = new File(path);
		// 如果文件存在则删除。
		// 创建文件的路径
		if (f.exists()) {
			logger.info("file has exist! delete it!");
			f.delete();
			logger.info("delete exist file success," + path);
		} else {
			logger.info("file has not exist! creat new file path:" + path);
			File f_dir = f.getParentFile();
			f_dir.mkdirs();
		}
		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter(path);
			fwriter.write(str);
			fwriter.flush();
		} catch (IOException ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		} finally {
			try {
				if(fwriter!=null)
				fwriter.close();
			} catch (IOException ex) {
				logger.error(ex.getMessage(), ex);
				throw ex;
			}
		}
		logger.info("write Stirng to file success.");
		return TaskConfig.SUCCESS;

	}

	/**
	 * 删除本地文件或目录
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String delDirOrFile(String url) throws Exception {
		File f = new File(url);
		if (f.exists()) {
			if (f.isDirectory()) {
				File[] children = f.listFiles();
				if(children!=null){
					for (int i = 0; i < children.length; i++) {
						if (!delDirOrFile(children[i].getAbsolutePath()).equals(TaskConfig.SUCCESS)) {
							logger.error("delete local file url:" + children[i].getAbsolutePath());
							throw new Exception("删除本地文件失败！");
						}
					}
				}
			}
			if (!f.delete()) {
				logger.error("delete local file url:" + url);
				throw new Exception("删除本地文件失败！");
			}
		} else {
			logger.info("file is not exist,donot need delete, path:" + f.getAbsolutePath());
		}
		logger.info("delete file success path:" + f.getAbsolutePath());
		return TaskConfig.SUCCESS;
	}

		/**
	 * 保存一个任务到本地
	 * </hr>
	 * 保存脚本文件以及参数文件。
	 * </hr>
	 * 脚本文件名为用户上传的文件名。
	 * </hr>
	 * 参数文件名为paramList.ini
	 * 
	 * @param operation
	 * @throws Exception
	 */
	/*	public static void saveTaskToLocal(TaskOperation operation) throws Exception {
		try {
			// 1--保存脚本文件到本地
			List<TaskStep> steps = operation.getSteps();
			logger.info("begian save shellFile ,taskId:" + operation.getId() + " taskName:" + operation.getName());
			for (TaskStep step : steps) {
				String shellName = step.getShellName();
				if (null == shellName) {
					logger.warn("save task without shell, stepId:" + step.getId() + " stepName:" + step.getName());
					continue;
				}
				String fileUrl = TaskConfig.TASK_SHELL_BASE_PATH + operation.getId() + File.separator + shellName;
				String shellTxt = step.getShellTxt();
				// InputStream inputStream = new
				// ByteArrayInputStream(shellTxt.getBytes());
				savefile(fileUrl, shellTxt);
				logger.info("save shellFile success, file:" + fileUrl);
			}
			// 2--保存参数到本地
			logger.info("begin save paramFile , taskId:" + operation.getId() + " taskName:" + operation.getName());
			writeParamToFile(operation);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("保存文件失败！");
		}

	}*/

	/**
	 * 
	 * @param destFileName
	 * @return
	 */
	public static boolean createFile(String destFileName) {
		File file = new File(destFileName);
		if (file.exists()) {
			logger.info("file has exist, file:" + destFileName);
			file.delete();
			logger.info("delete file success!  file:" + destFileName);
		}

		if (destFileName.endsWith(File.separator)) {
			logger.error("create file  fail " + destFileName + "！ file can not to be a dir!");
			return false;
		}
		// 判断目标文件所在的目录是否存在
		if (!file.getParentFile().exists()) {
			// 如果目标文件所在的目录不存在，则创建父目录
			if (!file.getParentFile().mkdirs()) {
				logger.error("create file dir fail！" + destFileName);
				return false;
			}
		}
		// 创建目标文件
		try {
			if (file.createNewFile()) {
				logger.info("create blank file success " + destFileName + " ！");
				return true;
			} else {
				logger.error("create blank file fail " + destFileName + " ！");
				return false;
			}
		} catch (IOException e) {
			logger.error(e);
			logger.error("create file error " + destFileName + " error!");
			return false;
		}
	}

	public static Document loadXmlFile(String path) throws ParserConfigurationException, SAXException, IOException {
		File f = new File(path);
		if (!f.exists()) {
			logger.error("resource not found, file" + f.getAbsolutePath());
			throw new FileNotFoundException("can not find the resource file filePath:" + path);
		}
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = db.parse(f);
		return doc;
	}

	public static Properties LoadPropFile(String path) throws IOException {
		Properties prop = new Properties();
		InputStream in = null;
		try {
			//in = new FileInputStream(ClassLoader.getSystemResource(path).getPath());
			in = TaskFileUtil.class.getClassLoader().getResource(path).openStream();
			prop.load(in);
		} catch (FileNotFoundException e) {
			logger.error("can not find file path:" + path);
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			if(null != in){
				in.close();
			}
		}
		return prop;
	}

	public static void copyFolder(String oldPath, String newPath) throws Exception {
		logger.info("copy file from "+oldPath +" to "+newPath);
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			if(null == file){
				logger.error("文件路径错误!");
				logger.error("file not found,path:"+oldPath);
				throw new Exception("文件路径错误!");
			}
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					input = new FileInputStream(temp);
					output = new FileOutputStream(newPath + File.separator + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + File.separator + file[i], newPath + File.separator + file[i]);
				}
			}
		} catch (IOException e) {
			logger.error("copy file error! sourcePath:"+oldPath +" dstPath:"+newPath);
			logger.error(e.getMessage(),e);
			throw new Exception("复制文件出错!");
		}
		finally
		{
			if(output!=null)
			output.close();
			if(input!=null)
			input.close();
		}
	}
	
	/**
	 * 获取步骤的脚本
	 */
	public static String getStepShell(TaskStep step) throws IOException
	{
		if(null != step.getShellName()){
			String filePath = TaskConfig.TASK_SHELL_BASE_PATH+step.getTaskId()+File.separator+step.getShellName();
			return readFile(filePath);
		}
		return "";
	}
	
	/**
	 * 获取模板步骤的脚本
	 * @param step
	 * @return
	 * @throws IOException
	 */
	public static String getStepTplShell(TaskStep step) throws IOException
	{
		String filePath = TaskConfig.TASK_TPL_BASE_PATH+step.getTaskId()+File.separator+step.getShellName();
		return readFile(filePath);
	}
	
	private static String readFile(String path) throws IOException
	{
		StringBuilder strB = new StringBuilder();
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(new File(path));
			br = new BufferedReader(fr);
			String line =null;
			 while(true){
		            line =br.readLine();
		            if(line ==null){
		                break;
		            }
		            strB.append(line).append("\n");
		        }
			fr.close();
			br.close();
		} catch ( IOException e) {
			logger.error("read fiel error,filePath:"+path);
			return null;
		}finally{
			if(null != fr)
			fr.close();
			if(null != br)
			br.close();
		}
		return strB.toString();
	}
}
