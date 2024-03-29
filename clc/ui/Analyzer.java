//@author A0112089J

package clc.ui;

import static clc.common.Constants.EMPTY;
import static clc.common.Constants.SPACE;
import static clc.common.Constants.ERROR_EMPTY_COMMAND_DETAILS;
import static clc.common.Constants.MESSAGE_INVALID_FORMAT;

import java.util.regex.PatternSyntaxException;

import clc.common.InvalidInputException;
import clc.common.LogHelper;

public class Analyzer {

	protected static String commandType, commandDetails;
	
	protected Analyzer(String input) {}
	
	protected static void analyze(String input) throws InvalidInputException {
		commandType = getFirstWord(input);
		LogHelper.info("Command type: " + commandType);
		commandDetails = removeFirstWord(input);
		LogHelper.info("Command details: " + commandDetails);
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

	protected static String removeFirstWord(String input) throws InvalidInputException {
		String firstWord;
		try {
			firstWord = input.replaceFirst(getFirstWord(input), EMPTY).trim();
		} catch (PatternSyntaxException e) {
			throw new InvalidInputException(String.format(MESSAGE_INVALID_FORMAT, input));
		}
		return firstWord;
	}

	protected static boolean doesCommandDetailsExist(String commandDetails) {
		return !commandDetails.equals(EMPTY);
	}

	protected static void throwExceptionIfEmptyCommandDetails() throws InvalidInputException {
		if (!doesCommandDetailsExist(commandDetails)) {
			LogHelper.info("No command details");
			throw new InvalidInputException(ERROR_EMPTY_COMMAND_DETAILS);
		}
	}
	
	protected static boolean isNumeric(String currWord) {
		try {
			Integer.parseInt(currWord);
			LogHelper.info("'" + currWord + "'" + " is numeric");
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
