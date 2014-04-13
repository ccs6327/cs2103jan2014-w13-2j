package clc.logic;

//@author A0105749Y
/**
 * This class is used to filter tasks by date, week, month or year
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Collections;

import clc.common.InvalidInputException;
import clc.common.LogHelper;
import clc.storage.Storage;
import static clc.common.Constants.*;


public class Display implements Command {
    private StringBuilder feedback = new StringBuilder();
    private String query;
    private String commandDetails = null;
    private Calendar startOfPeriod = null, endOfPeriod = null;
    private ArrayList<GregorianCalendar> time;
	private ArrayList<Task> internalMem;
	private ArrayList<Integer> displayMem;
	private ArrayList<Integer> internalMemCompleteTasks = new ArrayList<Integer>();
	private ArrayList<Integer> internalMemIncompleteTasks = new ArrayList<Integer>();
	
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
	 
	public Display(ArrayList<GregorianCalendar> time, String query) {
		this.time = time;
		this.query = query;
		internalMem = Storage.getInternalMem();
		displayMem = Storage.getDisplayMem();
	}
	
	@Override
	public String execute() {
		
		DisplayOutput.clear();
		displayMem.clear();
		sortTasks();
		
		LogHelper.info(LOG_DISPLAY_STARTED);
		try {
			if(internalMem.isEmpty()) {
				appendTaskDisplayMessage(feedback, MESSAGE_EMPTY_LIST);
				
			} else if (commandDetails!= null) {
				
				switch (commandDetails){
				case TYPE_DISPLAY_INCOMPLETE:
					displayIncompleteTasks();
					break;
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
					LogHelper.warning("user inputs invild command details for display function");
					throw new InvalidInputException(String.format(MESSAGE_INVALID_FORMAT, commandDetails));
				    //feedback.append(String.format(MESSAGE_INVALID_FORMAT, commandDetails)); 
				}
				
			} else {
				displayInPeriod();
			} 
		} catch (InvalidInputException iie) {
				return iie.getMessage();
		}
		
		assert feedback != null;
		return feedback.toString();
	}
	
	//display all tasks
	private void displayAllTasks(){
		appendTaskDisplayMessage(feedback, MESSAGE_SHOW_ALL_TASKS);
		
	    for (int i = 1; i<= internalMem.size(); i++){
	    	Task task = internalMem.get(i - 1);
	    	DisplayOutput.add(String.format(MESSAGE_OUTPUT_TASKS, i, task.toString()));	
	    	displayMem.add(i - 1);
	    }
	    
	    appendDisplayOutputMessage();
		appendTaskDisplayMessage(feedback, MESSAGE_DISPLAY);
	}
	
	//display incomplete tasks
	private void displayIncompleteTasks(){
		int displayNo = 1;
		appendTaskDisplayMessage(feedback, MESSAGE_SHOW_INCOMPLETE_TASKS);
		
		seprateTasksByCompletion();
		
	    for (int i = 1; i<= internalMemIncompleteTasks.size(); i++){
	    	int taskNo = internalMemIncompleteTasks.get(i - 1);
	    	Task task = internalMem.get(taskNo);
		    DisplayOutput.add(String.format(MESSAGE_OUTPUT_TASKS, displayNo, task.toString()));	
		    displayMem.add(taskNo);
		    displayNo++;
	    }
	    appendDisplayOutputMessage();
	    
		// Process feedback
		if (getDataAmount() > 1) {
			appendTaskDisplayMessage(feedback, MESSAGE_DISPLAY_INCOMPLETE_TASKS_PLURAL);
		} else if (getDataAmount() == 1) {
			appendTaskDisplayMessage(feedback, MESSAGE_DISPLAY_INCOMPLETE_TASKS_SINGULAR);
		} else {
			appendTaskDisplayMessage(feedback, MESSAGE_NO_INCOMPLETE_TASKS);
		}  
	}
	
	//display floating tasks
	private void displayFloatingTasks(){
		int displayNo = 1;
		appendTaskDisplayMessage(feedback, MESSAGE_SHOW_FLOATING_TASKS);
		
	    for (int i = 1; i<= internalMem.size(); i++){
	    	Task task = internalMem.get(i - 1);
	    	if(task.getTaskType() == TYPE_FLOATING_TASK){
		    	DisplayOutput.add(String.format(MESSAGE_OUTPUT_TASKS, displayNo, task.toString()));	
	    		displayMem.add(i - 1);
	    		displayNo++;
	    	}
	    }
		appendDisplayOutputMessage();
		
		// Process feedback
		if (getDataAmount() > 1) {
			appendTaskDisplayMessage(feedback, MESSAGE_DISPLAY_FLOATING_TASKS_PLURAL);
		} else if (getDataAmount() == 1) {
			appendTaskDisplayMessage(feedback, MESSAGE_DISPLAY_FLOATING_TASKS_SINGULAR);
		} else {
			appendTaskDisplayMessage(feedback, MESSAGE_NO_FLOATING_TASKS);
		}
	   
	}
	
	//display deadline tasks
	private void displayDeadlineTasks(){
		int displayNo = 1;
		appendTaskDisplayMessage(feedback,MESSAGE_SHOW_DEADLINE_TASKS);
		
	    for (int i = 1; i<= internalMem.size(); i++){
	    	Task task = internalMem.get(i - 1);
	    	if(task.getTaskType() == TYPE_DEADLINE_TASK){
		    	DisplayOutput.add(String.format(MESSAGE_OUTPUT_TASKS, displayNo, task.toString()));	
	    		displayMem.add(i - 1);
	    		displayNo++;
	    	}
	    }
		appendDisplayOutputMessage();
		
		// Process feedback
		if (getDataAmount() > 1) {
			appendTaskDisplayMessage(feedback, MESSAGE_DISPLAY_DEADLINE_TASKS_PLURAL);
		} else if (getDataAmount() == 1) {
			appendTaskDisplayMessage(feedback, MESSAGE_DISPLAY_DEADLINE_TASKS_SINGULAR);
		} else {
			appendTaskDisplayMessage(feedback, MESSAGE_NO_DEADLINE_TASKS);
		}
	}
	
	//display timed tasks
	private void displayTimedTasks(){
		int displayNo = 1;
		appendTaskDisplayMessage(feedback, MESSAGE_SHOW_TIMED_TASKS);

	    for (int i = 1; i<= internalMem.size(); i++){
	    	Task task = internalMem.get(i - 1);
	    	if(task.getTaskType() == TYPE_TIMED_TASK){
		    	DisplayOutput.add(String.format(MESSAGE_OUTPUT_TASKS, displayNo, task.toString()));	
	    		displayMem.add(i - 1);
	    		displayNo++;
	    	}
	    }
		appendDisplayOutputMessage();
		
		// Process feedback
		if (getDataAmount() > 1) {
			appendTaskDisplayMessage(feedback, MESSAGE_DISPLAY_TIMED_TASKS_PLURAL);
		} else if (getDataAmount() == 1) {
			appendTaskDisplayMessage(feedback, MESSAGE_DISPLAY_TIMED_TASKS_SINGULAR);
		} else {
			appendTaskDisplayMessage(feedback, MESSAGE_NO_TIMED_TASKS);
		}
	}

	
	private void displayInPeriod(){
			startOfPeriod = time.get(0);
			endOfPeriod = time.get(1);
			
			//Process display start message
			if(query != null){
				appendTaskDisplayMessage(feedback, String.format(MESSAGE_DISPLAY_QUERY, query));
			} else if (startOfPeriod == null){
				
				String endTime = D_M_Y_DateFormatter.format(endOfPeriod.getTime());
				appendTaskDisplayMessage(feedback,String.format(MESSAGE_DISPLAY_TASKS_BY_DEADLINE, endTime));
			} else {
				
				String startTime = D_M_Y_DateFormatter.format(startOfPeriod.getTime());
				String endTime = D_M_Y_DateFormatter.format(endOfPeriod.getTime());
				appendTaskDisplayMessage(feedback,String.format(MESSAGE_DISPLAY_TASKS_IN_PERIOD, startTime, endTime));
			}
			
			goThroughTimePeriod(startOfPeriod, endOfPeriod);
			appendDisplayOutputMessage();
			
			// Process feedback
			if (getDataAmount() > 1) {
				appendTaskDisplayMessage(feedback, MESSAGE_DISPLAY_IN_PERIOD_PLURAL);
			} else if (getDataAmount() == 1) {
				appendTaskDisplayMessage(feedback, MESSAGE_DISPLAY_IN_PERIOD_SINGULAR);
			} else {
				appendTaskDisplayMessage(feedback, MESSAGE_NO_TASKS_IN_PERIOD);
			}
	
	}
	
	// Go through the internal memory and check whether the tasks are with in the time period
	private void goThroughTimePeriod(Calendar startOfPeriod, Calendar endOfPeriod) {
		seprateTasksByCompletion();
		if (startOfPeriod!= null && isWithinADay(startOfPeriod , endOfPeriod)){
			int displayNo = 1;
			addDisplayMessage(DisplayOutput, MESSAGE_INCOMPLETE_TASKS);
			
			for (int i = 1; i <= internalMemIncompleteTasks.size(); i++) {
				int taskNo =  internalMemIncompleteTasks.get(i - 1);
				Task task = internalMem.get(taskNo);
				Calendar taskStartTime = task.getStartTime();
				Calendar taskEndTime = task.getEndTime();
				if (isWithinTimePeriod(startOfPeriod, endOfPeriod, taskStartTime, taskEndTime)) {
					DisplayOutput.add(String.format(MESSAGE_OUTPUT_TASKS, displayNo, task.toStringTimeFormatter()));	
					displayMem.add(i - 1);
				    displayNo++;
				}
			}	
			addDisplayOutputMessage(DisplayOutput, MESSAGE_COMPLETE_TASKS);
			
			for (int i = 1; i <= internalMemCompleteTasks.size(); i++) {
				int taskNo =  internalMemCompleteTasks.get(i - 1);
				Task task = internalMem.get(taskNo);
				Calendar taskStartTime = task.getStartTime();
				Calendar taskEndTime = task.getEndTime();
				if (isWithinTimePeriod(startOfPeriod, endOfPeriod, taskStartTime, taskEndTime)) {
					DisplayOutput.add(String.format(MESSAGE_OUTPUT_TASKS, displayNo, task.toStringTimeFormatter()));	
					displayMem.add(i - 1);
				    displayNo++;
				}
			}	
		} else {
			int displayNo = 1;
			addDisplayMessage(DisplayOutput, MESSAGE_INCOMPLETE_TASKS);
			
			for (int i = 1; i <= internalMemIncompleteTasks.size(); i++) {
				int taskNo =  internalMemIncompleteTasks.get(i - 1);
				Task task = internalMem.get(taskNo);
				Calendar taskStartTime = task.getStartTime();
				Calendar taskEndTime = task.getEndTime();
				if (isWithinTimePeriod(startOfPeriod, endOfPeriod, taskStartTime, taskEndTime)) {
					DisplayOutput.add(String.format(MESSAGE_OUTPUT_TASKS, displayNo, task.toString()));		
					displayMem.add(i - 1);
				    displayNo++;
				}
			}	
			addDisplayOutputMessage(DisplayOutput, MESSAGE_COMPLETE_TASKS);
			
			for (int i = 1; i <= internalMemCompleteTasks.size(); i++) {
				int taskNo =  internalMemCompleteTasks.get(i - 1);
				Task task = internalMem.get(taskNo);
				Calendar taskStartTime = task.getStartTime();
				Calendar taskEndTime = task.getEndTime();
				if (isWithinTimePeriod(startOfPeriod, endOfPeriod, taskStartTime, taskEndTime)) {
					DisplayOutput.add(String.format(MESSAGE_OUTPUT_TASKS, displayNo, task.toString()));	
					displayMem.add(i - 1);
				    displayNo++;
				}
			}	
			
		}				
	}
	// check it is in the time period
	private boolean isWithinTimePeriod(Calendar startOfPeriod, Calendar endOfPeriod, Calendar taskStartTime, Calendar taskEndTime) {
		if (taskEndTime != null && endOfPeriod != null) {// check whether it is not a floating task, and it has an endOfPeriod
			if (taskStartTime == null && isTimeBLaterOrEqualThanTimeA(taskEndTime, endOfPeriod)) {// check when it is a deadline task
				return (startOfPeriod == null || isTimeBLaterOrEqualThanTimeA(startOfPeriod, taskEndTime));
			} else if (taskStartTime != null) { // check when it is a timed task
				if (startOfPeriod == null) { // check whether it is before deadline
					return isTimeBLaterOrEqualThanTimeA(taskEndTime, endOfPeriod);
				} else {
					if (isTimeBLaterOrEqualThanTimeA(taskStartTime, startOfPeriod)) {
						return (isTimeBLaterOrEqualThanTimeA(startOfPeriod, taskEndTime) 
								&& isTimeBLaterOrEqualThanTimeA(taskEndTime, endOfPeriod));
					} else {
						return isTimeBLaterOrEqualThanTimeA(taskStartTime, endOfPeriod);
					}
				} 
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private boolean isTimeBLaterOrEqualThanTimeA(Calendar calA, Calendar calB){
		int i = calB.compareTo(calA);
		if (i > 0 || i == 0){
			return true;
		} else {
			return false;
		}
	}
	
	private void seprateTasksByCompletion() {
		internalMemCompleteTasks.clear();
		internalMemIncompleteTasks.clear();
		for(int i=1; i<=internalMem.size(); i++){
		    	Task task = internalMem.get(i - 1);
		    	if (!task.getIsDone()){
		    		internalMemIncompleteTasks.add(i-1);
		    	} else {
		    		internalMemCompleteTasks.add(i-1);
		    	}
		}
	}
	
	// Check whether the data which can be processed is empty
	private int getDataAmount() {
		return displayMem.size();
	}
	
	private boolean isWithinADay(Calendar calA, Calendar calB){
		   return calA.get(Calendar.YEAR) == calB.get(Calendar.YEAR)
		            && calA.get(Calendar.MONTH) == calB.get(Calendar.MONTH)
		            && (calA.get(Calendar.DAY_OF_MONTH) == calB.get(Calendar.DAY_OF_MONTH)
		            || (calA.get(Calendar.DAY_OF_MONTH) + 1 == calB.get(Calendar.DAY_OF_MONTH) 
		            && calB.get(Calendar.HOUR) == 0 && calB.get(Calendar.MINUTE) == 0 && calB.get(Calendar.SECOND) == 0));
	}
	
	private void sortTasks() {
		Collections.sort(internalMem, new  TaskComparator());
	}
	
	//append feedback method
	private void appendDisplayOutputMessage(){
	    feedback.append(NEW_LINE);
		for (int i = 0; i < DisplayOutput.size(); i++) {
			feedback.append(DisplayOutput.get(i));
			feedback.append(NEW_LINE);
		}
	}
	
	private void appendTaskDisplayMessage(StringBuilder feedback, String feedbackMessage){
		feedback.append(NEW_LINE);
		feedback = feedback.append(feedbackMessage);
		feedback.append(NEW_LINE);
	}

	private void addDisplayMessage(ArrayList<String> DisplayOutput, String message){
		DisplayOutput.add(message);
		DisplayOutput.add(EMPTY_STRING);
	}
	
	private void addDisplayOutputMessage(ArrayList<String> DisplayOutput, String message){
		DisplayOutput.add(EMPTY_STRING);
		DisplayOutput.add(message);
		DisplayOutput.add(EMPTY_STRING);
	}	
}
