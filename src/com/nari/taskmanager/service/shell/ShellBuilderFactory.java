package com.nari.taskmanager.service.shell;

public class ShellBuilderFactory {

	/**
	 * 执行初始化的脚本。
	 */
	public static final int INIT_SHELL = 1;
	/**
	 * 执行远程的step脚本。
	 */
	public static final int STEP_SHELL = 2;

	public static ShellBuilder getShellBuilder(int shell_type) {
		ShellBuilder shellBuilder = null;
		if (shell_type == INIT_SHELL) {
			shellBuilder = new InitShellBuilder();
		} else if (shell_type == STEP_SHELL) {
			shellBuilder = new RemoteShellBuilder();

		}
		return shellBuilder;
	}
}
