package clc.logic;

import static clc.common.Constants.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import clc.storage.Storage;



public class Add implements Command{
	Task task;

	public Add(Task task) {
		// TODO Auto-generated constructor stub
		this.task = task;
	}

	@Override
	public String execute() {
		
		internalMem.add(task);
		addNewVersion();
		Storage.writeContentIntoFile();
		
		/* //debug
		System.out.println("task name = " + task.getTaskName());
		System.out.println("task type = " + task.getTaskType());
		System.out.println("start time = " + task.getStartTime());
		System.out.println("end time = " + task.getEndTime());
		// */
		
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
	
	private String formatDate(Calendar calendar) {
		String date;
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy h.mm a");
		date = dateFormat.format(calendar.getTime());
		date = date.replaceAll("AM", "am");
		date = date.replaceAll("PM", "pm");
		return date;
	}
}
