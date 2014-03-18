package clc.logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import clc.storage.Storage;
import static clc.common.Constants.*;

public class Display implements Command {
    private StringBuilder feedback = new StringBuilder();
    private String commandDetails = null;
    private ArrayList<GregorianCalendar> time;
    private Calendar startOfPeriod = null, endOfPeriod = null;
	private ArrayList<Task> internalMem;
	private ArrayList<Integer> displayMem;
	
	public Display(ArrayList<GregorianCalendar> time) {
		this.time = time;
		internalMem = Storage.getInternalMem();
		displayMem = Storage.getDisplayMem();
	}
	
	public Display(String commandDetails) {
		this. commandDetails = commandDetails;
		internalMem = Storage.getInternalMem();
		displayMem = Storage.getDisplayMem();
	} 
	 
	@Override
	public String execute() {
		
		DisplayOutput.clear();
		displayMem.clear();
		
		if(internalMem.isEmpty()) {
			feedback.append("\n");
			feedback = feedback.append(MESSAGE_EMPTY_LIST);
			feedback.append("\n");
		} else if (commandDetails!= null) {
			
			switch (commandDetails){
			case TYPE_DISPLAY_ALL:
				displayAllTasks();
				break;
			case TYPE_DISPLAY_FLOATING_TASK:
				displayFloatingTasks();
				break;	
			case TYPE_DISPLAY_DEADLINE_TASK:
				displayDeadlineTasks();
				break;
			case TYPE_DISPLAY_TIMED_TASK:
				displayTimedTasks();
				break;
			default:	
			    feedback.append(String.format(MESSAGE_INVALID_FORMAT, commandDetails));
			}
			
		} else {
			displayInPeriod();
		}		
		assert feedback != null;
		return feedback.toString();
	}
	
	//display all tasks
	private void displayAllTasks(){
		DisplayOutput.add(MESSAGE_SHOW_ALL_TASKS);

	    for (int i = 1; i<= internalMem.size(); i++){
	    	
	    	Task task = internalMem.get(i - 1);
	    	if(task.getTaskType() == TYPE_TIMED_TASK){
	    		
	    		String startTime = D_M_Y_DateFormatter.format(task.getStartTime().getTime());
	    		String endTime = D_M_Y_DateFormatter.format(task.getEndTime().getTime());
	    		DisplayOutput.add(String.format(MESSAGE_OUTPUT_TIMED_TASKS, i, task.getTaskName(), startTime, endTime));
	    		
	    	} else if (task.getTaskType() == TYPE_DEADLINE_TASK){
	    		
	    		String endTime = D_M_Y_DateFormatter.format(task.getEndTime().getTime());
	    		DisplayOutput.add(String.format(MESSAGE_OUTPUT_DEADLINE_TASKS, i, task.getTaskName(), endTime));
	    	} else {
	    		DisplayOutput.add(String.format(MESSAGE_OUTPUT_FLOATING_TASKS, i, task.getTaskName()));
	    	}
	    	displayMem.add(i - 1);
	    }
	    
	    feedback.append("\n");
	    printOutDisplay();
	    feedback.append("\n");
	    feedback.append(MESSAGE_DISPLAY);
	    feedback.append("\n");
	}
	
	//display floating tasks
	private void displayFloatingTasks(){
		int displayNo = 1;
		DisplayOutput.add(MESSAGE_SHOW_FLOATING_TASKS);

	    for (int i = 1; i<= internalMem.size(); i++){
	    	Task task = internalMem.get(i - 1);
	    	if(task.getTaskType() == TYPE_FLOATING_TASK){
	    		DisplayOutput.add(String.format(MESSAGE_OUTPUT_FLOATING_TASKS, displayNo, task.getTaskName()));
	    		displayMem.add(i - 1);
	    		displayNo++;
	    	}
	    }
	    
		feedback.append("\n");
		printOutDisplay();
		
		// Process feedback
		if (!isDataEmpty()) {
			feedback.append("\n");
			feedback.append(MESSAGE_DISPLAY_FLOATING_TASKS);
			feedback.append("\n");
		} else {
			feedback.append("\n");
			feedback.append(MESSAGE_NO_FLOATING_TASKS);
			feedback.append("\n");
		}
	   
	}
	
	//display deadline tasks
	private void displayDeadlineTasks(){
		int displayNo = 1;
		DisplayOutput.add(MESSAGE_SHOW_DEADLINE_TASKS);

	    for (int i = 1; i<= internalMem.size(); i++){
	    	Task task = internalMem.get(i - 1);
	    	if(task.getTaskType() == TYPE_DEADLINE_TASK){
	    		String endTime = D_M_Y_DateFormatter.format(task.getEndTime().getTime());
	    		DisplayOutput.add(String.format(MESSAGE_OUTPUT_DEADLINE_TASKS, displayNo, task.getTaskName(), endTime));
	    		displayMem.add(i - 1);
	    		displayNo++;
	    	}
	    }
	    
		feedback.append("\n");
		printOutDisplay();
		
		// Process feedback
		if (!isDataEmpty()) {
			feedback.append("\n");
			feedback.append(MESSAGE_DISPLAY_DEADLINE_TASKS);
			feedback.append("\n");
		} else {
			feedback.append("\n");
			feedback.append(MESSAGE_NO_DEADLINE_TASKS);
			feedback.append("\n");
		}
	   
	}
	
