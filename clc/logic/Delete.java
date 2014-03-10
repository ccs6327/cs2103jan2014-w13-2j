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
		Collections.sort(taskSeqNo);

		for (int i = taskSeqNo.size() - 1; i >= 0; i--) {
			//ArrayList<Task> taskList = storage.getTaskList();
			int seqNo = taskSeqNo.get(i);
			
			if (isOutOfBound(internalMem.size(), seqNo)) {
				//print error msg
				System.out.println("Out Of Bound");
			} else {
				String taskName = internalMem.get(seqNo - 1).getTaskName();

				internalMem.remove(seqNo - 1);
				// print confirmation message
				System.out.println("deleted " + taskName);
			}
		}
		
		return "";
	}

	private boolean isOutOfBound(int taskListSize, int seqNo) {
		return (taskListSize < seqNo || seqNo <= 0);
	}
}
