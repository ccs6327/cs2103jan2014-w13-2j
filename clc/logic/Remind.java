package clc.logic;

import static clc.common.Constants.MESSAGE_REMIND_TIMED_TASKS;
import static clc.common.Constants.MESSAGE_REMIND_DEADLINE_TASKS;
import static clc.common.Constants.TYPE_DEADLINE_TASK;
import static clc.common.Constants.TYPE_TIMED_TASK;
import static clc.common.Constants.INTERVAL_TO_REMIND;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import clc.storage.Storage;

public class Remind {
	ArrayList<Task> internalMem;
	ArrayList<Date> timeToBeReminded;
	private ArrayList<Long> taskIdToBeReminded;
	static ArrayList<Task> reminderMem;

	public Remind() {
		internalMem = Storage.getInternalMem();
		timeToBeReminded = new ArrayList<Date>();
		reminderMem = new ArrayList<Task>();
		taskIdToBeReminded = new ArrayList<Long>();
	}

	public ArrayList<String> getToBeRemindedInfo() {
		ArrayList<String> taskInfo = new ArrayList<String>();
		for (int i = 0; i < internalMem.size(); i ++) {
			Task task = internalMem.get(i);
			if (task.getIsReminderNeeded() && !task.isDone()) {
				reminderMem.add(task);
				taskIdToBeReminded.add(task.getTaskId());
				switch (task.getTaskType()) {
				case TYPE_TIMED_TASK:
					remindTimedTask(taskInfo, task);
					break;
				case TYPE_DEADLINE_TASK:
					remindDeadlineTask(taskInfo, task);
					break;
				}
			}
		}
		return taskInfo;
	}

	private void remindDeadlineTask(ArrayList<String> taskInfo, Task task) {
		String endTime = formatDate(task.getEndTime());
		taskInfo.add(String.format(MESSAGE_REMIND_DEADLINE_TASKS, task.getTaskName(), endTime));
		Calendar reminderCalendar = (Calendar) task.getEndTime().clone();
		reminderCalendar.add(Calendar.MINUTE, INTERVAL_TO_REMIND);
		timeToBeReminded.add(reminderCalendar.getTime());
	}

	private void remindTimedTask(ArrayList<String> taskInfo, Task task) {
		String startTime = formatDate(task.getStartTime());
		String endTime = formatDate(task.getEndTime());
		taskInfo.add(String.format(MESSAGE_REMIND_TIMED_TASKS, task.getTaskName(), startTime, endTime));
		Calendar reminderCalendar = (Calendar) task.getStartTime().clone();
		reminderCalendar.add(Calendar.MINUTE, INTERVAL_TO_REMIND);
		timeToBeReminded.add(reminderCalendar.getTime());
	}

	public ArrayList<Date> getToBeRemindedTime() {
		return timeToBeReminded; 
	}

	public ArrayList<Long> getToBeRemindedTaskId() {
		return taskIdToBeReminded;
	}

	private String formatDate(Calendar calendar) {
		String date;
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy h.mm a");
		date = dateFormat.format(calendar.getTime());
		date = date.replaceAll("AM", "am");
		date = date.replaceAll("PM", "pm");
		return date;
	}
	
	protected static ArrayList<Task> getReminderMem() {
		return reminderMem;
	}
}
