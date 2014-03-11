package clc.logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.zip.DataFormatException;


import static clc.common.Constants.*;

public class Display implements Command {
    private String feedback;
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
			feedback = MESSAGE_EMPTY_LIST;
		} else if (commandDetails!= null) {// for now i assume all commandDetails are all
			displayAllTasks();
		} else {
			displayInPeriod();
		}		
		assert feedback != null;
		return feedback;
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
	 
	    printOutDisplay();
	    feedback = MESSAGE_DISPLAY;
	}
	
	private void displayInPeriod(){
			startOfPeriod = time.get(0);
			endOfPeriod = time.get(1);
			//Process output
			DisplayOutput.add(D_M_Y_DateFormatter.format(startOfPeriod.getTime()) + TO + D_M_Y_DateFormatter.format(endOfPeriod.getTime()));
			
			goThroughTimePeriod(startOfPeriod, endOfPeriod);
			printOutDisplay();
			
			// Process feedback
			if (!isDataEmpty()) {
				feedback = MESSAEG_DISPLAY_IN_PERIOD;
			} else {
				feedback = MESSAGE_NO_TASKS_IN_PERIOD;
			}
	
	}
	
	// Go through the internal memory and check whether the tasks are with in the time period
		private void goThroughTimePeriod(Calendar startOfPeriod, Calendar endOfPeriod) {
			int taskNo = 1;
			for (int i = 1; i <= internalMem.size(); i++) {
				Calendar taskStartTime = internalMem.get(i-1).getStartTime();
				Calendar taskEndTime = internalMem.get(i-1).getEndTime();
				if (isWithinTimePeriod(startOfPeriod, endOfPeriod, taskStartTime, taskEndTime)) {
					DisplayOutput.add(String.format(MESSAGE_OUTPUT_FLOATING_TASKS, taskNo, internalMem.get(i-1).getTaskName()));
					displayMem.add(internalMem.get(i-1));
					taskNo++;
				}	
			}
		}

		/**
		 * 
		 * Firstly, it checks through the start time
		 * Secondly, it checks through the end time
		 * Lastly, it checks if the date 
		 * If either one of the condition is true, the date is within the taskStartDate and taskEndDate
		 */
		private boolean isWithinTimePeriod(Calendar startOfPeriod, Calendar endOfPeriod, Calendar taskStartTime, Calendar taskEndTime) {
			return true;
		}
		
		// Check whether the data which can be processed is empty
		protected boolean isDataEmpty() {
			return displayMem.isEmpty();
		}
		
		private void printOutDisplay(){
			for (int i = 0; i < DisplayOutput.size(); i++) {
				System.out.println(DisplayOutput.get(i));
			}
		}

}
