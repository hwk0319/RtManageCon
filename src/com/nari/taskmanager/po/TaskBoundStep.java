package com.nari.taskmanager.po;

import java.util.ArrayList;
import java.util.List;

public class TaskBoundStep {
	private List<TaskStep> steps = new ArrayList<TaskStep>();
	private int timeOut;
	public List<TaskStep> getSteps() {
		return steps;
	}
	public void setSteps(List<TaskStep> steps) {
		this.steps = steps;
	}
	public int getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	public String getBoundName() {
		String name = "";
		for(TaskStep step:steps)
		{
			name += step.getName();
		}
		return name;
	}
	public int getBoundStepOrder() {
		for(TaskStep step:steps)
		{
			return step.getStepOrder();
		}
		return Integer.MIN_VALUE;
	}
}
