package clc.ui;

import static clc.common.Constants.SPACE;
import static clc.common.Constants.EMPTY;
import static clc.common.Constants.ERROR_NO_TASK_NAME;
import static clc.common.Constants.ERROR_REMINDER_FOR_FLOATING_TASK;
import static clc.common.Constants.ERROR_EMPTY_COMMAND_DETAILS;
import static clc.common.Constants.QUOTATION_MARK;
import static clc.common.Constants.SLASH_R;
import clc.common.InvalidInputException;
import clc.logic.Task;

public class AddAnalyzer extends TimeParser {
	private static String taskName;
	private static Task taskToBeAdded;
	private static boolean isReminderNeeded;
	private static boolean isCaseQuoteTaskName;

	protected AddAnalyzer(String input) {
		super(input);
	}

	protected static void analyze() throws InvalidInputException {
		determineIfReminderNeeded();

		setUpInfoToBeProcessed();

		if (doesContainTimeInfo()) { //timed task or deadline task
			//merge the taskName
			taskName = mergeNotQuotedTaskName();
			determineIfTaskNameProvided();

			if (isCaseDeadlineTask()) {
				taskToBeAdded = new Task(taskName, endCalendar);
			} else if (isCaseTimedTask()) {
				taskToBeAdded = new Task(taskName, startCalendar, endCalendar);
			} else {
				throw new InvalidInputException(ERROR_EMPTY_COMMAND_DETAILS);
			}

			if (isReminderNeeded) {
				taskToBeAdded.setReminder();
			}
			
			if (isRecurringTime) { //support for unlimited time of recurring
				taskToBeAdded.setRecurringTime(Integer.MAX_VALUE);
			}
		} else { // floating task
			if (isReminderNeeded) {
				throw new InvalidInputException(ERROR_REMINDER_FOR_FLOATING_TASK);
			}
			taskName = commandDetails;
			taskToBeAdded = new Task(taskName);
		}
	}
	
	

	private static void setUpInfoToBeProcessed() {
		isCaseQuoteTaskName = false;
		if (commandDetails.contains(QUOTATION_MARK)) {
			int startTaskNameIndex = commandDetails.indexOf(QUOTATION_MARK);
			int endTaskNameIndex = commandDetails.lastIndexOf(QUOTATION_MARK);

			if (startTaskNameIndex != endTaskNameIndex) {
				isCaseQuoteTaskName = true;
				taskName = commandDetails.substring(startTaskNameIndex + 1, endTaskNameIndex);
				if (endTaskNameIndex == commandDetails.length() - 1) {
					infoToBeProcessed = commandDetails.substring(0, startTaskNameIndex).split(SPACE);
				} else if (startTaskNameIndex == 0){
					infoToBeProcessed = commandDetails.substring(endTaskNameIndex + 1, commandDetails.length()).split(SPACE);
				} else {
					infoToBeProcessed = commandDetails.split(SPACE);
					taskName = EMPTY;
					isCaseQuoteTaskName = false;
				}
			} else {
				infoToBeProcessed = commandDetails.split(SPACE);
			}
		} else {
			infoToBeProcessed = commandDetails.split(SPACE);
		}
	}

	private static void determineIfReminderNeeded() {
		isReminderNeeded = false;
		if (getFirstWord(commandDetails).equalsIgnoreCase(SLASH_R)) {
			isReminderNeeded = true;
			commandDetails = removeFirstWord(commandDetails);
		}
	}

	private static void determineIfTaskNameProvided()
			throws InvalidInputException {
		if (isEmptyTaskName()) {
			throw new InvalidInputException(ERROR_NO_TASK_NAME);
		}
	}

	private static boolean isEmptyTaskName() {
		return taskName.equals(EMPTY);
	}

	protected static Task getToBeAddedTask() {
		return taskToBeAdded;
	}

	private static String mergeNotQuotedTaskName() {
		if (!isCaseQuoteTaskName){
			taskName = EMPTY;
			for (int i = 0; i < startCalendarIndex; i++) {
				taskName += (infoToBeProcessed[i] + SPACE);
			}
			for (int i = endCalendarIndex + 1; i < infoToBeProcessed.length; i++) {
				taskName += (infoToBeProcessed[i] + SPACE);
			}
		}

		return taskName.trim();
	}
}
