//@author A0112089J

package clc.ui;

import static clc.common.Constants.ERROR_CANNOT_UPDATE_TO_RECURRING_TASK;
import static clc.common.Constants.SPACE;
import static clc.common.Constants.COMMA;
import static clc.common.Constants.CHAR_SPACE;
import static clc.common.Constants.CHAR_COMMA;
import static clc.common.Constants.EMPTY;
import static clc.common.Constants.QUOTATION_MARK;
import static clc.common.Constants.ERROR_NO_SEQUENCE_NUMBER;
import static clc.common.Constants.ERROR_NO_NEW_TASK_NAME;
import static clc.common.Constants.CALENDAR_PROVIDED;

import java.util.GregorianCalendar;

import clc.common.InvalidInputException;
import clc.common.LogHelper;

public class UpdateAnalyzer extends TimeParser {
	private static boolean isCaseUpdateCalendar, isCaseQuotedTaskName;
	private static String[] tempInfo;
	private static int calendarProvided = 0;
	private static int seqNo;
	private static GregorianCalendar tempStartCalendar;

	protected UpdateAnalyzer(String input) {
		super(input);
	}

	protected static void analyze() throws InvalidInputException {
		throwExceptionIfEmptyCommandDetails();
		throwExceptionIfNoSeqNumberProvided();
		checkIfQuotedTaskName();
		addSpaceToCommaIfNotQuotedTaskName();

		isCaseQuotedTaskName = false;
		calendarProvided = 0;
		tempInfo = commandDetails.split(SPACE);
		infoToBeProcessed = tempInfo;

		if (!isCaseQuotedTaskName && doesContainTimeInfo()) { //case update calendar
			setCalendarInfoForUpdate();
			isCaseUpdateCalendar = true;
		} else { //case update task name
			isCaseUpdateCalendar = false;
		}
	}

	private static void addSpaceToCommaIfNotQuotedTaskName() {
		if (commandDetails.contains(COMMA)) {
			int indexOfFirstComma = commandDetails.indexOf(COMMA);
			int indexOfLastComma = commandDetails.lastIndexOf(COMMA);
			indexOfLastComma = findCommaAndAddSpaces(indexOfFirstComma,
														indexOfLastComma);
		}
	}

	private static int findCommaAndAddSpaces(int indexOfFirstComma,
			int indexOfLastComma) {
		for (int i = indexOfFirstComma; i <= indexOfLastComma; i ++) {
			if (commandDetails.charAt(i) == CHAR_COMMA) {
				i = addSpaceToTheFrontOfCommaIfNotSpace(i);
				indexOfLastComma += addSpaceToTheBackOfCommaIfNotSpace(i);
			}
		}
		return indexOfLastComma;
	}

	private static int addSpaceToTheBackOfCommaIfNotSpace(int i) {
		if (i + 1 < commandDetails.length() 
				&& commandDetails.charAt(i + 1) != CHAR_SPACE) {
			commandDetails = commandDetails.substring(0, i + 1) + SPACE
					+ commandDetails.substring(i + 1, commandDetails.length());
			return 1;
		}
		return 0;
	}

	private static int addSpaceToTheFrontOfCommaIfNotSpace(int i) {
		if (i - 1 >= 0 && commandDetails.charAt(i - 1) != ' ') {
			commandDetails = commandDetails.substring(0, i) + SPACE
					+ commandDetails.substring(i, commandDetails.length());
			i ++;
		}
		return i;
	}

	private static void setCalendarInfoForUpdate() throws InvalidInputException {
		if (commandDetails.contains(COMMA)) {
			tempStartCalendar = null;
			int indexOfComma = findIndexOfComma();
			
			analyzeUpdateStartTime(indexOfComma);
			analyzeUpdateEndTime(indexOfComma);
			//comma case parse two string separately 
			//tempStartCalendar is required to store the startCalendar 
			startCalendar = tempStartCalendar;
		} else { //update without using comma
			determineIfStartDateIsProvided();
			determineIfStartTimeIsProvided();
			determineIfEndDateIsProvided();
			determineIfEndTimeIsProvided();
		}
		LogHelper.info("Update case: " + CALENDAR_PROVIDED[calendarProvided - 1]);
	}

	private static void checkIfQuotedTaskName() {
		if (isQuotedCommandDetails()) {
			isCaseQuotedTaskName = true;
			//trim away quotation mark
			commandDetails = commandDetails.substring(1, commandDetails.length() - 1);
			LogHelper.info("New quoted task name: " + commandDetails);
		}
	}

	private static boolean isQuotedCommandDetails() {
		return commandDetails.indexOf(QUOTATION_MARK) == 0 
				&& commandDetails.lastIndexOf(QUOTATION_MARK) == commandDetails.length() - 1;
	}

	private static void throwExceptionIfNoSeqNumberProvided() throws InvalidInputException {
		if (!isNumeric(getFirstWord(commandDetails))) {
			LogHelper.info("No sequence number is indicated");
			throw new InvalidInputException(ERROR_NO_SEQUENCE_NUMBER);
		} else {
			seqNo = Integer.parseInt(getFirstWord(commandDetails));
			commandDetails = removeFirstWord(commandDetails);
		}
	}

