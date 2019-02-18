package com.nari.taskmanager.service.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.po.TaskStep;
import com.nari.taskmanager.service.StateManageService;
import com.nari.taskmanager.service.TaskRunningConfigService;
import com.nari.taskmanager.service.process.CommandStreamGobbler;
import com.nari.taskmanager.service.process.CommandWaitForThread;

@Service
@Scope("prototype")
public class ShellCommandService implements Runnable {
	private static String USERNAME = "root";
	private static String PASSWD = "111111";
	private static String HOST = "192.168.41.44";// 192.168.200.172
	private static int DEFAULT_SSH_PORT = 10022;
	private TaskStep step;
	private TaskOperation operation;
	private int shellKey;
	private int timeout = 0;
	String stepName = "";

	@Autowired
	StateManageService stateManageService;

	@Autowired
	TaskRunningConfigService taskRunningConfigService;

	private Logger logger = Logger.getLogger(ShellCommandService.class);

	public ShellCommandService() {

	}

	public void run() {
		if (this.getShellKey() == ShellBuilderFactory.STEP_SHELL) {
			exec(operation, step);
		} else {
			exec(operation);
		}
	}

	public void exec(final TaskOperation operation) {
		logger.info("begin dispacher task to remote host!");
		final String commShellPath = TaskConfig.TASK_ROOT_PATH + TaskConfig.SHELL_COMMON_INIT_PATH;
		final String localTaskPath = TaskConfig.TASK_SHELL_BASE_PATH + operation.getId();// 本地任务脚本的目录
		final String remoteTaskPath = TaskConfig.REMOTE_TASK_PATH;

		Map<String, String> hostInfo = new HashMap<String, String>();
		try {
			NormalShellCommand norCmd = new NormalShellCommand(operation, null);
			hostInfo = norCmd.getShellDstHostInfo();
			if (null == hostInfo || hostInfo.isEmpty())
				throw new Exception("host number 0!");
		} catch (Exception e) {
			logger.error("get hostinfo error.", e);
			notifyTaskContinue("上传脚本文件失败!");
			return;
		}

		AtomicInteger at = new AtomicInteger(0);
		List<Thread> threads = new ArrayList<Thread>();

		for (Entry<String, String> entry : hostInfo.entrySet()) {
			final String hostIp = entry.getKey();
			final String passwd = entry.getValue();
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						JschUtil jschUtil = new JschUtil("root", hostIp, passwd);
						jschUtil.upLoadFile(commShellPath, remoteTaskPath);
						logger.info("upload commShell to " + hostIp + " success. commShellPath:" + remoteTaskPath);
						jschUtil = new JschUtil("root", hostIp, passwd);
						jschUtil.upLoadFile(localTaskPath, remoteTaskPath);
						logger.info("upload taskShell to " + hostIp + " success. shellPath:" + remoteTaskPath);
						taskRunningConfigService.insertTaskLog(operation.getId(), 0, hostIp, "info", "upload file success!");
					} catch (Exception e) {
						logger.error("upload file error!");
						logger.error(e.getMessage(), e);
						taskRunningConfigService.insertTaskLog(operation.getId(), 0, hostIp, "error", "upload file error!"+e.getMessage());
						notifyTaskContinue("上传脚本文件失败!");
					}
				}
			};
			t.setName("dispatch_task_Thread_" + operation.getName() + "_" + at.getAndIncrement());
			threads.add(t);
		}
		try {
			for (Thread t : threads) {
				t.start();
				t.join();
			}
			Thread.sleep(100);
		} catch (InterruptedException e) {
			logger.error("build new thread error!", e);
			notifyTaskContinue("上传脚本文件失败!");
			return;
		}

		synchronized (operation) {
			logger.info("notify task dispacher  success!");
			operation.notify();
		}
	}

	public void exec(final TaskOperation operation,final TaskStep step) {
		final String remoteTaskBasePath = TaskConfig.REMOTE_TASK_PATH + File.separator + operation.getId();
		final String remoteShellPath = remoteTaskBasePath + File.separator + step.getShellName();
		final String shellParamPath = remoteTaskBasePath + File.separator + TaskConfig.TASK_PARAM_FILE_NAME;
		Map<String, String> hostInfo = new HashMap<String, String>();
		try {
			NormalShellCommand norcmd = new NormalShellCommand(operation, step);
			hostInfo = norcmd.getShellDstHostInfo();
			if (null == hostInfo || hostInfo.isEmpty())
				throw new Exception("host number 0!");
			operation.setDstHostNum(hostInfo.size());
		} catch (Exception e) {
			logger.error("get hostinfo error.", e);
			notifyTaskContinue("执行脚本失败!");
			return ;
		}
		AtomicInteger at = new AtomicInteger(0);
		List<Thread> threads = new ArrayList<Thread>();
		for (Entry<String, String> entry : hostInfo.entrySet()) {
			final String hostIp = entry.getKey();
			final String passwd = entry.getValue();
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						JschUtil jschUtil = new JschUtil("root", hostIp, passwd);
						StringBuffer cmdBuf = new StringBuffer();
						cmdBuf.append("sh ").append(remoteShellPath).append(" ").append(shellParamPath).append(" ")
								.append(step.getTaskId()).append(" ").append(step.getId()).append(" \n");
						String command = cmdBuf.toString();
						jschUtil.execCmmmand(command);
						taskRunningConfigService.insertTaskLog(operation.getId(), step.getId(), hostIp, "info", "call remote shellscript sucess.");
					} catch (Exception e) {
						taskRunningConfigService.insertTaskLog(operation.getId(), step.getId(), hostIp, "error", "call remote shellscript error."+e.getMessage());
						notifyTaskContinue("执行脚本失败!");
						return;
					}
				}
			};
			t.setName("dispatch_task_Thread_" + operation.getName() + "_" + at.getAndIncrement());
			t.start();
			//threads.add(t);
			
		}
		/*for(Thread t : threads)
		{
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.error("build new thread error!", e);
				notifyTaskContinue("执行文件失败!");
				return;
			}

		}*/
	}

	public void execBashCommand1(String command) {
		Process process = null;
		try {
			logger.info("run command : " + command);
			process = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			logger.error("run the shell command error,command:" + command + e.getMessage());
			// throw new Exception("执行脚本失!");
			notifyTaskContinue("执行脚本失!");
			return;
		}
		String s = null;
		StringBuffer sb = new StringBuffer();
		InputStream std = process.getInputStream();
		InputStream err = process.getErrorStream();
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(std));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(err));
		try {
			while ((s = stdInput.readLine()) != null) {
				sb.append(s).append("\n");
			}
			while ((s = stdError.readLine()) != null) {
				sb.append(s).append("\n");
			}
			std.close();
			err.close();
			logger.info(sb.toString());
		} catch (IOException e1) {
			logger.error(e1);
		}

		try {
			process.waitFor();
			logger.info("shell command wait end.");
			if (0 != process.exitValue()) {
				logger.error("run the shell command error.errorcode:" + process.exitValue());
				taskRunningConfigService.insertTaskLog(operation.getId(), step == null ? 0 : step.getId(), "127.0.0.1",
						"ERROR", "exec shellscript error .code:" + process.exitValue());
				// throw new Exception("执行脚本失!");
				notifyTaskContinue("执行脚本失!");
				return;
			}
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}

	/**
	 * 命令先写入本地，然后再本地调用脚本。</br>
	 * 本地执行模式</br>
	 * 
	 * @param command
	 */
	private void execBashCommand(String command) {
		BufferedReader br = null;
		InputStream in = null;
		Process process = null;
		try {
			logger.info("run command : " + command);
			// String[] cmds = new String[] { "/bin/sh", "-c", command };
			process = Runtime.getRuntime().exec(command);
			CommandStreamGobbler errorGobbler = new CommandStreamGobbler(process.getErrorStream(), command, "ERR");
			errorGobbler.setName(Thread.currentThread().getName() + "-" + "ERR");
			CommandStreamGobbler outputGobbler = new CommandStreamGobbler(process.getInputStream(), command, "STD");
			outputGobbler.setName(Thread.currentThread().getName() + "-" + "STD");
			errorGobbler.start();
			// 必须先等待错误输出ready再建立标准输出
			while (!errorGobbler.isReady()) {
				Thread.sleep(10);
			}
			outputGobbler.start();
			while (!outputGobbler.isReady()) {
				Thread.sleep(10);
			}
			CommandWaitForThread commandThread = new CommandWaitForThread(process);
			commandThread.setName(Thread.currentThread().getName() + "-" + "WAIT");
			commandThread.start();
			long commandTime = new Date().getTime();
			long nowTime = new Date().getTime();
			boolean timeoutFlag = false;
			while (!commandThread.isFinish()) {
				if (nowTime - commandTime > timeout) {
					logger.info("time out true");
					timeoutFlag = true;
					break;
				} else {
					Thread.sleep(1000);
					nowTime = new Date().getTime();
				}
			}
			if (timeoutFlag) {
				// 命令超时
				errorGobbler.setTimeout(1);
				outputGobbler.setTimeout(1);
				logger.error("the command：" + command + " timeout");
			}
			while (true) {
				if (errorGobbler.isReadFinish() && outputGobbler.isReadFinish()) {
					break;
				}
				if (nowTime - commandTime > timeout) {
					logger.error("the command：" + command + " timeout");
					errorGobbler.interrupt();
					outputGobbler.interrupt();
					break;
				}

				Thread.sleep(100);
				nowTime = new Date().getTime();
			}
		} catch (IOException | InterruptedException e) {
			logger.error(e.getMessage(), e);
			// notifyTaskContinue();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					//
				}
			}
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					//
				}
			}
			if (null != process && process.isAlive()) {
				process.destroy();
			}

		}
	}

	/**
	 * 本地无阻塞的执行。
	 * 
	 * @param command
	 */
	private void execBashCommandWithOutBlocking(String command) {
		try {
			logger.info("run command : " + command);
			String[] cmds = new String[] { "/bin/sh", "-c", command };
			Runtime.getRuntime().exec(cmds);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			notifyTaskContinue(e.getMessage());
		}
	}

	/**
	 * window主机模拟返回
	 */
