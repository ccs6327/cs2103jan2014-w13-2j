package clc.logic;

import static clc.common.Constants.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Update implements Command {
	
    private int seqNo;
    private String feedback;
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
			feedback = MESSAGE_NO_TASK_TO_UPDATE;
		} else if (isOutOfBound()) {
			feedback = MESSAGE_INEXISTANCE_SEQNO;
		} else {
			task = displayMem.get(seqNo - 1);
            updateTask();
		}
		
		return feedback;
	}
	
	private void updateTask(){
		if (newTaskName != null){
            task.updateTaskName(newTaskName);
            feedback = String.format(MESSAGE_TASK_NAME_UPDATED_SUCCESS, newTaskName);
		}
		
		if (newStartTime != null) {
		    task.updateStartTime(newStartTime);
		    String startTime = D_M_Y_DateFormatter.format(newStartTime.getTime());
		    feedback = String.format(MESSAGE_TASK_STARTTIME_UPDATED_SUCCESS, startTime);
		} 
		
		if (newEndTime != null) {
			task.updateEndTime(newEndTime);
			String endTime = D_M_Y_DateFormatter.format(newEndTime.getTime());
			feedback = String.format(MESSAGE_TASK_ENDTIME_UPDATED_SUCCESS, endTime);
		} 
		
		if(newTaskName == null && newStartTime == null && newEndTime == null){
			feedback = MESSAGE_NO_CHANGE;
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
	
	