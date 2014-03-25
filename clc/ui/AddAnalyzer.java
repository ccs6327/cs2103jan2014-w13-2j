package clc.ui;

import static clc.common.Constants.*;
import clc.common.InvalidInputException;
import clc.logic.Task;

public class AddAnalyzer extends TimeAnalyzer {
	private static String taskName;
	private static Task taskToBeAdded;
	private static boolean isReminderNeeded;

	protected AddAnalyzer(String input) {
		super(input);
	}

	protected static void analyze() throws InvalidInputException {
		determineIfReminderNeeded();
		infoToBeProcessed = commandDetails.split(SPACE);
		
		if (doesContainTimeInfo()) { //timed task or deadline task
			
			recordAndProcessCalendarInfoProvided();
			determineIfStartTimeLaterThanEndTime();
			
			//merge the taskName
			taskName = mergeTaskName();
			determineIfTaskNameProvided();
			
			if (isCaseDeadlineTask()) {
				taskToBeAdded = new Task(taskName, endTime);
			} else if (isCaseTimedTask()) {
				taskToBeAdded = new Task(taskName, startTime, endTime);
			} else {
				throw new InvalidInputException(MESSAGE_INVALID_FORMAT);
			}
			
			if (isReminderNeeded) {
				taskToBeAdded.setReminder();
			}
		} else { // floating task
			if (isReminderNeeded) {
				throw new InvalidInputException("ERROR: no reminder is allowed for floating task");
			}
			taskName = commandDetails;
			taskToBeAdded = new Task(taskName);
		}
	}

	private static void determineIfReminderNeeded() {
		if (getFirstWord(commandDetails).equals("-r")) {
			isReminderNeeded = true;
			commandDetails = removeFirstWord(commandDetails);
		}
	}

	private static void determineIfTaskNameProvided()
			throws InvalidInputException {
		if (isEmptyTaskName()) {
			throw new InvalidInputException("ERROR: no task name is given");
		}
	}

	private static boolean isEmptyTaskName() {
		return taskName.equals("");
	}
	
	protected static Task getToBeAddedTask() {
		return taskToBeAdded;
	}
	
	private static String mergeTaskName() {
		String taskName = "";
		for (int i = 0; i < firstDateIndex; i++) {
			taskName += (infoToBeProcessed[i] + SPACE);
		}
		return taskName.trim();
	}
}
