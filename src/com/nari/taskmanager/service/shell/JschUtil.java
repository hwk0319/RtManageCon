package com.nari.taskmanager.service.shell;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class JschUtil {

	private static final Logger logger = Logger.getLogger(JschUtil.class);

	private String charset = "UTF-8"; // 设置编码格式,可以根据服务器的编码设置相应的编码格式
	private JSch jsch;
	private Session session;
	Channel channel = null;
	ChannelSftp chSftp = null;

	// 初始化配置参数
	private String jschHost = "";
	private int jschPort = 10022;
	private String jschUserName = "";
	private String jschPassWord = "";
	private int jschTimeOut = 5000;

	public JschUtil(String jschUserName, String jschHost, String jschPassWord) throws JSchException {
		this.jschUserName = jschUserName;
		this.jschHost = jschHost;
		this.jschPassWord = jschPassWord;
		connect();
	}

	public JschUtil(String jschUserName, String jschHost, String jschPassWord, int timeout) {
		this.jschUserName = jschUserName;
		this.jschHost = jschHost;
		this.jschPassWord = jschPassWord;
		this.jschTimeOut = timeout;
	}

	/**
	 * 连接到指定的服务器
	 * 
	 * @return
	 * @throws JSchException
	 */
	public boolean connect() throws JSchException {
		jsch = new JSch();// 创建JSch对象
		boolean result = false;
		try {
			long begin = System.currentTimeMillis();// 连接前时间
			logger.debug("Try to connect to jschHost = " + jschHost + ",as jschUserName = " + jschUserName
					+ ",as jschPort =  " + jschPort);
			session = jsch.getSession(jschUserName, jschHost, jschPort);// //
			session.setPassword(jschPassWord); 
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setTimeout(jschTimeOut);
			session.connect();
			logger.debug("Connected successfully to jschHost = " + jschHost + ",as jschUserName = " + jschUserName
					+ ",as jschPort =  " + jschPort);

			long end = System.currentTimeMillis();// 连接后时间

			result = session.isConnected();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (result) {
				logger.debug("connect success");
			} else {
				logger.debug("connect failure");
			}
		}

		if (!session.isConnected()) {
			logger.error("获取连接失败");
		}

		return session.isConnected();

	}

	/**
	 * 关闭连接
	 */
	public void close() {

		if (channel != null && channel.isConnected()) {
			channel.disconnect();
			channel = null;
		}

		if (session != null && session.isConnected()) {
			session.disconnect();
			session = null;
		}

	}

	/**
	 * 脚本是同步执行的方式 执行脚本命令
	 * 
	 * @param command
	 * @return
	 */
	public Map<String, Object> execCmmmand(String command) throws Exception {

		Map<String, Object> mapResult = new HashMap<String, Object>();

		logger.info(command);

		StringBuffer result = new StringBuffer();// 脚本返回结果

		BufferedReader reader = null;
		int returnCode = -2;// 脚本执行退出状态码

		try {
			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);

			channel.connect();// 执行命令 等待执行结束

			InputStream in = channel.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in, Charset.forName(charset)));

			String res = "";
			while ((res = reader.readLine()) != null) {
				result.append(res + "\n");
				logger.info(res);
			}
			returnCode = channel.getExitStatus();
			mapResult.put("returnCode", returnCode);
			mapResult.put("result", result.toString());
		} catch (IOException e) {

			logger.error(e.getMessage(), e);

		} catch (JSchException e) {

			logger.error(e.getMessage(), e);

		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}

		return mapResult;

	}

	public void upLoadFile(String lPath, String dPath) throws Exception {
		logger.debug("upload file from local:" + lPath + " to " + session.getHost() + ":" + dPath);
		try {
			channel = session.openChannel("sftp"); // 打开SFTP通道
			channel.connect(); // 建立SFTP通道的连接
			chSftp = (ChannelSftp) channel;
			createDir(dPath, chSftp);
			File file = new File(lPath);
			copyFile(chSftp, file, chSftp.pwd());
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			session.disconnect();
			channel.disconnect();
		}
	}

	public void copyFile(ChannelSftp sftp, File localFile, String remotePath) throws Exception {
		if (localFile.isDirectory()) {
			File[] list = localFile.listFiles();
			try {
				logger.debug("path: cd " + localFile.getName());
				sftp.cd(localFile.getName());
			} catch (Exception e) {
				logger.debug("path: mkdir " + localFile.getName());
				sftp.mkdir(localFile.getName());
				logger.debug("path: cd " + localFile.getName());
				sftp.cd(localFile.getName());
			}
			for (int i = 0; i < list.length; i++) {
				copyFile(sftp, list[i], sftp.pwd());
			}
		} else {
			InputStream instream = null;
			OutputStream outstream = null;
			try {
				logger.debug("copy file " + localFile.getAbsolutePath() + " to " + session.getHost() + ":" + sftp.pwd());
				outstream = sftp.put(localFile.getName(), ChannelSftp.OVERWRITE);
				instream = new FileInputStream(localFile);
				byte b[] = new byte[1024];
				int n;
				try {
					while ((n = instream.read(b)) != -1) {
						outstream.write(b, 0, n);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (SftpException e) {
				throw e;
			} finally {
				if (null != outstream) {
					outstream.flush();
					outstream.close();
				}
				if (null != instream) {
					instream.close();
				}
			}
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param directory
	 *            下载的目录,有两种写法 １、如/opt，拿到则是默认文件名 ２、/opt/文件名，则是另起一个名字
	 * @param downloadFile
	 *            要下载的文件 如/opt/xxx.txt
	 * 
	 */

	public void download(String directory, String downloadFile) {
		try {
			connect();
			logger.debug("Opening Channel.");
			channel = session.openChannel("sftp"); // 打开SFTP通道
			channel.connect(); // 建立SFTP通道的连接
			chSftp = (ChannelSftp) channel;
			SftpATTRS attr = chSftp.stat(downloadFile);
			long fileSize = attr.getSize();
			OutputStream out = new FileOutputStream(directory);
			InputStream is = chSftp.get(downloadFile, new MyProgressMonitor());
			byte[] buff = new byte[1024 * 2];
			int read;
			if (is != null) {
				logger.debug("Start to read input stream");
				do {
					read = is.read(buff, 0, buff.length);
					if (read > 0) {
						out.write(buff, 0, read);
					}
					out.flush();
				} while (read >= 0);
				logger.debug("input stream read done.");
			}
			logger.debug("成功下载文件至" + directory);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			chSftp.quit();
			if (channel != null) {
				channel.disconnect();
				logger.debug("channel disconnect");
			}
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param deleteFile
	 *            要删除的文件
	 */
	public void delete(String deleteFile) {

		try {
			logger.debug("Opening Channel.");
			channel = session.openChannel("sftp"); // 打开SFTP通道
			channel.connect(); // 建立SFTP通道的连接
			chSftp = (ChannelSftp) channel;
			chSftp.rm(deleteFile);
			logger.debug("成功删除文件" + deleteFile);
		} catch (Exception e) {
			logger.error(e);
		}

	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void createDir(String createpath, ChannelSftp sftp) throws Exception {
		if (isDirExist(createpath, sftp)) {
			sftp.cd(createpath);
			return;
		}
		logger.debug("create dir " + createpath);
		String[] folders = createpath.split("/");
		sftp.cd("/");
		for (String folder : folders) {
			if (folder.length() > 0) {
				try {
					sftp.cd(folder);
				} catch (SftpException e) {
					sftp.mkdir(folder);
					sftp.cd(folder);
				}
			}
		}
	}

	public boolean isDirExist(String directory, ChannelSftp sftp) {
		boolean isDirExistFlag = false;
		try {
			SftpATTRS sftpATTRS = sftp.lstat(directory);
			isDirExistFlag = true;
			return sftpATTRS.isDir();
		} catch (SftpException e) {
			if (e.getMessage().toLowerCase().equals("no such file")) {
				isDirExistFlag = false;
			}
		}
		return isDirExistFlag;
	}
}
