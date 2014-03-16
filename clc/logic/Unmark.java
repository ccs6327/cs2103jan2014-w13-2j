package clc.logic;

import java.util.ArrayList;

import clc.storage.History;
import clc.storage.Storage;
import static clc.common.Constants.*;

public class Unmark implements Command {
	private ArrayList<Integer> taskSeqNo;
	private StringBuilder feedback = new StringBuilder();
	private ArrayList<Task> displayMem;
	
	public Unmark(ArrayList<Integer> taskSeqNo) {
		this.taskSeqNo = taskSeqNo;
		displayMem = Storage.getDisplayMem();
	}

	@Override
	public String execute() {
		for (int i = 0; i < taskSeqNo.size(); i++) {
			int seqNo = taskSeqNo.get(i);

			if (isOutOfBound(displayMem.size(), seqNo)) {
				feedback.append(String.format(MESSAGE_OUT_OF_BOUND, seqNo));
				feedback.append("\n");
			} else {
				boolean isUnmarked = false;
				isUnmarked = displayMem.get(seqNo - 1).markUndone();

				String taskName = displayMem.get(seqNo - 1).getTaskName();
				if (isUnmarked) {
					feedback.append(String.format(MESSAGE_MARK_NOT_DONE, taskName));
					feedback.append("\n");
				} else { //task is not marked as done previously
					feedback.append(String.format(MESSAGE_PREVIOUSLY_MARK_NOT_DONE, taskName));
					feedback.append("\n");
				}
			}
		}
		
		History.addNewVersion();
		Storage.writeContentIntoFile();
		
		return feedback.toString().trim();
	}

	private boolean isOutOfBound(int taskListSize, int seqNo) {
		return (taskListSize < seqNo || seqNo <= 0);
	}
}

