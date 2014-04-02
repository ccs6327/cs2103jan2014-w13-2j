package clc.ui;

import static clc.common.Constants.SPACE;
import static clc.common.Constants.COMMA;
import static clc.common.Constants.ERROR_NO_COMMA;
import static clc.common.Constants.ERROR_NO_SEQUENCE_NUMBER;
import static clc.common.Constants.ERROR_NO_NEW_TASK_NAME;
import static clc.common.Constants.ERROR_EMPTY_COMMAND_DETAILS;

import java.util.GregorianCalendar;

import clc.common.InvalidInputException;

public class UpdateAnalyzer extends TimeParser {
	private static boolean isCaseUpdateCalendar;
	private static String[] tempInfo;
	private static int calendarProvided = 0;
	private static int seqNo;
	private static GregorianCalendar tempStartCalendar;

	protected UpdateAnalyzer(String input) {
		super(input);
	}

	protected static void analyze() throws InvalidInputException {
		
		determineIfCommandDetailsEmpty();
		
		calendarProvided = 0;
		tempInfo = commandDetails.split(SPACE);
		infoToBeProcessed = tempInfo;

		determineIfSeqNoProvided();
		processCalendarInfo();

		if (doesContainTimeInfo()) { //case update calendar
			if (!commandDetails.contains(COMMA)) {
				throw new InvalidInputException(ERROR_NO_COMMA);
			}

			int indexOfComma = findIndexOfComma();
			analyzeUpdateStartTime(indexOfComma);
			determineIfStartDateIsProvided();
			determineIfStartTimeIsProvided();

			analyzeUpdateEndTime(indexOfComma);
			determineIfEndDateIsProvided();
			determineIfEndTimeIsProvided();

			startCalendar = tempStartCalendar;

			isCaseUpdateCalendar = true;
		} else { //case update task name
			isCaseUpdateCalendar = false;
		}
	}

	private static void determineIfCommandDetailsEmpty()
			throws InvalidInputException {
		if (commandDetails.equals("")) {
			throw new InvalidInputException(ERROR_EMPTY_COMMAND_DETAILS);
		}
	}

	private static void determineIfSeqNoProvided() throws InvalidInputException {
		if (!isNumeric(getFirstWord(commandDetails))) {
			throw new InvalidInputException(ERROR_NO_SEQUENCE_NUMBER);
		} else {
			seqNo = Integer.parseInt(getFirstWord(commandDetails));
		}
	}

	private static void determineIfStartDateIsProvided() {
		if (isEndDateSet) {
			calendarProvided += 8;
		}
	}

	private static void determineIfStartTimeIsProvided() {
		if (isEndTimeSet) {
			calendarProvided += 4;
		}
	}

	private static void determineIfEndDateIsProvided() {
		if (isEndDateSet) {
			calendarProvided += 2;
		}
	}

	private static void determineIfEndTimeIsProvided() {
		if (isEndTimeSet) {
			calendarProvided += 1;
		}
	}

	private static void analyzeUpdateStartTime(int indexOfComma) throws InvalidInputException {
		int index = 0;
		if (indexOfComma > 0) {
			infoToBeProcessed = new String[indexOfComma - 1];
			for (int i = 1; i < indexOfComma; i ++) { //first one is sequence no
				infoToBeProcessed[index ++] = tempInfo[i];
			}
			processCalendarInfo();

			// as processCalendarInfo will set the time to endTime
			// so we have to swap the value
			tempStartCalendar = endCalendar;
			endCalendar = null;
		}
	}

	private static void analyzeUpdateEndTime(int indexOfComma) throws InvalidInputException {
		int index = 0;
		initializeVariable(); //to avoid wrong calculation of calendarProvided
		if (tempInfo.length != indexOfComma + 1) {
			infoToBeProcessed = new String[tempInfo.length - indexOfComma - 1];
			for (int i = indexOfComma + 1; i < tempInfo.length ; i ++) { //first one is sequence no
				infoToBeProcessed[index ++] = tempInfo[i];
			}
			processCalendarInfo();
		}
	}

	private static int findIndexOfComma() {
		for(int i = 0; i < tempInfo.length; i ++) {
			if(tempInfo[i].equals(COMMA)) {
				return i;
			}
		}
		return -1;
	}

	public static boolean getUpdateCase() {
		return isCaseUpdateCalendar;
	}

	protected static int getSeqNumForUpdate() throws InvalidInputException {
		return seqNo;
	}

	protected static String getNewTaskName() throws InvalidInputException {
		String newTaskName = removeFirstWord(commandDetails);
		if (isNewTaskNameProvided(newTaskName)) {
			throw new InvalidInputException(ERROR_NO_NEW_TASK_NAME);
		}
		return newTaskName;
	}

	private static boolean isNewTaskNameProvided(String newTaskName) {
		return newTaskName.equals("");
	}

	public static int getCalendarProvidedCase() {
		return calendarProvided;
	}
}
