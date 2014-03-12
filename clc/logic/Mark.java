package clc.logic;

import java.util.ArrayList;

import clc.storage.Storage;
import static clc.common.Constants.*;

public class Mark implements Command {
	private ArrayList<Integer> taskSeqNo;
	private StringBuilder feedback = new StringBuilder();
	
	public Mark(ArrayList<Integer> taskSeqNo) {
		this.taskSeqNo = taskSeqNo;
	}

	@Override
	public String execute() {
		for (int i = 0; i < taskSeqNo.size(); i++) {
			int seqNo = taskSeqNo.get(i);
			
			if (isOutOfBound(displayMem.size(), seqNo)) {
				//print error msg
				feedback.append(String.format(MESSAGE_OUT_OF_BOUND, seqNo));
				feedback.append("\n");
			} else {
				boolean isMarked = false;
				isMarked = displayMem.get(seqNo - 1).markDone();
				
				String taskName = displayMem.get(seqNo - 1).getTaskName();
				if (isMarked) {
					feedback.append(String.format(MESSAGE_MARK_DONE, taskName));
					feedback.append("\n");
				} else { //task is marked before this
					feedback.append(String.format(MESSAGE_PREVIOUSLY_MARK_DONE, taskName));
					feedback.append("\n");
				}
				addNewVersion();
	    		Storage.writeContentIntoFile();
			}
		}
		return feedback.toString().trim();
	}

	private boolean isOutOfBound(int taskListSize, int seqNo) {
		return (taskListSize < seqNo || seqNo <= 0);
	}
}
