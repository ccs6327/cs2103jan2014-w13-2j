//@author A0112089J

package clc.logic;

import java.util.ArrayList;

import clc.storage.History;
import clc.storage.Storage;

import static clc.common.Constants.NEWLINE;
import static clc.common.Constants.MESSAGE_OUT_OF_BOUND;
import static clc.common.Constants.MESSAGE_MARK_NOT_DONE;
import static clc.common.Constants.MESSAGE_PREVIOUSLY_MARK_NOT_DONE;

public class Unmark implements Command {
	private ArrayList<Integer> taskSeqNo;
	private StringBuilder feedback = new StringBuilder();
	private ArrayList<Integer> displayMem;
	private ArrayList<Task> internalMem;
	
	public Unmark(ArrayList<Integer> taskSeqNo) {
		this.taskSeqNo = taskSeqNo;
		displayMem = Storage.getDisplayMem();
		internalMem = Storage.getInternalMem();
	}

	@Override
	public String execute() {
		boolean isChanged = unmarkTasks();
		updateHistoryAndDatabaseIfChanged(isChanged);
		return feedback.toString().trim();
	}

	private boolean unmarkTasks() {
		boolean isChanged = false;
		for (int i = 0; i < taskSeqNo.size(); i++) {
			int seqNo = taskSeqNo.get(i);
			isChanged = unmarkTaskIfInBoundary(isChanged, seqNo);
		}
		return isChanged;
	}

	private boolean unmarkTaskIfInBoundary(boolean isChanged, int seqNo) {
		if (isOutOfBound(displayMem.size(), seqNo)) {
			appendOutOfBoundMessage(seqNo);
		} else {
			isChanged = unmarkTaskAndAppendFeedbackMessage(isChanged, seqNo);
		}
		return isChanged;
	}

	private boolean isOutOfBound(int taskListSize, int seqNo) {
		return (taskListSize < seqNo || seqNo <= 0);
	}

	private void appendOutOfBoundMessage(int seqNo) {
		feedback.append(String.format(MESSAGE_OUT_OF_BOUND, seqNo));
		feedback.append(NEWLINE);
	}

	private boolean unmarkTaskAndAppendFeedbackMessage(boolean isChanged, int seqNo) {
		boolean isUnmarked = false;
		int internalSeqNo = displayMem.get(seqNo - 1);
		isUnmarked = internalMem.get(internalSeqNo).markUndone();
		String taskName = internalMem.get(internalSeqNo).getTaskName();
		
		if (isUnmarked) {
			isChanged = true;
			appendMarkNotDoneMessage(taskName);
		} else { //task is not marked as done previously
			appendPreviouslyNotMarkDoneMessage(taskName);
		}
		return isChanged;
	}

	private void appendMarkNotDoneMessage(String taskName) {
		feedback.append(String.format(MESSAGE_MARK_NOT_DONE, taskName));
		feedback.append(NEWLINE);
	}

	private void appendPreviouslyNotMarkDoneMessage(String taskName) {
		feedback.append(String.format(MESSAGE_PREVIOUSLY_MARK_NOT_DONE, taskName));
		feedback.append(NEWLINE);
	}

	private void updateHistoryAndDatabaseIfChanged(boolean isChanged) {
		if (isChanged) {
			History.addNewVersion();
			Storage.writeContentIntoFile();
		}
	}
}