	private static void analyzeUpdateStartTime(int indexOfComma) throws InvalidInputException {

		if (doesContainInfoBeforeComma(indexOfComma)) {
			setStartTimeInfo(indexOfComma);
			processCalendarInfo();

			throwExceptionIfUpdateWithRecurringTime();

			//TimeParser parse calendar information from the back
			//therefore, have to set separately
			setIsStartDateTrueIfDateIsSet();
			setIsStartTimeTrueIfTimeIsSet();

			// as processCalendarInfo will set the time to endTime
			// so we have to swap the value
			tempStartCalendar = endCalendar;
			endCalendar = null;

			determineIfStartDateIsProvided();
			determineIfStartTimeIsProvided();
			if (tempStartCalendar != null) {
				LogHelper.info("New start calendar: " + tempStartCalendar.getTime().toString());
			}
		}
	}

	private static void throwExceptionIfUpdateWithRecurringTime() throws InvalidInputException {
		if (isRecurringEveryWeek || isRecurringEveryday) {
			throw new InvalidInputException(ERROR_CANNOT_UPDATE_TO_RECURRING_TASK);
		}
	}

	private static void setIsStartTimeTrueIfTimeIsSet() {
		if (isEndTimeSet) {
			isStartTimeSet = true;
		}
	}

	private static void setIsStartDateTrueIfDateIsSet() {
		if (isEndDateSet) {
			isStartDateSet = true;
		}
	}

	private static void setStartTimeInfo(int indexOfComma) {
		int index = 0;
		int size = indexOfComma;
		infoToBeProcessed = new String[size];
		for (int i = 0; i < indexOfComma; i ++) {
			infoToBeProcessed[index ++] = tempInfo[i];
		}
	}

	private static boolean doesContainInfoBeforeComma(int indexOfComma) {
		return indexOfComma > 0;
	}

	private static void analyzeUpdateEndTime(int indexOfComma) throws InvalidInputException {
		if (doesContainInfoAfterComma(indexOfComma)) {
			setEndTimeInfo(indexOfComma);

			processCalendarInfo();

			determineIfEndDateIsProvided();
			determineIfEndTimeIsProvided();
			if (endCalendar != null) {
				LogHelper.info("New end calendar: " + endCalendar.getTime().toString());
			}
		}
	}

	private static void setEndTimeInfo(int indexOfComma) {
		int index = 0;
		int size = tempInfo.length - indexOfComma - 1;
		infoToBeProcessed = new String[size];
		for (int i = indexOfComma + 1; i < tempInfo.length ; i ++) {
			infoToBeProcessed[index ++] = tempInfo[i];
		}
	}

	private static boolean doesContainInfoAfterComma(int indexOfComma) {
		return tempInfo.length != indexOfComma + 1;
	}

	/*
	 * calendarProvided
	 * case 1: update end time only
	 * case 2: update end date only
	 * case 3: update end date and end time only
	 * case 4: update start time only
	 * case 5: update start time and end time only
	 * case 6: update start time and end date only
	 * case 7: update start time, end date and end time
	 * case 8: update start date only
	 * case 9: update start date and end time only
	 * case 10: update start date and end date only
	 * case 11: update start date, end date and end time
	 * case 12: update start date and start time only
	 * case 13: update start date, start time and end time
	 * case 14: update start date, start time and end date
	 * case 15: update start date, start time, end date and end time
	 */

	private static void determineIfStartDateIsProvided() {
		if (isStartDateSet) {
			calendarProvided += 8;
		}
	}

	private static void determineIfStartTimeIsProvided() {
		if (isStartTimeSet) {
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

	private static int findIndexOfComma() {
		for(int i = 0; i < tempInfo.length; i ++) {
			if(tempInfo[i].equals(COMMA)) {
				return i;
			}
		}
		return -1;
	}

	protected static boolean getUpdateCase() {
		return isCaseUpdateCalendar;
	}

	protected static int getSeqNumForUpdate() throws InvalidInputException {
		return seqNo;
	}

	protected static String getNewTaskName() throws InvalidInputException {
		if (isNewTaskNameProvided(commandDetails)) {
			throw new InvalidInputException(ERROR_NO_NEW_TASK_NAME);
		}
		return commandDetails;
	}

	private static boolean isNewTaskNameProvided(String newTaskName) {
		return newTaskName.equals(EMPTY);
	}

	protected static int getCalendarProvidedCase() {
		return calendarProvided;
	}

	//Override TimeParser methods
	protected static boolean doesContainTimeInfo() throws InvalidInputException {
		processCalendarInfo();
		return isStartDateSet || isStartTimeSet 
				|| isEndDateSet || isEndTimeSet;
	}

	protected static void processCalendarInfo() throws InvalidInputException {
		initializeVariable();
		analyzeAndSetCalendar();
		setStartCalendarAsNullIfNotSet();
		setEndCalendarAsNullIfNotSet();
	}
}
