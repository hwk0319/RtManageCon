package com.nari.taskmanager.service.shell;

import org.apache.log4j.Logger;

import com.jcraft.jsch.SftpProgressMonitor;

public class MyProgressMonitor implements SftpProgressMonitor {  
	  
	  
    private static final Logger logger = Logger.getLogger(MyProgressMonitor.class);  
  
  
    private long transfered;  
  
  
    @Override  
    public boolean count(long count) {  
        transfered = transfered + count;  
        logger.debug("Currently transferred total size: " + transfered + " bytes");  
        return true;  
    }  
  
  
    @Override  
    public void end() {  
        logger.debug("Transferring done.");  
    }  
  
  
    @Override  
    public void init(int op, String src, String dest, long max) {  
        logger.debug("Transferring begin.");  
    }  
  
  
}  