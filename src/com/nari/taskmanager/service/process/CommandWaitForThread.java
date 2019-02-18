package com.nari.taskmanager.service.process;

import org.apache.log4j.Logger;

public class CommandWaitForThread extends Thread {
	private Logger logger= Logger.getLogger(CommandWaitForThread.class);
	private Process process;  
    private volatile boolean finish = false;  
    private volatile int exitValue = -1;  
  
    public CommandWaitForThread(Process process) {  
        this.process = process;  
    }  
  
    public void run() {  
        try {  
            this.exitValue = process.waitFor();  
        } catch (InterruptedException e) {  
            logger.error("the shell  thread was interrupted"); 
            logger.error(e.getMessage(),e);
        } finally {  
            finish = true;  
        }  
    }  
  
    public boolean isFinish() {  
        return finish;  
    }  
  
    public void setFinish(boolean finish) {  
        this.finish = finish;  
    }  
  
    public int getExitValue() {  
        return exitValue;  
    }  
  
}  