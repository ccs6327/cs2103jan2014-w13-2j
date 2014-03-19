package clc.ui;

import static clc.common.Constants.COMMA;
import static clc.common.Constants.SPACE;

import java.util.ArrayList;

import clc.common.InvalidInputException;

public class UpdateAnalyzer extends TimeAnalyzer {
	private static boolean isCaseUpdateCalendar;
	private static String[] tempInfo;
	private static int calendarProvided = 0;

	protected UpdateAnalyzer(String input) {
		super(input);
	}

	protected static void analyze() throws InvalidInputException {
		calendarProvided = 0;
		tempInfo = commandDetails.split(SPACE);
		infoToBeProcessed = tempInfo;
		//** throw exception if first is not a digit

		if (doesContainTimeInfo()) { //case update calendar
			if (!commandDetails.contains(COMMA)) {
				throw new InvalidInputException();
			}
			int indexOfComma = findIndexOfComma();
			analyzeUpdateStartTime(indexOfComma);
			determineIfStartDateIsProvided();
			determineIfStartTimeIsProvided();
			clearInfoMemory();
			analyzeUpdateEndTime(indexOfComma);
			determineIfEndDateIsProvided();
			determineIfEndTimeIsProvided();
			isCaseUpdateCalendar = true;
		} else { //case update task name
			isCaseUpdateCalendar = false;
		}                                                    
	}

	private static void clearInfoMemory() {
		timeInfo = new ArrayList<Integer>();
		dateInfo = new ArrayList<Integer>();
		monthInfo = new ArrayList<Integer>();
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

	private static void analyzeUpdateStartTime(int indexOfComma) {
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

	private static void analyzeUpdateEndTime(int indexOfComma) {
		int index = 0;
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
		String currWord = getFirstWord(commandDetails);
		if (isNumeric(currWord)) {
			return Integer.parseInt(currWord);
		} else {
			throw new InvalidInputException();
		}
	}

	protected static String getNewTaskName() throws InvalidInputException {
		String newTaskName = removeFirstWord(commandDetails);
		if (newTaskName.equals("")) {
			throw new InvalidInputException();
		}
		return newTaskName;
	}

	public static int getCalendarProvidedCase() {
		return calendarProvided;
	}
}
