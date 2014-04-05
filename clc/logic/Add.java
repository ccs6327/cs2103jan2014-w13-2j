package clc.logic;

import static clc.common.Constants.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import clc.storage.History;
import clc.storage.Storage;

public class Add implements Command{
	private Task task;
	private ArrayList<Task> internalMem;
	
	public Add() {
		internalMem = Storage.getInternalMem();
	}
	
	public Add(Task task) {
		this.task = task;
		internalMem = Storage.getInternalMem();
	}

	@Override
	public String execute() {
		
		internalMem.add(task);
		History.addNewVersion();
		Storage.writeContentIntoFile();
		
		switch (task.getTaskType()) {
		case TYPE_TIMED_TASK:
			return String.format(MESSAGE_TIMED_TASK_ADDED, task.getTaskName(), formatDate(task.getStartTime()), formatDate(task.getEndTime()));
			
		case TYPE_DEADLINE_TASK:
			return String.format(MESSAGE_DEADLINE_TASK_ADDED, task.getTaskName(), formatDate(task.getEndTime()));
			
		case TYPE_FLOATING_TASK:
			return String.format(MESSAGE_FLOATING_TASK_ADDED, task.getTaskName());
			
		default:
			return "Unhandled command";
		}
		
	}
	
	public void addOverdueRecurringTask() {
		int taskListSize = internalMem.size();
		for (int i = 0; i < taskListSize; i ++) {
			Task currentTask = internalMem.get(i);
			int nRecurring = currentTask.getRecurringTime();
			if (isRecurringAndOverDueTask(currentTask)) {
				task = new Task(currentTask);
				currentTask.setRecurringTime(0);
				task.setRecurringTime(--nRecurring);
				task.postponeStartAndEndTime(Calendar.WEEK_OF_YEAR, 1);
				internalMem.add(task);
			}
		}
		
	}

	private boolean isRecurringAndOverDueTask(Task currentTask) {
		int isOverdue = currentTask.getEndTime().compareTo(Calendar.getInstance());
		return isOverdue <=0 && currentTask.getRecurringTime() > 0;
	}
	
	private String formatDate(Calendar calendar) {
		String date;
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy h.mm a");
		date = dateFormat.format(calendar.getTime());
		date = date.replaceAll("AM", "am");
		date = date.replaceAll("PM", "pm");
		return date;
	}
}
