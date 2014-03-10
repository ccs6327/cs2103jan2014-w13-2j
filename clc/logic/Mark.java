package clc.logic;

import java.util.ArrayList;
import java.util.Calendar;
import static clc.common.Constants.*;

public class Mark implements Command {
	ArrayList<Integer> taskSeqNo;
	
	public Mark(ArrayList<Integer> taskSeqNo) {
		this.taskSeqNo = taskSeqNo;
	}

	@Override
	public String execute() {
		StringBuilder feedback = new StringBuilder();
		for (int i = 0; i < taskSeqNo.size(); i++) {
			int seqNo = taskSeqNo.get(i);
			
			if (isOutOfBound(internalMem.size(), seqNo)) {
				//print error msg
				feedback.append(seqNo + " is Out Of Bound");
				feedback.append("\n");
			} else {
				boolean isMarked = false;
				isMarked = internalMem.get(seqNo - 1).markDone();
				
				String taskName = internalMem.get(seqNo - 1).getTaskName();
				if (isMarked) {
					feedback.append(taskName + " mark as done sucessfully");
					feedback.append("\n");
				} else { //task is marked before this
					feedback.append(taskName + " is already marked as done");
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
