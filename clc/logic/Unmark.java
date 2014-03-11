package clc.logic;

import java.util.ArrayList;

import static clc.common.Constants.*;

public class Unmark implements Command {
	private ArrayList<Integer> taskSeqNo;
	private StringBuilder feedback = new StringBuilder();
	
	public Unmark(ArrayList<Integer> taskSeqNo) {
		this.taskSeqNo = taskSeqNo;
	}

	@Override
	public String execute() {
		for (int i = 0; i < taskSeqNo.size(); i++) {
			int seqNo = taskSeqNo.get(i);

			if (isOutOfBound(internalMem.size(), seqNo)) {
				feedback.append(String.format(MESSAGE_OUT_OF_BOUND, seqNo));
				feedback.append("\n");
			} else {
				boolean isUnmarked = false;
				isUnmarked = internalMem.get(seqNo - 1).markUndone();

				String taskName = internalMem.get(seqNo - 1).getTaskName();
				if (isUnmarked) {
					feedback.append(String.format(MESSAGE_MARK_NOT_DONE, taskName));
					feedback.append("\n");
				} else { //task is not marked as done previously
					feedback.append(String.format(MESSAGE_PREVIOUSLY_MARK_NOT_DONE, taskName));
					feedback.append("\n");
				}
			}
		}
		return feedback.toString();
	}

	private boolean isOutOfBound(int taskListSize, int seqNo) {
		return (taskListSize < seqNo || seqNo <= 0);
	}
}

