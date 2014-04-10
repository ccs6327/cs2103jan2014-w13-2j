//author A0112089J

package clc.ui;

import static clc.common.Constants.EMPTY;
import static clc.common.Constants.SPACE;
import static clc.common.Constants.ERROR_EMPTY_COMMAND_DETAILS;
import clc.common.InvalidInputException;

public class Analyzer {

	protected static String commandType, commandDetails;
	
	protected Analyzer(String input) {}
	
	protected static void analyze(String input) {
		commandType = getFirstWord(input);
		commandDetails = removeFirstWord(input);
	}

	protected static String getCommandType() {
		return commandType;
	}
	
	protected static String getCommandDetails() {
		return commandDetails;
	}
	
	protected static String getFirstWord(String input) {
		String commandType = input.trim().split(SPACE)[0];
		return commandType;
	}

	protected static String removeFirstWord(String input) {
		return input.replaceFirst(getFirstWord(input), EMPTY).trim();
	}

	protected static boolean doesCommandDetailsExist(String commandDetails) {
		return !commandDetails.equals(EMPTY);
	}

	protected static void throwExceptionIfEmptyCommandDetails() throws InvalidInputException {
		if (commandDetails.equals(EMPTY)) {
			throw new InvalidInputException(ERROR_EMPTY_COMMAND_DETAILS);
		}
	}
	
	protected static boolean isNumeric(String currWord) {
		try {
			Integer.parseInt(currWord);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
