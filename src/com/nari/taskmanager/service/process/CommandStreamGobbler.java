package com.nari.taskmanager.service.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class CommandStreamGobbler extends Thread {

	private Logger logger = Logger.getLogger(CommandStreamGobbler.class);
	private InputStream is;

	private String command;

	private String prefix = "";

	private volatile boolean readFinish = false;

	private volatile boolean ready = false;

	// 命令执行结果,0:执行中 1:超时
	private volatile int commandResult = 0;

	private List<String> infoList = new LinkedList<String>();

	public CommandStreamGobbler(InputStream is, String command, String prefix) {
		this.is = is;
		this.command = command;
		this.prefix = prefix;
	}

	public void run() {
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line = null;
			ready = true;
			while (commandResult != 1) {
				if (br.ready()) {
					if ((line = br.readLine()) != null) {
						infoList.add(line);
						//logger.info(prefix + " line: " + line);
					} else {
						readFinish = true;
						break;
					}
				} else {
					Thread.sleep(1000);
				}
				if(this.isInterrupted()){
					return;
				}
			}
				logger.info("read stream end  : " +prefix);
				readFinish = true;
		} catch (IOException | InterruptedException ioe) {
			logger.error("the command：" + command + ioe.getMessage());
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (isr != null) {
					isr.close();
				}
			} catch (IOException ioe) {
				logger.error("the command：" + command + ioe.getMessage());
			}
			readFinish = true;
		}
	}

	public InputStream getIs() {
		return is;
	}

	public String getCommand() {
		return command;
	}

	public boolean isReadFinish() {
		return readFinish;
	}

	public boolean isReady() {
		return ready;
	}

	public List<String> getInfoList() {
		return infoList;
	}

	public void setTimeout(int timeout) {
		this.commandResult = timeout;
	}
}