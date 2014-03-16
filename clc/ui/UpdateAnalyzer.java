package clc.ui;

import static clc.common.Constants.COMMA;
import static clc.common.Constants.SPACE;

public class UpdateAnalyzer extends TimeAnalyzer {
	private static boolean isCaseUpdateCalendar;
	private static String[] tempInfo;

	protected UpdateAnalyzer(String input) {
		super(input);
		// TODO Auto-generated constructor stub
	}

	protected static void analyze() {
		tempInfo = commandDetails.split(SPACE);
		infoToBeProcessed = tempInfo;
		//** throw exception if first is not a digit

		if (doesContainTimeInfo()) { //case update calendar
			int indexOfComma = findIndexOfComma();
			analyzeUpdateStartTime(indexOfComma);
			analyzeUpdateEndTime(indexOfComma);
			isCaseUpdateCalendar = true;	
		} else { //case update task name
			isCaseUpdateCalendar = false;
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
	
	protected static int getSeqNumForUpdate() {
		String currWord = getFirstWord(commandDetails);
		if (isNumeric(currWord)) {
			return Integer.parseInt(currWord);
		}
		return -1; //****handle exception
	}

	protected static String getNewTaskName() {
		return removeFirstWord(commandDetails);
		//** throw exception when removeFirstWord(commandDetails).equals("")
	}
}
