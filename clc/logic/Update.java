package clc.logic;

import static clc.common.Constants.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Update implements Command {
	
    private int taskNo;
    private String feedback;
    private String newTaskName = null;
    private Task task;
    private ArrayList<GregorianCalendar> time;
    private Calendar newStartTime = null, newEndTime = null;
   
	//constructor
	public Update(int taskNo, String newTaskName) {
		this.taskNo = taskNo;
		this.newTaskName = newTaskName;
	}
	public Update(int taskNo, ArrayList<GregorianCalendar> time){
		this.taskNo = taskNo;
		this.time = time;
		newStartTime = time.get(0);
		newEndTime = time.get(1);
	}
	@Override
	public String execute() {
		task = displayMem.get(taskNo-1);
		updateTask();
		return feedback;
	}
	
	private void updateTask(){
		if (newTaskName != null){
            task.updateTaskName(newTaskName);
            feedback = MESSAGE_TASK_NAME_UPDATED_SUCCESS;
		} 
		
		if (newStartTime != null) {
		    task.updateStartTime(newStartTime);
		    feedback = MESSAGE_TASK_STARTTIME_UPDATED_SUCCESS;
		} 
		
		if (newEndTime != null) {
			task.updateEndTime(newEndTime);
			feedback = MESSAGE_TASK_ENDTIME_UPDATED_SUCCESS;
		} 
	}
	
	// Check whether the data which can be processed is empty
	protected boolean isDataEmpty() {
		return displayMem.isEmpty();
	}
}	
	
	