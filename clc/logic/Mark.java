//author A0112089J

package clc.logic;

import java.util.ArrayList;
import java.util.Calendar;

import clc.storage.History;
import clc.storage.Storage;

import static clc.common.Constants.MESSAGE_OUT_OF_BOUND;
import static clc.common.Constants.MESSAGE_MARK_DONE;
import static clc.common.Constants.MESSAGE_PREVIOUSLY_MARK_DONE;
import static clc.common.Constants.EVERYDAY;
import static clc.common.Constants.EVERY_WEEK;
import static clc.common.Constants.EMPTY;
import static clc.common.Constants.NEWLINE;

public class Mark implements Command {
	private ArrayList<Integer> taskSeqNo;
	private StringBuilder feedback = new StringBuilder();
	private ArrayList<Integer> displayMem;
	private ArrayList<Task> internalMem;
	private ArrayList<Task> reminderMem;
	private boolean isReminderTask;
	private long targetTaskId;
	
	public Mark(ArrayList<Integer> taskSeqNo) {
		this.taskSeqNo = taskSeqNo;
		displayMem = Storage.getDisplayMem();
		internalMem = Storage.getInternalMem();
	}
	
	//constructor for marking reminder task
	public Mark(long taskId) {
		isReminderTask = true;
		reminderMem = Remind.getReminderMem();
		targetTaskId = taskId;
	}

	@Override
	public String execute() {
		if (isReminderTask) {
			markReminderMem();
			return EMPTY;
		}
		return markDisplayMem();
	}

	private void markReminderMem() {
		for (int i = reminderMem.size() - 1; i >= 0; i --) {
			Task task = reminderMem.get(i);
			if (task.getTaskId() == targetTaskId) {
				task.markDone();
	    		Storage.writeContentIntoFile();
				break;
			}
		}
	}

	private String markDisplayMem() {
		boolean isChanged = markTasks();
		updateHistoryAndDatabaseIfChanged(isChanged);
		return feedback.toString().trim();
	}

	private boolean markTasks() {
		boolean isChanged = false;
		for (int i = 0; i < taskSeqNo.size(); i++) {
			int seqNo = taskSeqNo.get(i);
			isChanged = markTaskIfInBoundary(isChanged, seqNo);
		}
		return isChanged;
	}

	private boolean markTaskIfInBoundary(boolean isChanged, int seqNo) {
		if (isOutOfBound(displayMem.size(), seqNo)) {
			appendOutOfBoundaryMessage(seqNo);
		} else {
			isChanged = markTaskAndAppendFeedbackMessage(isChanged, seqNo);
		}
		return isChanged;
	}

	private boolean isOutOfBound(int taskListSize, int seqNo) {
		return (taskListSize < seqNo || seqNo <= 0);
	}

	private boolean markTaskAndAppendFeedbackMessage(boolean isChanged, int seqNo) {
		boolean isMarked = false;
		int internalSeqNo = displayMem.get(seqNo - 1); 
		
		Task toBeMarkedTask = internalMem.get(internalSeqNo);
		caseMarkRecurringTask(toBeMarkedTask);
		isMarked = toBeMarkedTask.markDone();
		String taskName = internalMem.get(internalSeqNo).getTaskName();
		if (isMarked) {
			isChanged = true;
			appendMarkDoneMessage(taskName);
		} else { //task is marked before this
			appendPreviouslyMarkDoneMessage(taskName);
		}
		return isChanged;
	}

	private void appendPreviouslyMarkDoneMessage(String taskName) {
		feedback.append(String.format(MESSAGE_PREVIOUSLY_MARK_DONE, taskName));
		feedback.append(NEWLINE);
	}

	private void appendMarkDoneMessage(String taskName) {
		feedback.append(String.format(MESSAGE_MARK_DONE, taskName));
		feedback.append(NEWLINE);
	}

	private void appendOutOfBoundaryMessage(int seqNo) {
		feedback.append(String.format(MESSAGE_OUT_OF_BOUND, seqNo));
		feedback.append(NEWLINE);
	}

	private void updateHistoryAndDatabaseIfChanged(boolean isChanged) {
		if (isChanged) {
			History.addNewVersion();
    		Storage.writeContentIntoFile();
		}
	}

	private void caseMarkRecurringTask(Task toBeMarkedTask) {
		int nRecurring = toBeMarkedTask.getNumberOfRecurring();
		addNewTaskIfRecurring(toBeMarkedTask, nRecurring);
	}

	private void addNewTaskIfRecurring(Task toBeMarkedTask, int nRecurring) {
		if (nRecurring > 0) {
			Task newRecurringTask = new Task(toBeMarkedTask);
			//pass the recurring time to the new task
			//set original task to non-recurring task
			toBeMarkedTask.setNumberOfRecurring(0);
			newRecurringTask.setNumberOfRecurring(--nRecurring);
			constructNewTaskAccordingToRecurringPeriod(newRecurringTask);
			Command addRecurringTask = new Add(newRecurringTask);
			addRecurringTask.execute();
		}
	}

	private void constructNewTaskAccordingToRecurringPeriod(
			Task newRecurringTask) {
		if (isRecurringEveryWeekTask(newRecurringTask)) {
			newRecurringTask.postponeStartAndEndTime(Calendar.WEEK_OF_YEAR);
		} else if (isRecurringEverydayTask(newRecurringTask)) {
			newRecurringTask.postponeStartAndEndTime(Calendar.DAY_OF_YEAR);
		}
	}

	private boolean isRecurringEveryWeekTask(Task newRecurringTask) {
		return newRecurringTask.getRecurringPeriod().equals(EVERY_WEEK);
	}

	private boolean isRecurringEverydayTask(Task newRecurringTask) {
		return newRecurringTask.getRecurringPeriod().equals(EVERYDAY);
	}
}
