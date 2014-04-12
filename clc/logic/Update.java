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
			feedback.append(NEWLINE);
		} else if (isOutOfBound()) {
			feedback.append(MESSAGE_INEXISTANCE_SEQNO);
			feedback.append(NEWLINE);
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
		String taskOldTypeString = task.taskTypeToString();
        Calendar taskOldStartTime = task.getStartTime(); 
		Calendar taskOldEndTime = task.getEndTime();
		
		Calendar updateTime = null;
		Calendar floatingTaskStartTime = null;
		//for checking start time >= end time
		Calendar startTimeForChecking = null;
		Calendar endTimeForChecking = null;
		
		//update name
		if (newTaskName != null){
            task.setTaskName(newTaskName);
            feedback.append(String.format(MESSAGE_TASK_NAME_UPDATED_SUCCESS, taskName, newTaskName));
            feedback.append(NEWLINE);
		}
		
		 //System.out.println(caseCalendarProvided);
		//update start time
		if (newStartTime != null){
			
			if (taskOldType == TYPE_TIMED_TASK){//for timed task
				//update date
				if (caseCalendarProvided/8 == 1) {
					taskName = task.getTaskName();
					updateTime = updateNewDate(taskOldStartTime, newStartTime);
				    task.setStartTime(updateTime);
				    caseCalendarProvided -= 8;
				} 
				//update time
				if (caseCalendarProvided/4 == 1) {
					taskName = task.getTaskName();
					updateTime = task.getStartTime();
					updateTime = updateNewDate(newStartTime, updateTime);
				    task.setStartTime(updateTime);
				    caseCalendarProvided -= 4;
				}
				//process feedback
				String startTime = D_M_Y_DateFormatter.format(updateTime.getTime());
				feedback.append(String.format(MESSAGE_TASK_STARTTIME_UPDATED_SUCCESS, taskName, startTime, seqNo));
				feedback.append(NEWLINE);
				
			} else if (taskOldType == TYPE_DEADLINE_TASK){//for deadline task
				task.setStartTime(newStartTime);
				updateTime = task.getStartTime();
				task.setTaskType(TYPE_TIMED_TASK);
				caseCalendarProvided %= 4;
				
				//process feedback
				String startTime = D_M_Y_DateFormatter.format(updateTime.getTime());
				feedback.append(String.format(MESSAGE_TASK_STARTTIME_UPDATED_SUCCESS, taskName, startTime, seqNo));
				feedback.append(NEWLINE);

				
			} else {//for floating task
				if(newEndTime != null){
					task.setStartTime(newStartTime);
					updateTime = task.getStartTime();
					//task.setTaskType(TYPE_DEADLINE_TASK);
					
					//process feedback
					String startTime = D_M_Y_DateFormatter.format(updateTime.getTime());
					feedback.append(String.format(MESSAGE_TASK_STARTTIME_UPDATED_SUCCESS, taskName, startTime, seqNo));
					feedback.append(NEWLINE);
				} else {
					
					//process feedback
					feedback.append(MESSAGE_ERROR_UPDATE_FLOATING);
					feedback.append(NEWLINE);
				}
			}
			
		}
		
		//update end time
		//System.out.println(newEndTime.getTime());
        //System.out.println(caseCalendarProvided);
		if (newEndTime != null) {
			if (taskOldType == TYPE_FLOATING_TASK){//for floating task
				floatingTaskStartTime = task.getStartTime();
				if (floatingTaskStartTime == null){
					task.setEndTime(newEndTime);
					updateTime = task.getEndTime();
					task.setTaskType(TYPE_DEADLINE_TASK);
					
					//process feedback
					String endTime = D_M_Y_DateFormatter.format(updateTime.getTime());
					feedback.append(String.format(MESSAGE_TASK_ENDTIME_UPDATED_SUCCESS, taskName, endTime, seqNo));
					feedback.append(NEWLINE);

					
				} else {
					task.setEndTime(newEndTime);
					updateTime = task.getEndTime();
					task.setTaskType(TYPE_TIMED_TASK);
					
					//process feedback
					String endTime = D_M_Y_DateFormatter.format(updateTime.getTime());
					feedback.append(String.format(MESSAGE_TASK_ENDTIME_UPDATED_SUCCESS, taskName, endTime, seqNo));
					feedback.append(NEWLINE);
					
				}
			}else if (taskOldType == TYPE_DEADLINE_TASK){// for deadline task
				task.setEndTime(newEndTime);
				updateTime = task.getEndTime();
				
				//process feedback
				String endTime = D_M_Y_DateFormatter.format(updateTime.getTime());
				feedback.append(String.format(MESSAGE_TASK_ENDTIME_UPDATED_SUCCESS, taskName, endTime, seqNo));
				feedback.append(NEWLINE);

			} else { // for timed task
				
				//update date
				if (caseCalendarProvided/2 == 1) {
					taskName = task.getTaskName();
					updateTime = updateNewDate(taskOldEndTime, newEndTime);
				    task.setEndTime(updateTime);
				    caseCalendarProvided -= 2;
				} 
				
				//update time
				if (caseCalendarProvided == 1) {
					taskName = task.getTaskName();
					updateTime = task.getEndTime();
					updateTime = updateNewDate(newEndTime, updateTime);
				    task.setEndTime(updateTime);
				}
				
				// process feedback
				String endTime = D_M_Y_DateFormatter.format(updateTime.getTime());
				//System.out.println(endTime);
				feedback.append(String.format(MESSAGE_TASK_ENDTIME_UPDATED_SUCCESS, taskName, endTime, seqNo));
				feedback.append(NEWLINE);
			}
		} 
		
		//check if task type changed
		int taskNewType = task.getTaskType();
		String taskNewTypeString = task.taskTypeToString();
		if(isTaskTypeChanged(taskOldType, taskNewType)){
			feedback.append(NEWLINE);
			feedback.append(String.format(MESSAGE_TASK_TYPE_CHANGED, seqNo ,taskName, taskOldTypeString, taskNewTypeString));
			feedback.append(NEWLINE);
		}
		
		
        //check if startTime before endTime
		startTimeForChecking = task.getStartTime();
		endTimeForChecking = task.getEndTime();
		
		if (isStartTimeLaterThanEndTime(startTimeForChecking, endTimeForChecking)) {
			
			String startTimeC = D_M_Y_DateFormatter.format(startTimeForChecking.getTime());
			String endTimeC = D_M_Y_DateFormatter.format(endTimeForChecking.getTime());
			
			task.setStartTime(taskOldStartTime);
			task.setEndTime(taskOldEndTime);
			task.setTaskType(taskOldType);
			
			feedback.append(NEWLINE);
			feedback.append(String.format(MESSAGE_UPDATE_TIME_ERROR, startTimeC, endTimeC));
			feedback.append(NEWLINE);
		}
		
		//check if nothing changes
		if(newTaskName == null && newStartTime == null && newEndTime == null){
			feedback.append(MESSAGE_NO_CHANGE);
			feedback.append(NEWLINE);
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
	
	protected boolean isStartTimeLaterThanEndTime(Calendar startTime, Calendar endTime) {
		return startTime != null && startTime.compareTo(endTime) >= 0;
	}
	
	protected boolean isTaskTypeChanged(int taskOldType, int taskNewType) {
		return taskOldType != taskNewType;
	}
}	
	
	