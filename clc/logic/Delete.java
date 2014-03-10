package clc.logic;

import java.util.ArrayList;
import java.util.Collections;

import static clc.common.Constants.*;

public class Delete implements Command {
	ArrayList<Integer> taskSeqNo;
	
	public Delete(ArrayList<Integer> taskSeqNo) {
		this.taskSeqNo = taskSeqNo;
	}
	
	@Override
	public String execute() {
		StringBuilder feedback = new StringBuilder();
		Collections.sort(taskSeqNo);

		for (int i = taskSeqNo.size() - 1; i >= 0; i--) {
			//ArrayList<Task> taskList = storage.getTaskList();
			int seqNo = taskSeqNo.get(i);
			
			if (isOutOfBound(internalMem.size(), seqNo)) {
				//print error msg
				feedback.append(seqNo + " is Out Of Bound");
				feedback.append("\n");
			} else {
				String taskName = internalMem.get(seqNo - 1).getTaskName();

				internalMem.remove(seqNo - 1);
				feedback.append("deleted " + taskName);
				feedback.append("\n");
			}
		}
		
		return feedback.toString();
	}

	private boolean isOutOfBound(int taskListSize, int seqNo) {
		return (taskListSize < seqNo || seqNo <= 0);
	}
}
