package clc.logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.zip.DataFormatException;


import static clc.common.Constants.*;

public class Display implements Command {
    private StringBuilder feedback = new StringBuilder();
    private String commandDetails = null;
    private ArrayList<GregorianCalendar> time;
    private Calendar startOfPeriod = null, endOfPeriod = null;
    
	public Display(ArrayList<GregorianCalendar> time) {
		this.time = time;
	}
	public Display(String commandDetails) {
		this. commandDetails = commandDetails;
	} 
	 
	@Override
	public String execute() {
		
		DisplayOutput.clear();
		displayMem.clear();
		
		if(internalMem.isEmpty()) {
			feedback.append("\n");
			feedback = feedback.append(MESSAGE_EMPTY_LIST);
			feedback.append("\n");
		} else if (commandDetails!= null) {// for now i assume all commandDetails are all
			displayAllTasks();
		} else {
			displayInPeriod();
		}		
		assert feedback != null;
		return feedback.toString();
	}
	
	private void displayAllTasks(){
		DisplayOutput.add(MESSAGE_SHOW_ALL_TASKS);

	    for (int i = 1; i<= internalMem.size(); i++){
	    	
	    	Task task = internalMem.get(i-1);
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
	    	displayMem.add(task);
	    }
	    
	    feedback.append("\n");
	    printOutDisplay();
	    feedback.append("\n");
	    feedback.append(MESSAGE_DISPLAY);
	    feedback.append("\n");
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
				feedback.append(MESSAEG_DISPLAY_IN_PERIOD);
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
				Task task = internalMem.get(i-1);
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
					displayMem.add(task);
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
		
		protected boolean isTimeA_NotLater_Than_TimeB(Calendar calA, Calendar calB){
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
