package clc.logic;

//@author A0105749Y
/**
 * This class is used to update new information to task.
 * 
 */

import static clc.common.Constants.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import clc.storage.History;
import clc.storage.Storage;


public class Update implements Command {
	
    private int seqNo;
    private int caseCalendarProvided;
    private Task task; 
    private String newTaskName = null;
    private StringBuilder feedback = new StringBuilder();
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
		newStartTime = time.get(0);
		newEndTime = time.get(1);
		displayMem = Storage.getDisplayMem();
		internalMem = Storage.getInternalMem();
	}
	
	@Override
	public String execute() {
		if (isDataEmpty()) {
			appendTaskUpdatedMessage(feedback, MESSAGE_NO_TASK_TO_UPDATE);
			
		} else if (isOutOfBound()) {
			appendTaskUpdatedMessage(feedback, MESSAGE_INEXISTANCE_SEQNO);
			
		} else {
			int internalSeqNo = displayMem.get(seqNo - 1);
			task = internalMem.get(internalSeqNo);
            updateTask();
            
            //have a backup for undo
    		History.addNewVersion();
    		Storage.writeContentIntoFile();
		}
		
		assert feedback != null;
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
		if (isUpdatedNameCase(newTaskName)){
			updateTaskName(taskName, newTaskName);
		}
		
		//update start time
		if (isUpdatedStartTimeCase(newStartTime)){
			
			if (taskOldType == TYPE_TIMED_TASK){//for timed task
				//update date
				if (isStartDateUpdated(caseCalendarProvided)) {
					updateTime = updateNewDate(taskOldStartTime, newStartTime);
				    task.setStartTime(updateTime);
				    caseCalendarProvided -= 8;
				} 
				//update time
				if (isStartTimeUpdated(caseCalendarProvided)) {
					updateTime = task.getStartTime();
					updateTime = updateNewTime(updateTime, newStartTime);
				    task.setStartTime(updateTime);
				    caseCalendarProvided -= 4;
				}
				taskName = task.getTaskName();
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
		if (isUpdatedEndTimeCase(newEndTime)) {
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
			} else { // for deadline & timed task
				
				//update date
				if (isEndDateUpdated(caseCalendarProvided)) {
					updateTime = updateNewDate(taskOldEndTime, newEndTime);
				    task.setEndTime(updateTime);
				    caseCalendarProvided -= 2;
				} 
				
				//update time
				if (isEndTimeUpdated(caseCalendarProvided)) {
					updateTime = task.getEndTime();
					updateTime = updateNewTime(updateTime, newEndTime);
				    task.setEndTime(updateTime);
				}
				
				// process feedback
				taskName = task.getTaskName();
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
			appendTaskTypeChangedMessage(feedback, seqNo, taskName, taskOldTypeString, taskNewTypeString);
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
			appendTaskUpdatedMessage(feedback, MESSAGE_NO_CHANGE);
		}
	}
	
	private GregorianCalendar updateNewDate(Calendar oldTime, Calendar newTime) {
		int year = newTime.get(Calendar.YEAR);
	 	int month =	newTime.get(Calendar.MONTH);	
		int date = newTime.get(Calendar.DATE);
		int hour = oldTime.get(Calendar.HOUR_OF_DAY);
		int minute = oldTime.get(Calendar.MINUTE);
		GregorianCalendar dateUpdated = new GregorianCalendar(year, month, date, hour, minute);
		return dateUpdated;
	}
	
	private GregorianCalendar updateNewTime(Calendar oldTime, Calendar newTime){
	    return updateNewDate(newTime, oldTime);
	}
	
	private void updateTaskName(String taskOldName, String taskNewName){
            task.setTaskName(taskNewName);
            appendTaskUpdatedNewNameMessage(feedback, taskOldName, taskNewName);
	} 
	
    //append feedback method
	private void appendTaskUpdatedMessage(StringBuilder feedback, String message) {
		feedback.append(message);
		feedback.append(NEWLINE);
	}
	
	private void appendTaskTypeChangedMessage(StringBuilder feedback, int seqNo, String taskName, String taskOldType, String taskNewType) {
		feedback.append(NEWLINE);
		feedback.append(String.format(MESSAGE_TASK_TYPE_CHANGED, seqNo ,taskName, taskOldType, taskNewType));
		feedback.append(NEWLINE);
	}
	
	private void appendTaskUpdatedNewNameMessage(StringBuilder feedback, String taskOldName, String taskNewName) {
	       feedback.append(String.format(MESSAGE_TASK_NAME_UPDATED_SUCCESS, taskOldName, taskNewName));
           feedback.append(NEWLINE);
	}
	

	// Check whether the data which can be processed is empty
	private boolean isDataEmpty() {
		return displayMem.isEmpty();
	}
	
	private boolean isOutOfBound() {
		return seqNo > displayMem.size();
	}
	
	private boolean isStartTimeLaterThanEndTime(Calendar startTime, Calendar endTime) {
		return startTime != null && startTime.compareTo(endTime) >= 0;
	}
	
	private boolean isTaskTypeChanged(int taskOldType, int taskNewType) {
		return taskOldType != taskNewType;
	}
	
	private boolean isUpdatedNameCase(String taskNewName){
		return taskNewName != null;
	}
	
	private boolean isUpdatedStartTimeCase(Calendar newStartTime){
		return newStartTime != null;
	}
	
	private boolean isUpdatedEndTimeCase(Calendar newEndTime){
		return newEndTime != null;
	}
	
	private boolean isStartDateUpdated(int caseCalendarProvided) {
		return caseCalendarProvided/8 == 1;
	}
	
	private boolean isStartTimeUpdated(int caseCalendarProvided) {
		return caseCalendarProvided/4 == 1;
	}
	
	private boolean isEndDateUpdated(int caseCalendarProvided) {
		return caseCalendarProvided/2 == 1;
	}
	private boolean isEndTimeUpdated(int caseCalendarProvided) {
		return caseCalendarProvided == 1;
	}
}	

	
	