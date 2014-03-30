package clc.logic;

import static clc.common.Constants.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import clc.storage.History;
import clc.storage.Storage;


public class Update implements Command {
	
    private int seqNo;
    private int caseCalendarProvided;
    private StringBuilder feedback = new StringBuilder();
    private String newTaskName = null;
    private Task task;
    private ArrayList<GregorianCalendar> time;
    private Calendar newStartTime = null, newEndTime = null;
    private ArrayList<Integer> displayMem;
    private ArrayList<Task> internalMem;
   
	//constructor
	public Update(int seqNo, String newTaskName) {
		this.seqNo = seqNo;
		this.newTaskName = newTaskName;
		displayMem = Storage.getDisplayMem();
		internalMem = Storage.getInternalMem();
	}
	public Update(int seqNo, int caseCalendarProvided, ArrayList<GregorianCalendar> time){
		this.seqNo = seqNo;
		this.caseCalendarProvided = caseCalendarProvided;
		this.time = time;
		displayMem = Storage.getDisplayMem();
		internalMem = Storage.getInternalMem();
		newStartTime = time.get(0);
		newEndTime = time.get(1);
	}
	@Override
	public String execute() {
		if (isDataEmpty()) {
			feedback.append(MESSAGE_NO_TASK_TO_UPDATE);
			feedback.append("\n");
		} else if (isOutOfBound()) {
			feedback.append(MESSAGE_INEXISTANCE_SEQNO);
			feedback.append("\n");
		} else {
			int internalSeqNo = displayMem.get(seqNo - 1);
			task = internalMem.get(internalSeqNo);
            updateTask();
    		History.addNewVersion();
    		Storage.writeContentIntoFile();
		}
		
		return feedback.toString();
	}
	
	private void updateTask(){
		String taskName = task.getTaskName();
		int taskType = task.getTaskType();
		Calendar updateTime = null;
		
		//update name
		if (newTaskName != null){
            task.updateTaskName(newTaskName);
            feedback.append(String.format(MESSAGE_TASK_NAME_UPDATED_SUCCESS, taskName, newTaskName));
            feedback.append("\n");
		}
		
		//update start time
		if (newStartTime != null){
			Calendar taskOldStartTime;
			
			if (taskType == TYPE_TIMED_TASK){
				//update date
				if (caseCalendarProvided/8 == 1) {
					taskName = task.getTaskName();
					taskOldStartTime = task.getStartTime();
					updateTime = updateNewDate(taskOldStartTime, newStartTime);
				    task.updateStartTime(updateTime);
				    caseCalendarProvided -= 8;
				} 
				//update time
				if (caseCalendarProvided/4 == 1) {
					taskName = task.getTaskName();
					taskOldStartTime = task.getStartTime();
					updateTime = updateNewDate(newStartTime, taskOldStartTime);
				    task.updateStartTime(updateTime);
				    caseCalendarProvided -= 4;
				}
				
			} else if (taskType == TYPE_DEADLINE_TASK){
				task.updateStartTime(newStartTime);
				updateTime = task.getStartTime();
				task.setTaskType(TYPE_TIMED_TASK);
			} else {//for floating task, something not correct
				task.updateEndTime(newStartTime);
				updateTime = task.getEndTime();
				task.setTaskType(TYPE_DEADLINE_TASK);
			}
			
			String startTime = D_M_Y_DateFormatter.format(updateTime.getTime());
			feedback.append(String.format(MESSAGE_TASK_STARTTIME_UPDATED_SUCCESS, taskName, startTime, seqNo));
			feedback.append("\n");
		}
		
		//update end time
		if (newEndTime != null) {
			Calendar taskOldEndTime;
			if (taskType == TYPE_FLOATING_TASK){
				taskOldEndTime = task.getEndTime();
				if (taskOldEndTime == null){
					task.updateEndTime(newEndTime);
					updateTime = task.getEndTime();
					task.setTaskType(TYPE_DEADLINE_TASK);
				} else {
					task.updateStartTime(taskOldEndTime);
					task.updateEndTime(newEndTime);
					updateTime = task.getEndTime();
					task.setTaskType(TYPE_TIMED_TASK);
				}
			} else {
				//update date
				if (caseCalendarProvided/2 == 1) {
					taskName = task.getTaskName();
					taskOldEndTime = task.getEndTime();
					updateTime = updateNewDate(taskOldEndTime, newEndTime);
				    task.updateEndTime(updateTime);
				    caseCalendarProvided -= 2;
				} 
				
				//update time
				if (caseCalendarProvided == 1) {
					taskName = task.getTaskName();
					taskOldEndTime = task.getEndTime();
					updateTime = updateNewDate(newEndTime, taskOldEndTime);
				    task.updateEndTime(updateTime);
				}
			}
			
			String endTime = D_M_Y_DateFormatter.format(updateTime.getTime());
			feedback.append(String.format(MESSAGE_TASK_ENDTIME_UPDATED_SUCCESS, taskName, endTime, seqNo));
			feedback.append("\n");
		} 
		if(newTaskName == null && newStartTime == null && newEndTime == null){
			feedback.append(MESSAGE_NO_CHANGE);
			feedback.append("\n");
		}
	}
	
	private GregorianCalendar updateNewDate(Calendar oldTime, Calendar newTime) {
		int year = newTime.get(Calendar.YEAR);
	 	int month =	newTime.get(Calendar.MONTH);	
		int date = newTime.get(Calendar.DATE);
		int hour = oldTime.get(Calendar.HOUR_OF_DAY);
		int minute = oldTime.get(Calendar.MINUTE);
		GregorianCalendar updateTime = new GregorianCalendar(year, month, date, hour, minute);
		return updateTime;
	}
	
	// Check whether the data which can be processed is empty
	protected boolean isDataEmpty() {
		return displayMem.isEmpty();
	}
	
	protected boolean isOutOfBound() {
		return seqNo > displayMem.size();
	}
	
}	
	
	