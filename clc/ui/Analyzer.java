package clc.ui;

import static clc.common.Constants.EMPTY;
import static clc.common.Constants.ERROR_EMPTY_COMMAND_DETAILS;
import clc.common.InvalidInputException;

public class Analyzer {

	protected static String commandType, commandDetails;
	
	protected Analyzer(String input) {}
	
	protected static void analyze(String input) {
		commandType = getFirstWord(input);
		commandDetails = removeFirstWord(input).trim();
	}

	protected static String getCommandType() {
		return commandType;
	}
	
	protected static String getCommandDetails() {
		return commandDetails;
	}
	
	protected static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	protected static String removeFirstWord(String userCommand) {
		return userCommand.replaceFirst(getFirstWord(userCommand), EMPTY).trim();
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
		return !commandDetails.equals(EMPTY);
	}

	protected static void throwExceptionIfEmptyCommandDetails()
			throws InvalidInputException {
		if (commandDetails.equals(EMPTY)) {
			throw new InvalidInputException(ERROR_EMPTY_COMMAND_DETAILS);
		}
	}
}
