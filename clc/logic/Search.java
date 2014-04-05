package clc.logic;

import static clc.common.Constants.*;

import java.util.ArrayList;
import java.util.Collections;

import clc.storage.Storage;


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
		feedback.append("\n");
		feedback.append(getFeedbackToUser());
		feedback.append("\n");
		return feedback.toString();
	}
   
	// Search the internal memory for the keyword and process the output
	private void searchAndprocessOutput() {
		DisplayOutput.clear();
		displayMem.clear();
		DisplayOutput.add(String.format(MESSAGE_MATCHING_ENTRIES, commandDetails));
		DisplayOutput.add(ANOTHER_LINE);
		
		//logger.log(Level.INFO, "Starting to sort events");
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
	    printOutDisplay();
	}

	// Get the feedback according to the result of search
	private String getFeedbackToUser() {
		if (!displayMem.isEmpty()) {
			if (commandDetails.equals(BLANK_STRING)) {
				return MESSAGE_RESULTS_FOUND + MESSAGE_DISPLAY;
			} else {
				return MESSAGE_RESULTS_FOUND + String.format(MESSAGE_SEARCH_FEEDBACK, commandDetails);
			}
		} else {
			return String.format(ERROR_TASK_NOT_FOUND, commandDetails);
		}
	}
	
	
	private void printOutDisplay(){
		for (int i = 0; i < DisplayOutput.size(); i++) {
			feedback.append(DisplayOutput.get(i));
			feedback.append("\n");
			//System.out.println(DisplayOutput.get(i));
		}
	}
	protected void sortTasks() {
		Collections.sort(internalMem, new  TaskComparator());
	}
}
