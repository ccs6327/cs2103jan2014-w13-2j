//@author A0112089J

package clc.logic;

import java.util.ArrayList;
import java.util.Collections;

import clc.storage.Storage;
import clc.storage.History;

import static clc.common.Constants.MESSAGE_OUT_OF_BOUND;
import static clc.common.Constants.MESSAGE_TASK_DELETED;
import static clc.common.Constants.NEWLINE;

public class Delete implements Command {
	private ArrayList<Integer> taskSeqNo;
	private ArrayList<Task> internalMem;
	private ArrayList<Integer> displayMem;
	
	public Delete(ArrayList<Integer> taskSeqNo) {
		this.taskSeqNo = taskSeqNo;
		internalMem = Storage.getInternalMem();
		displayMem = Storage.getDisplayMem();
	}
	
	@Override
	public String execute() {
		assert taskSeqNo != null; 
		
		StringBuilder feedback = new StringBuilder();
		boolean isChanged = false;

		//save a backup for undo
		History.setDisplayMem();
		appendMessageAccordingToSequence(feedback);
		isChanged = deleteFromTheBackIfInBoundary(isChanged);
		updateHistoryAndDatabaseIfChanged(isChanged);
		
		//after delete function displayMem is different
		//displayMem must be cleared otherwise delete function can still be function after first deletion
		displayMem.clear();
		
		assert feedback != null;
		
		return feedback.toString();
	}

	private void appendMessageAccordingToSequence(StringBuilder feedback) {
		for (int i = 0; i < taskSeqNo.size(); i++) {
			int seqNo = taskSeqNo.get(i);
			if (isOutOfBound(displayMem.size(), seqNo)) {
				appendOutOfBoundaryMessage(feedback, seqNo);
			} else {
				appendTaskDeletedMessage(feedback, seqNo);
			}
		}
	}

	private void appendOutOfBoundaryMessage(StringBuilder feedback, int seqNo) {
		feedback.append(String.format(MESSAGE_OUT_OF_BOUND, seqNo));
		feedback.append(NEWLINE);
	}

	private void appendTaskDeletedMessage(StringBuilder feedback, int seqNo) {
		int internalSeqNo = displayMem.get(seqNo - 1);
		String taskName = internalMem.get(internalSeqNo).getTaskName();
		feedback.append(String.format(MESSAGE_TASK_DELETED, taskName));
		feedback.append(NEWLINE);
	}

	private boolean deleteFromTheBackIfInBoundary(boolean isChanged) {
		Collections.sort(taskSeqNo);
		for (int i = taskSeqNo.size() - 1; i >= 0; i--) {
			int seqNo = taskSeqNo.get(i);
			
			if (!isOutOfBound(displayMem.size(), seqNo)) {
				isChanged = true;
				int internalSeqNo = displayMem.get(seqNo - 1); 
				displayMem.remove(seqNo - 1);
				internalMem.remove(internalSeqNo);
			}
		}
		return isChanged;
	}

	private boolean isOutOfBound(int taskListSize, int seqNo) {
		return (taskListSize < seqNo || seqNo <= 0);
	}

	private void updateHistoryAndDatabaseIfChanged(boolean isChanged) {
		if (isChanged) {
			History.addNewVersion();
			Storage.writeContentIntoFile();
		}
	}
}
