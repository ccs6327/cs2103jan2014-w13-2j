package clc.logic;

import static clc.common.Constants.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import clc.storage.Storage;


public class Update implements Command {
	
    private int seqNo;
    private StringBuilder feedback = new StringBuilder();
    private String newTaskName = null;
    private Task task;
    private ArrayList<GregorianCalendar> time;
    private Calendar newStartTime = null, newEndTime = null;
   
	//constructor
	public Update(int seqNo, String newTaskName) {
		this.seqNo = seqNo;
		this.newTaskName = newTaskName;
	}
	public Update(int seqNo, ArrayList<GregorianCalendar> time){
		this.seqNo = seqNo;
		this.time = time;
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
			task = displayMem.get(seqNo - 1);
            updateTask();
    		addNewVersion();
    		Storage.writeContentIntoFile();
		}
		
		return feedback.toString();
	}
	
	private void updateTask(){
		if (newTaskName != null){
            task.updateTaskName(newTaskName);
            feedback.append(String.format(MESSAGE_TASK_NAME_UPDATED_SUCCESS, newTaskName));
            feedback.append("\n");
		}
		
		if (newStartTime != null) {
		    task.updateStartTime(newStartTime);
		    String startTime = D_M_Y_DateFormatter.format(newStartTime.getTime());
		    feedback.append(String.format(MESSAGE_TASK_STARTTIME_UPDATED_SUCCESS, startTime));
		    feedback.append("\n");
		} 
		
		if (newEndTime != null) {
			task.updateEndTime(newEndTime);
			String endTime = D_M_Y_DateFormatter.format(newEndTime.getTime());
			feedback.append(String.format(MESSAGE_TASK_ENDTIME_UPDATED_SUCCESS, endTime));
			feedback.append("\n");
		} 
		
		if(newTaskName == null && newStartTime == null && newEndTime == null){
			feedback.append(MESSAGE_NO_CHANGE);
			feedback.append("\n");
			
		}
	}
	
	// Check whether the data which can be processed is empty
	protected boolean isDataEmpty() {
		return displayMem.isEmpty();
	}
	
	protected boolean isOutOfBound() {
		return seqNo > displayMem.size();
	}
}	
	
	