/*	private void runWindow() {
		// window
		// runSSHShellCommand(cmdBuf.toString());
		SoapClient client = new SoapClient();
		client.setTaskId(step.getTaskId());
		client.setStepId(step.getId());
		Thread t = new Thread(client);
		t.start();
	}*/

	private void notifyTaskContinue(String errMsg) {
		if (null != step) {
			logger.info("error notify step continue stepId:" + step.getId());
			synchronized (step) {
				step.setState(TaskConfig.TASK_STATE_ERROR);
				step.setResultDesc(errMsg);
				operation.setResultDesc(errMsg);
				stateManageService.notifyExector(step);
			}
			return;
		} else if (null != operation) {
			logger.info("error notify operation continue operationId:" + operation.getId());
			synchronized (operation) {
				operation.setState(TaskConfig.TASK_STATE_ERROR);
				operation.setResultDesc(errMsg);
				stateManageService.notifyExector(operation);
			}
			return;
		}
		else
		{
			logger.error("error");
		}
	}

	/**
	 * 远程命令执行模式。 ssh执行模式
	 * 
	 * @param command
	 * @return
	 */
	private boolean runSSHShellCommand(String command) {
		JSch jsch = new JSch();
		Channel channel = null;
		Session session = null;
		InputStream instream = null;
		OutputStream outstream = null;

		try {
			synchronized (step) {
				session = jsch.getSession(USERNAME, HOST, DEFAULT_SSH_PORT);
				session.setPassword(PASSWD);
				java.util.Properties config = new java.util.Properties();
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);
				session.connect();

				channel = session.openChannel("shell");
				channel.connect();
				instream = channel.getInputStream();
				outstream = channel.getOutputStream();
				outstream.write(command.getBytes());
				outstream.flush();
			}
			// shell 返回字符长度
			byte[] tmp = new byte[1024];
			// 获取命令执行的结果
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
				if (instream.available() > 0) {
					int i = instream.read(tmp, 0, 1024);
					if (i < 0)
						break;
					logger.info(new String(tmp, 0, i));
				} else {
					break;
				}

			}
			logger.info("end shell service, waiting for shell response.");

		} catch (Exception e) {
			step.setState(TaskConfig.TASK_STATE_ERROR);
			step.setResultDesc("调用shell命令失败！");
			logger.error(e.getMessage(), e);
			synchronized (step) {
				step.notify();// 如果调用shell命令失败，通知执行器不要等待。
			}
		} finally {
			if (null != outstream) {
				try {
					outstream.close();
				} catch (IOException ignore) {

				}
			}
			if (null != instream) {
				try {
					instream.close();
				} catch (IOException ignore) {
				}
			}
			if (null != channel) {
				channel.disconnect();
			}
			if (null != session) {
				session.disconnect();
			}
		}
		return true;
	}

	public TaskStep getStep() {
		return step;
	}

	public void setStep(TaskStep step) {
		this.step = step;
		if (null != step) {
			this.timeout = step.getTimeOut() * 1000;
			stepName = step.getName();
		} else {
			this.timeout = 120000;
			stepName = "dispacher";
		}
	}

	public TaskOperation getOperation() {
		return operation;
	}

	public void setOperation(TaskOperation operation) {
		this.operation = operation;
	}

	public int getShellKey() {
		return shellKey;
	}

	public void setShellKey(int shellKey) {
		this.shellKey = shellKey;
	}

}
