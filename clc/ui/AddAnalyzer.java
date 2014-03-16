package clc.ui;

import static clc.common.Constants.SPACE;

import clc.logic.Task;

public class AddAnalyzer extends TimeAnalyzer {
	private static String taskName;
	private static Task taskToBeAdded;

	protected AddAnalyzer(String input) {
		super(input);
	}

	protected static void analyze() {
		infoToBeProcessed = commandDetails.split(SPACE);
		
		//**handle empty name case throw exception
		
		if (doesContainTimeInfo()) { //timed task or deadline task
			recordAndProcessCalendarInfoProvided();

			//merge the taskName
			taskName = mergeTaskName();

			//startTime = TimeAnalyzer.startTime;
			//endTime = TimeAnalyzer.endTime;
			
			if (isCaseDeadlineTask()) {
				taskToBeAdded = new Task(taskName, endTime);
			} else if (isCaseTimedTask()) {
				taskToBeAdded = new Task(taskName, startTime, endTime);
			} else {
				//handle exception ****
				System.out.println("invalid calendar format");
			}
		} else { // floating task
			taskName = commandDetails;
			taskToBeAdded = new Task(taskName);
		}
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