	//display timed tasks
	private void displayTimedTasks(){
		int displayNo = 1;
		DisplayOutput.add(MESSAGE_SHOW_TIMED_TASKS);

	    for (int i = 1; i<= internalMem.size(); i++){
	    	Task task = internalMem.get(i - 1);
	    	if(task.getTaskType() == TYPE_TIMED_TASK){
	    		
	    		String startTime = D_M_Y_DateFormatter.format(task.getStartTime().getTime());
	    		String endTime = D_M_Y_DateFormatter.format(task.getEndTime().getTime());
	    		DisplayOutput.add(String.format(MESSAGE_OUTPUT_TIMED_TASKS, displayNo, task.getTaskName(), startTime, endTime));
	    		displayMem.add(i - 1);
	    		displayNo++;
	    	}
	    }
	    
		feedback.append("\n");
		printOutDisplay();
		
		// Process feedback
		if (!isDataEmpty()) {
			feedback.append("\n");
			feedback.append(MESSAGE_DISPLAY_TIMED_TASKS);
			feedback.append("\n");
		} else {
			feedback.append("\n");
			feedback.append(MESSAGE_NO_TIMED_TASKS);
			feedback.append("\n");
		}
	   
	}

	
	private void displayInPeriod(){
			startOfPeriod = time.get(0);
			endOfPeriod = time.get(1);
			//Process output
			//DisplayOutput.add(D_M_Y_DateFormatter.format(startOfPeriod.getTime()) + TO + D_M_Y_DateFormatter.format(endOfPeriod.getTime()));
			
			goThroughTimePeriod(startOfPeriod, endOfPeriod);
			feedback.append("\n");
			printOutDisplay();
			
			// Process feedback
			if (!isDataEmpty()) {
				feedback.append("\n");
				feedback.append(MESSAGE_DISPLAY_IN_PERIOD);
				feedback.append("\n");
			} else {
				feedback.append("\n");
				feedback.append(MESSAGE_NO_TASKS_IN_PERIOD);
				feedback.append("\n");
			}
	
	}
	
	// Go through the internal memory and check whether the tasks are with in the time period
		private void goThroughTimePeriod(Calendar startOfPeriod, Calendar endOfPeriod) {
			int taskNo = 1;
			for (int i = 1; i <= internalMem.size(); i++) {
				Task task = internalMem.get(i - 1);
				Calendar taskStartTime = task.getStartTime();
				Calendar taskEndTime = task.getEndTime();
				if (isWithinTimePeriod(startOfPeriod, endOfPeriod, taskStartTime, taskEndTime)) {
					if (task.getTaskType() == TYPE_DEADLINE_TASK) {
						String taskEndTimeString = D_M_Y_DateFormatter.format(taskEndTime.getTime());
						DisplayOutput.add(String.format(MESSAGE_OUTPUT_DEADLINE_TASKS, taskNo, task.getTaskName(), taskEndTimeString));
					} else {
						String taskStartTimeString = D_M_Y_DateFormatter.format(taskStartTime.getTime());
			    		String taskEndTimeString = D_M_Y_DateFormatter.format(taskEndTime.getTime());
						DisplayOutput.add(String.format(MESSAGE_OUTPUT_TIMED_TASKS, taskNo, task.getTaskName(), taskStartTimeString, taskEndTimeString));
					}
					displayMem.add(i - 1);
					taskNo++;
				}	
			}
		}

		// check it is in the time period
		private boolean isWithinTimePeriod(Calendar startOfPeriod, Calendar endOfPeriod, Calendar taskStartTime, Calendar taskEndTime) {
			if (taskEndTime != null && endOfPeriod != null) {// check whether it is not a floating task, and it has an endOfPeriod
				if (taskStartTime == null && isTimeA_NotLater_Than_TimeB(taskEndTime, endOfPeriod)) {// check when it is a deadline task
					return (startOfPeriod == null || isTimeA_NotLater_Than_TimeB(startOfPeriod, taskEndTime));
				} else if (taskStartTime != null) { // check when it is a timed task
					if (startOfPeriod == null) { // check whether it is before deadline
						return isTimeA_NotLater_Than_TimeB(taskEndTime, endOfPeriod);
					} else {
						if (isTimeA_NotLater_Than_TimeB(taskStartTime, startOfPeriod)) {
							return (isTimeA_NotLater_Than_TimeB(startOfPeriod, taskEndTime) 
									&& isTimeA_NotLater_Than_TimeB(taskEndTime, endOfPeriod));
						} else {
							return isTimeA_NotLater_Than_TimeB(taskStartTime, endOfPeriod);
						}
					} 
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		
		private boolean isTimeA_NotLater_Than_TimeB(Calendar calA, Calendar calB){
			int i = calB.compareTo(calA);
			if (i > 0 || i == 0){
				return true;
			} else {
				return false;
			}
		}
		
		// Check whether the data which can be processed is empty
		protected boolean isDataEmpty() {
			return displayMem.isEmpty();
		}
		
		private void printOutDisplay(){
			for (int i = 0; i < DisplayOutput.size(); i++) {
				feedback.append(DisplayOutput.get(i));
				feedback.append("\n");
				//System.out.println(DisplayOutput.get(i));
			}
		}

}
