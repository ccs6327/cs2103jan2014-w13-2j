package clc.logic;

/**
 * This class is used to search events by the keyword
 **/

//@author A0105749Y

import java.util.ArrayList;
import java.util.Collections;

import clc.storage.Storage;
import clc.common.LogHelper;
import static clc.common.Constants.MESSAGE_MATCHING_ENTRIES;
import static clc.common.Constants.MESSAGE_OUTPUT_TASKS;
import static clc.common.Constants.MESSAGE_RESULTS_FOUND;
import static clc.common.Constants.MESSAGE_DISPLAY;
import static clc.common.Constants.MESSAGE_SEARCH_FEEDBACK_PLURAL;
import static clc.common.Constants.MESSAGE_SEARCH_FEEDBACK_SINGULAR;
import static clc.common.Constants.ERROR_TASK_NOT_FOUND;
import static clc.common.Constants.EMPTY_STRING;
import static clc.common.Constants.NEW_LINE;
import static clc.common.Constants.DisplayOutput;
import static clc.common.Constants.LOG_SEARCH_STARTED;


public class Search implements Command {
	
	String commandDetails;
    private StringBuilder feedback = new StringBuilder();
	private ArrayList<Task> internalMem;
	private ArrayList<Integer> displayMem;
	
	public Search(String commandDetails){
		this.commandDetails = commandDetails;
		internalMem = Storage.getInternalMem();
		displayMem = Storage.getDisplayMem();
	}
	public String execute() {
		searchAndprocessOutput();
		appendTaskSearchMessage(feedback, getFeedbackToUser());
		assert feedback != null;
		return feedback.toString();
	}
   
	// Search the internal memory for the keyword and process the output
	private void searchAndprocessOutput() {
		
	    //before searching DisplayOutput and displayMem must be cleared
		DisplayOutput.clear();
		displayMem.clear();
		DisplayOutput.add(String.format(MESSAGE_MATCHING_ENTRIES, commandDetails));
		DisplayOutput.add(EMPTY_STRING);
		
		LogHelper.info(LOG_SEARCH_STARTED);
		if (!internalMem.isEmpty()) {
			sortTasks();
			int displayNo = 1;
			for (int i = 0; i < internalMem.size(); i++) {
				Task task = internalMem.get(i);
				if (task.getTaskName().toLowerCase().contains(commandDetails.toLowerCase())) {
			    	DisplayOutput.add(String.format(MESSAGE_OUTPUT_TASKS, displayNo, task.toString()));	
					displayMem.add(i);
					displayNo++;
				}	
			}
		}
		appendDisplayOutputMessage();
	}

	// Get the feedback according to the result of search
	private String getFeedbackToUser() {
		if (getDataAmount() > 0) {
			if (commandDetails.equals(EMPTY_STRING)) {
				return MESSAGE_RESULTS_FOUND + MESSAGE_DISPLAY;
			} else if (getDataAmount() > 1) {
				return MESSAGE_RESULTS_FOUND + String.format(MESSAGE_SEARCH_FEEDBACK_PLURAL, commandDetails);
			} else {
				return MESSAGE_RESULTS_FOUND + String.format(MESSAGE_SEARCH_FEEDBACK_SINGULAR, commandDetails);
			}
		} else {
			return String.format(ERROR_TASK_NOT_FOUND, commandDetails);
		}
	}
	
	private int getDataAmount() {
		return displayMem.size();
	}
	
	private void sortTasks() {
		Collections.sort(internalMem, new  TaskComparator());
	}
	
	private void appendTaskSearchMessage(StringBuilder feedback, String feedbackMessage) {
		feedback.append(NEW_LINE);
		feedback.append(feedbackMessage);
		feedback.append(NEW_LINE);
	}
	
	private void appendDisplayOutputMessage(){
		for (int i = 0; i < DisplayOutput.size(); i++) {
			feedback.append(DisplayOutput.get(i));
			feedback.append(NEW_LINE);
		}
	}
}
