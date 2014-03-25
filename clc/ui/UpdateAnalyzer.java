package clc.ui;

import static clc.common.Constants.SPACE;
import static clc.common.Constants.COMMA;
import static clc.common.Constants.ERROR_NO_COMMA;
import static clc.common.Constants.ERROR_NO_SEQUENCE_NUMBER;
import static clc.common.Constants.ERROR_NO_NEW_TASK_NAME;
import static clc.common.Constants.ERROR_EMPTY_COMMAND_DETAILS;

import java.util.GregorianCalendar;

import clc.common.InvalidInputException;

public class UpdateAnalyzer extends TimeAnalyzer {
	private static boolean isCaseUpdateCalendar;
	private static String[] tempInfo;
	private static int calendarProvided = 0;
	private static int seqNo;
	private static GregorianCalendar startTime;

	protected UpdateAnalyzer(String input) {
		super(input);
	}

	protected static void analyze() throws InvalidInputException {
		
		determineIfCommandDetailsEmpty();
		
		calendarProvided = 0;
		tempInfo = commandDetails.split(SPACE);
		infoToBeProcessed = tempInfo;

		determineIfSeqNoProvided();

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

			determineIfStartTimeLaterThanEndTime();

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
		if (dateInfo.size() == 1) {
			calendarProvided += 8;
		}
	}

	private static void determineIfStartTimeIsProvided() {
		if (timeInfo.size() == 1) {
			calendarProvided += 4;
		}
	}

	private static void determineIfEndTimeIsProvided() {
		if (dateInfo.size() == 1) {
			calendarProvided += 2;
		}
	}

	private static void determineIfEndDateIsProvided() {
		if (timeInfo.size() == 1) {
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
			recordAndProcessCalendarInfoProvided();

			// as processCalendarInfo will set the time to endTime
			// so we have to swap the value
			startTime = endTime;
			endTime = null;
		}
	}

	private static void analyzeUpdateEndTime(int indexOfComma) throws InvalidInputException {
		int index = 0;
		instantiateVariable(); //to avoid wrong calculation of calendarProvided
		if (tempInfo.length != indexOfComma + 1) {
			infoToBeProcessed = new String[tempInfo.length - indexOfComma - 1];
			for (int i = indexOfComma + 1; i < tempInfo.length ; i ++) { //first one is sequence no
				infoToBeProcessed[index ++] = tempInfo[i];
			}
			recordAndProcessCalendarInfoProvided();
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

	protected static void recordAndProcessCalendarInfoProvided() throws InvalidInputException {
		instantiateVariable();
		recordCalendarInfo();
		if (timeInfo.size() > 0 || dateInfo.size() > 0) { //have Calendar Info to be processed
			processCalendarInfo();
		}
	}
}
