package clc.logic;

//@author A0105749Y
/**
 * This class is used to update new information to task.
 * 
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static clc.common.Constants.MESSAGE_TASK_NAME_UPDATED_SUCCESS; 
import static clc.common.Constants.MESSAGE_TASK_STARTTIME_UPDATED_SUCCESS;
import static clc.common.Constants.MESSAGE_TASK_ENDTIME_UPDATED_SUCCESS;
import static clc.common.Constants.MESSAGE_TASK_TYPE_CHANGED;
import static clc.common.Constants.MESSAGE_NO_TASK_TO_UPDATE;
import static clc.common.Constants.MESSAGE_INEXISTANCE_SEQNO;
import static clc.common.Constants.MESSAGE_NO_CHANGE;
import static clc.common.Constants.TYPE_FLOATING_TASK;
import static clc.common.Constants.TYPE_DEADLINE_TASK;
import static clc.common.Constants.TYPE_TIMED_TASK;
import static clc.common.Constants.MESSAGE_FLOATING_TASK_UPDATED_ERROR;
import static clc.common.Constants.MESSAGE_UPDATE_TIME_ERROR;
import static clc.common.Constants.NEWLINE;
import static clc.common.Constants.D_M_Y_DateFormatter;

import clc.common.InvalidInputException;
import clc.common.LogHelper;
import clc.storage.History;
import clc.storage.Storage;


public class Update implements Command {
	
    private int seqNo;
    private int caseCalendarProvided;
    private Task task; 
    private Task taskCopy;
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
			taskCopy = task.getNewCopy();
			
		    try {    
			    updateTask();
				taskIfStartTimeLaterThanOrEqualToEndTime(task);
				
			} catch (InvalidInputException iie) {
	            internalMem.set(internalSeqNo, taskCopy);
				return iie.getMessage();
			}
            
            //have a backup for undo operation
    		History.addNewVersion();
    		Storage.writeContentIntoFile();
		}
		
		assert feedback != null;
		return feedback.toString();
	}
	
	private void updateTask() throws InvalidInputException{
		
		String taskName = task.getTaskName();
		int taskOldType = task.getTaskType();
		String taskOldTypeString = task.taskTypeToString();
        Calendar taskOldStartTime = task.getStartTime(); 
		Calendar taskOldEndTime = task.getEndTime();
		
		Calendar updateTime = null;
		Calendar floatingTaskStartTime = null;
		
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
				//process feedback
				appendTaskStartTimeUpdatedMessage(feedback, task, seqNo);
				
			} else if (taskOldType == TYPE_DEADLINE_TASK){//for deadline task
				task.setStartTime(newStartTime);
				updateTime = task.getStartTime();
				task.setTaskType(TYPE_TIMED_TASK);
				caseCalendarProvided %= 4;
				
				//process feedback
				appendTaskStartTimeUpdatedMessage(feedback, task, seqNo);

				
			} else {//for floating task
				if(newEndTime != null){
					task.setStartTime(newStartTime);
					updateTime = task.getStartTime();
					
					//process feedback
					appendTaskStartTimeUpdatedMessage(feedback, task, seqNo);
					
				} else {
					LogHelper.warning("user updates floating task's start time only");
					throw new InvalidInputException(MESSAGE_FLOATING_TASK_UPDATED_ERROR);
				}
			}
			
		}
		
		//update end time
		if (isUpdatedEndTimeCase(newEndTime)) {
			if (taskOldType == TYPE_FLOATING_TASK){//for floating task
				floatingTaskStartTime = task.getStartTime();
				if (floatingTaskStartTime == null){
					task.setEndTime(newEndTime);
					updateTime = task.getEndTime();
					task.setTaskType(TYPE_DEADLINE_TASK);	
					
				} else {
					task.setEndTime(newEndTime);
					updateTime = task.getEndTime();
					task.setTaskType(TYPE_TIMED_TASK);
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
			}
			// process feedback
			appendTaskEndTimeUpdatedMessage(feedback, task, seqNo);
		} 

		//check if task type changed
		int taskNewType = task.getTaskType();
		String taskNewTypeString = task.taskTypeToString();
		if(isTaskTypeChanged(taskOldType, taskNewType)){
			LogHelper.warning(String.format(MESSAGE_TASK_TYPE_CHANGED, seqNo ,taskName, taskOldTypeString, taskNewTypeString));
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
	
	private void appendTaskUpdatedNewNameMessage(StringBuilder feedback, String taskOldName, String taskNewName) {
	       feedback.append(String.format(MESSAGE_TASK_NAME_UPDATED_SUCCESS, taskOldName, taskNewName));
           feedback.append(NEWLINE);
	}
	
	private void appendTaskStartTimeUpdatedMessage(StringBuilder feedback,Task task, int seqNo){
		String taskName = task.getTaskName();
		String startTime = D_M_Y_DateFormatter.format(task.getStartTime().getTime());
		feedback.append(String.format(MESSAGE_TASK_STARTTIME_UPDATED_SUCCESS, taskName, startTime, seqNo));
		feedback.append(NEWLINE);
	}
	
	private void appendTaskEndTimeUpdatedMessage(StringBuilder feedback,Task task, int seqNo){
		String taskName = task.getTaskName();
		String endTime = D_M_Y_DateFormatter.format(task.getEndTime().getTime());
		feedback.append(String.format(MESSAGE_TASK_ENDTIME_UPDATED_SUCCESS, taskName, endTime, seqNo));
		feedback.append(NEWLINE);
	}
    
    // check if task start time is later or equal than task end time, throw error message
	private static void taskIfStartTimeLaterThanOrEqualToEndTime(Task task) throws InvalidInputException {
		Calendar startTime = task.getStartTime();
		Calendar endTime = task.getEndTime();
		if (startTime != null && startTime.compareTo(endTime) >= 0) {
			LogHelper.warning("Start time is later than or equal to end time");
			throw new InvalidInputException(MESSAGE_UPDATE_TIME_ERROR);
		}
	}
	
	private boolean isDataEmpty() {
		return displayMem.isEmpty();
	}
	
	private boolean isOutOfBound() {
		return seqNo > displayMem.size();
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

	
	