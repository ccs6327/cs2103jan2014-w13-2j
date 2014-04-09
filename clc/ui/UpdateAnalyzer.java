package clc.ui;

import static clc.common.Constants.SPACE;
import static clc.common.Constants.COMMA;
import static clc.common.Constants.EMPTY;
import static clc.common.Constants.QUOTATION_MARK;
import static clc.common.Constants.ERROR_NO_SEQUENCE_NUMBER;
import static clc.common.Constants.ERROR_NO_NEW_TASK_NAME;

import java.text.ParseException;
import java.util.GregorianCalendar;

import clc.common.InvalidInputException;

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

		isCaseQuotedTaskName = false;
		calendarProvided = 0;
		tempInfo = commandDetails.split(SPACE);
		infoToBeProcessed = tempInfo;

		if (!isCaseQuotedTaskName && doesContainTimeInfo()) { //case update calendar
			if (commandDetails.contains(COMMA)) {
				int indexOfComma = findIndexOfComma();
				analyzeUpdateStartTime(indexOfComma);
				analyzeUpdateEndTime(indexOfComma);
				startCalendar = tempStartCalendar;
			} else { // one calendar -> endCalendar , two calendar -> start and end calendar
				determineIfStartDateIsProvided();
				determineIfStartTimeIsProvided();
				determineIfEndDateIsProvided();
				determineIfEndTimeIsProvided();
			}
			
			isCaseUpdateCalendar = true;
		} else { //case update task name
			isCaseUpdateCalendar = false;
		}
	}

	private static void checkIfQuotedTaskName() {
		if (isQuotedCommandDetails()) {
			isCaseQuotedTaskName = true;
			//trim away '
			commandDetails = commandDetails.substring(1, commandDetails.length() - 1);
		}
	}

	private static boolean isQuotedCommandDetails() {
		return commandDetails.indexOf(QUOTATION_MARK) == 0 
				&& commandDetails.lastIndexOf(QUOTATION_MARK) == commandDetails.length() - 1;
	}

	private static void throwExceptionIfNoSeqNumberProvided() throws InvalidInputException {
		if (!isNumeric(getFirstWord(commandDetails))) {
			throw new InvalidInputException(ERROR_NO_SEQUENCE_NUMBER);
		} else {
			seqNo = Integer.parseInt(getFirstWord(commandDetails));
			commandDetails = removeFirstWord(commandDetails);
		}
	}

	private static void analyzeUpdateStartTime(int indexOfComma) throws InvalidInputException {
		int index = 0;
		if (indexOfComma > 0) {
			infoToBeProcessed = new String[indexOfComma];
			for (int i = 0; i < indexOfComma; i ++) {
				infoToBeProcessed[index ++] = tempInfo[i];
			}
			processCalendarInfo();
			
			if (isEndDateSet) {
				isStartDateSet = true;
			}
			
			if (isEndTimeSet) {
				isStartTimeSet = true;
			}
			// as processCalendarInfo will set the time to endTime
			// so we have to swap the value
			tempStartCalendar = endCalendar;
			endCalendar = null;

			determineIfStartDateIsProvided();
			determineIfStartTimeIsProvided();
		}
	}

	private static void analyzeUpdateEndTime(int indexOfComma) throws InvalidInputException {
		int index = 0;
		if (tempInfo.length != indexOfComma + 1) {
			infoToBeProcessed = new String[tempInfo.length - indexOfComma - 1];
			for (int i = indexOfComma + 1; i < tempInfo.length ; i ++) {
				infoToBeProcessed[index ++] = tempInfo[i];
			}
	
			processCalendarInfo();

			determineIfEndDateIsProvided();
			determineIfEndTimeIsProvided();
		}
	}

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

	public static boolean getUpdateCase() {
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

	public static int getCalendarProvidedCase() {
		return calendarProvided;
	}
	
	protected static void processCalendarInfo() throws InvalidInputException {
		initializeVariable();
		int currIndex = infoToBeProcessed.length - 1;
		
		while (currIndex >= 0 && !hasAllTimeSet()) {
			String toBeAnalyzedString = EMPTY;
			int loopIndex = currIndex;
			for (int i = 0; i < 3; i ++) { // calendar at most represent by 3 Strings
				toBeAnalyzedString = infoToBeProcessed[loopIndex] +  toBeAnalyzedString;
				try {
					analyzeTime(toBeAnalyzedString);
					if (endCalendarIndex == -1) {
						startCalendarIndex = loopIndex;
						endCalendarIndex = loopIndex;
					} else if (startCalendarIndex == -1 || loopIndex < startCalendarIndex) {
						startCalendarIndex = loopIndex;
					}
					currIndex = loopIndex;
					break;
				} catch (ParseException e) { //catch parsing error
					if (loopIndex - 1 < 0) {
						break;
					}
				}
				loopIndex --;
			}
			currIndex --;
			setCalendar();
		}
		
		setStartCalendarAsNullIfNotSet();
		setEndCalendarAsNullIfNotSet();
	}
}
