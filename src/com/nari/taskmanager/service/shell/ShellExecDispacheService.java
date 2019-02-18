package com.nari.taskmanager.service.shell;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.po.TaskStep;

@Service
@Scope("prototype")
public class ShellExecDispacheService implements BeanFactoryAware {
	private static Logger logger = Logger.getLogger(ShellExecDispacheService.class);
	
	//@Autowired
	//private ShellCommandService shellCommandService;
	private BeanFactory factory;

	public ShellExecDispacheService(){
		
	}

	public void exec(TaskOperation operation) {
		// 如果第一步的状态不为init，说明任务已经分发，跳过这一步
		List<TaskStep> steps = operation.getSteps();
		TaskStep step1 = steps.get(0);
		if (step1.getState() != TaskConfig.TASK_STATE_INIT) {
			return;
		}
		//ShellCommandService shellCommandService = (ShellCommandService)applicationContext.getBean("shellCommandService");
		ShellCommandService shellCommandService = (ShellCommandService)factory.getBean("shellCommandService");
		shellCommandService.setOperation(operation);
		shellCommandService.setShellKey(ShellBuilderFactory.INIT_SHELL);
		shellCommandService.setStep(null);
		Thread initShellThread = new Thread(shellCommandService);
		initShellThread.setName("shell_thread_"+operation.getId());
		initShellThread.start();
		synchronized (operation) {
			try {
				operation.wait(120000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}
		logger.info("operation: " + operation.getName() + " waiting dispacher task end!");
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.factory = beanFactory;
		
	}

}
