package clc.ui;

import clc.logic.Task;

public class Analyzer {

	protected static String commandType, commandDetails;
	private Task task;
	static String[] infoToBeProcessed;

	protected Analyzer(String input) {}
	
	protected static void analyze(String input) {
		commandType = getFirstWord(input);
		commandDetails = removeFirstWord(input);
	}

	protected static String getCommandType() {
		return commandType;
	}

	protected Task getTask() {
		return task;
	}

	protected static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	protected static String removeFirstWord(String userCommand) {
		return userCommand.replaceFirst(getFirstWord(userCommand), "").trim();
	}
	
	protected static String getNewTaskName() {
		return removeFirstWord(commandDetails);
	}
	
	protected static boolean isNumeric(String str) {  
		try {  
			Integer.parseInt(str);  
		} catch(NumberFormatException nfe) {  
			return false;  
		}  
		return true;  
	}

	protected static boolean doesCommandDetailsExist(String commandDetails) {
		return !commandDetails.equals("");
	}
}
