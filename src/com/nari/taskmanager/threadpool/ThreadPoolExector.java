package com.nari.taskmanager.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolExector {
	private static final int MAXPOOLSIZE = 5;
	public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(MAXPOOLSIZE);
}
