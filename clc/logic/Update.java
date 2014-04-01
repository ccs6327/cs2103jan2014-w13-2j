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
		
		int taskOldType = task.getTaskType();
        Calendar taskOldStartTime = task.getStartTime(); 
		Calendar taskOldEndTime = task.getEndTime();
		
		Calendar updateTime = null;
		Calendar floatingTaskStartTime = null;
		//for checking start time >= end time
		Calendar startTimeForChecking = null;
		Calendar endTimeForChecking = null;
		
		//update name
		if (newTaskName != null){
            task.updateTaskName(newTaskName);
            feedback.append(String.format(MESSAGE_TASK_NAME_UPDATED_SUCCESS, taskName, newTaskName));
            feedback.append("\n");
		}
		
		//update start time
		if (newStartTime != null){
			
			if (taskOldType == TYPE_TIMED_TASK){//for timed task
				//update date
				if (caseCalendarProvided/8 == 1) {
					taskName = task.getTaskName();
					updateTime = updateNewDate(taskOldStartTime, newStartTime);
				    task.updateStartTime(updateTime);
				    caseCalendarProvided -= 8;
				} 
				//update time
				if (caseCalendarProvided/4 == 1) {
					taskName = task.getTaskName();
					updateTime = task.getStartTime();
					updateTime = updateNewDate(newStartTime, updateTime);
				    task.updateStartTime(updateTime);
				    caseCalendarProvided -= 4;
				}
				//process feedback
				String startTime = D_M_Y_DateFormatter.format(updateTime.getTime());
				feedback.append(String.format(MESSAGE_TASK_STARTTIME_UPDATED_SUCCESS, taskName, startTime, seqNo));
				feedback.append("\n");
				
			} else if (taskOldType == TYPE_DEADLINE_TASK){//for deadline task
				task.updateStartTime(newStartTime);
				updateTime = task.getStartTime();
				task.setTaskType(TYPE_TIMED_TASK);
				
				//process feedback
				String startTime = D_M_Y_DateFormatter.format(updateTime.getTime());
				feedback.append(String.format(MESSAGE_TASK_STARTTIME_UPDATED_SUCCESS, taskName, startTime, seqNo));
				feedback.append("\n");
				feedback.append("\n");
				feedback.append(String.format(MESSAGE_TASK_TYPE_CHANGED, taskName, UPDATE_DEADLINE_TASK, UPDATE_TIMED_TASK, seqNo));
				feedback.append("\n");
				
			} else {//for floating task
				if(newEndTime != null){
					task.updateStartTime(newStartTime);
					updateTime = task.getStartTime();
					//task.setTaskType(TYPE_DEADLINE_TASK);
					
					//process feedback
					String startTime = D_M_Y_DateFormatter.format(updateTime.getTime());
					feedback.append(String.format(MESSAGE_TASK_STARTTIME_UPDATED_SUCCESS, taskName, startTime, seqNo));
					feedback.append("\n");
				} else {
					
					//process feedback
					feedback.append(MESSAGE_ERROR_UPDATE_FLOATING);
					feedback.append("\n");
				}
			}
			
		}
		
		//update end time
		if (newEndTime != null) {

			if (taskOldType == TYPE_FLOATING_TASK){//for floating task
				floatingTaskStartTime = task.getStartTime();
				if (floatingTaskStartTime == null){
					task.updateEndTime(newEndTime);
					updateTime = task.getEndTime();
					task.setTaskType(TYPE_DEADLINE_TASK);
					
					//process feedback
					String endTime = D_M_Y_DateFormatter.format(updateTime.getTime());
					feedback.append(String.format(MESSAGE_TASK_ENDTIME_UPDATED_SUCCESS, taskName, endTime, seqNo));
					feedback.append("\n");
					feedback.append("\n");
					feedback.append(String.format(MESSAGE_TASK_TYPE_CHANGED, taskName, UPDATE_FLOATING_TASK, UPDATE_DEADLINE_TASK, seqNo));
					feedback.append("\n");
					
				} else {
					task.updateEndTime(newEndTime);
					updateTime = task.getEndTime();
					task.setTaskType(TYPE_TIMED_TASK);
					
					//process feedback
					String endTime = D_M_Y_DateFormatter.format(updateTime.getTime());
					feedback.append(String.format(MESSAGE_TASK_ENDTIME_UPDATED_SUCCESS, taskName, endTime, seqNo));
					feedback.append("\n");
					feedback.append("\n");
					feedback.append(String.format(MESSAGE_TASK_TYPE_CHANGED, taskName, UPDATE_FLOATING_TASK, UPDATE_TIMED_TASK, seqNo));
					feedback.append("\n");
					
				}
			} else {
				
				//update date
				if (caseCalendarProvided/2 == 1) {
					taskName = task.getTaskName();
					updateTime = updateNewDate(taskOldEndTime, newEndTime);
				    task.updateEndTime(updateTime);
				    caseCalendarProvided -= 2;
				} 
				
				//update time
				if (caseCalendarProvided == 1) {
					taskName = task.getTaskName();
					updateTime = task.getEndTime();
					updateTime = updateNewDate(newEndTime, updateTime);
				    task.updateEndTime(updateTime);
				}
				
				// process feedback
				String endTime = D_M_Y_DateFormatter.format(updateTime.getTime());
				feedback.append(String.format(MESSAGE_TASK_ENDTIME_UPDATED_SUCCESS, taskName, endTime, seqNo));
				feedback.append("\n");
			}
		} 
		
		
        //check if startTime before endTime
		startTimeForChecking = task.getStartTime();
		endTimeForChecking = task.getEndTime();
		if (startTimeForChecking == null ||startTimeForChecking.compareTo(endTimeForChecking) < 0) {
			feedback.append("\n");
			
		}else {
			
			String startTimeC = D_M_Y_DateFormatter.format(startTimeForChecking.getTime());
			String endTimeC = D_M_Y_DateFormatter.format(endTimeForChecking.getTime());
			
			task.updateStartTime(taskOldStartTime);
			task.updateEndTime(taskOldEndTime);
			task.setTaskType(taskOldType);
			
			feedback.append("\n");
			feedback.append(String.format(MESSAGE_UPDATE_TIME_ERROR, startTimeC, endTimeC));
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
	
	