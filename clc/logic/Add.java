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
	
	public Add(Task task) {
		// TODO Auto-generated constructor stub
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
	
	public void checkAndAddRecurringTask() {
		
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
