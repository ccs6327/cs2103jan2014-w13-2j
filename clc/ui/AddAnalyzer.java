//author A0112089J

package clc.ui;

import static clc.common.Constants.SPACE;
import static clc.common.Constants.EMPTY;
import static clc.common.Constants.QUOTATION_MARK;
import static clc.common.Constants.SLASH_R;
import static clc.common.Constants.ERROR_NO_TASK_NAME;
import static clc.common.Constants.ERROR_REMINDER_FOR_FLOATING_TASK;
import static clc.common.Constants.ERROR_EMPTY_COMMAND_DETAILS;

import clc.common.InvalidInputException;

import clc.logic.Task;

public class AddAnalyzer extends TimeParser {
	private static String taskName;
	private static Task taskToBeAdded;
	private static boolean isReminderNeeded;
	private static boolean isCaseQuotedTaskName;

	protected AddAnalyzer(String input) {
		super(input);
	}

	protected static Task getToBeAddedTask() {
		return taskToBeAdded;
	}

	protected static void analyze() throws InvalidInputException {
		throwExceptionIfEmptyCommandDetails();
		determineIfReminderNeeded();
		setUpInfoToBeProcessed(); //info to be processed in time parser

		if (doesContainTimeInfo()) { //timed task or deadline task
			//quoted one does not require to merge anymore
			mergeNotQuotedWordsAsTaskName();
			determineIfTaskNameProvided();
			setUpCalendarTask();
		} else { // floating task
			throwExceptionIfReminderNeeded();
			setCommandDetailsAsTaskNameIfNotQuotedTaskName();
			setUpFloatingTask();
		}
	}

	private static void determineIfReminderNeeded() throws InvalidInputException {
		isReminderNeeded = false;
		if (getFirstWord(commandDetails).equalsIgnoreCase(SLASH_R)) {
			isReminderNeeded = true;
			commandDetails = removeFirstWord(commandDetails);
		}
	}

	private static void setUpInfoToBeProcessed() {
		isCaseQuotedTaskName = false;
		int startQuotationIndex = commandDetails.indexOf(QUOTATION_MARK);
		int endQuotationIndex = commandDetails.lastIndexOf(QUOTATION_MARK);
		if (doesQuotationMarkExist(startQuotationIndex) 
				&& doesContainAtLeastTwoQuotationMarks(startQuotationIndex, endQuotationIndex)) {
			isCaseQuotedTaskName = true;
			setInfoIfQuotationMarkAtTheBeginningOrEnding(startQuotationIndex, endQuotationIndex);
			setQuotedStringAsTaskName(startQuotationIndex, endQuotationIndex);
		}
		else {
			infoToBeProcessed = commandDetails.split(SPACE);
		}
	}

	private static boolean doesQuotationMarkExist(int startQuotationIndex) {
		return startQuotationIndex != -1;
	}

	private static boolean doesContainAtLeastTwoQuotationMarks(int startQuotationIndex, int endQuotationIndex) {
		return startQuotationIndex != endQuotationIndex;
	}

	private static void setInfoIfQuotationMarkAtTheBeginningOrEnding(int startQuotationIndex, int endQuotationIndex) {
		if (isQuotationMarkAtTheBeginning(startQuotationIndex)){
			infoToBeProcessed = commandDetails.substring(endQuotationIndex + 1, commandDetails.length()).split(SPACE);
		} else if (isQuotationMarkAtTheEnding(endQuotationIndex)) {
			infoToBeProcessed = commandDetails.substring(0, startQuotationIndex).split(SPACE);
		} else { //quoted in the middle
			infoToBeProcessed = commandDetails.split(SPACE);
			isCaseQuotedTaskName = false;
		}
	}

	private static boolean isQuotationMarkAtTheBeginning(int startQuotationIndex) {
		return startQuotationIndex == 0;
	}

	private static boolean isQuotationMarkAtTheEnding(int endQuotationIndex) {
		return endQuotationIndex == commandDetails.length() - 1;
	}

	private static void setQuotedStringAsTaskName(int startQuotationIndex, int endQuotationIndex) {
		if (isCaseQuotedTaskName) {
			taskName = commandDetails.substring(startQuotationIndex + 1, endQuotationIndex);
		}
	}

	private static void mergeNotQuotedWordsAsTaskName() {
		if (!isCaseQuotedTaskName){
			taskName = EMPTY;
			mergeWordsBeforeCalendarInformation();
			mergeWordsAfterCalendarInformation();
		}
	}

	private static void mergeWordsBeforeCalendarInformation() {
		for (int i = 0; i < startCalendarIndex; i++) {
			taskName += (infoToBeProcessed[i] + SPACE);
		}
	}

	private static void mergeWordsAfterCalendarInformation() {
		for (int i = endCalendarIndex + 1; i < infoToBeProcessed.length; i++) {
			taskName += (infoToBeProcessed[i] + SPACE);
		}
		taskName = taskName.trim();
	}

	private static void determineIfTaskNameProvided()
			throws InvalidInputException {
		if (isEmptyTaskName()) {
			throw new InvalidInputException(ERROR_NO_TASK_NAME);
		}
	}


	private static void setUpCalendarTask() throws InvalidInputException {
		if (isCaseDeadlineTask()) {
			taskToBeAdded = new Task(taskName, endCalendar);
		} else if (isCaseTimedTask()) {
			taskToBeAdded = new Task(taskName, startCalendar, endCalendar);
		} else {
			throw new InvalidInputException(ERROR_EMPTY_COMMAND_DETAILS);
		}

		caseIfReminderNeeded();
		caseIfRecurringTask();
	}

	private static void caseIfReminderNeeded() {
		if (isReminderNeeded) {
			taskToBeAdded.setReminder();
		}
	}

	private static void caseIfRecurringTask() {
		if (isRecurringEveryWeek) {
			taskToBeAdded.setNumberOfRecurring(Integer.MAX_VALUE);
			taskToBeAdded.setRecurringPeriod(recurringPeriod);
		} else if  (isRecurringEveryday) {
			taskToBeAdded.setNumberOfRecurring(Integer.MAX_VALUE);
			taskToBeAdded.setRecurringPeriod(recurringPeriod);
		}
	}

	private static void throwExceptionIfReminderNeeded()
			throws InvalidInputException {
		if (isReminderNeeded) {
			throw new InvalidInputException(ERROR_REMINDER_FOR_FLOATING_TASK);
		}
	}

	private static void setCommandDetailsAsTaskNameIfNotQuotedTaskName() {
		if (!isCaseQuotedTaskName) {
			taskName = commandDetails;
		}
	}

	private static void setUpFloatingTask() {
		taskToBeAdded = new Task(taskName);
	}

	private static boolean isEmptyTaskName() {
		return taskName.equals(EMPTY);
	}
}
