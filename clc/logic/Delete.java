package clc.logic;

import java.util.ArrayList;
import java.util.Collections;

import clc.storage.Storage;
import clc.storage.History;
import static clc.common.Constants.*;

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
		StringBuilder feedback = new StringBuilder();
		boolean isChanged = false;
		Collections.sort(taskSeqNo);
		
		for (int i = taskSeqNo.size() - 1; i >= 0; i--) {
			//ArrayList<Task> taskList = storage.getTaskList();
			int seqNo = taskSeqNo.get(i);
			
			if (isOutOfBound(displayMem.size(), seqNo)) {
				//print error msg
				feedback.append(String.format(MESSAGE_OUT_OF_BOUND, seqNo));
				feedback.append("\n");
			} else {
				isChanged = true;
				int internalSeqNo = displayMem.get(seqNo - 1); 
				String taskName = internalMem.get(internalSeqNo).getTaskName();
				displayMem.remove(seqNo - 1);
				internalMem.remove(internalSeqNo);
				
				feedback.append(String.format(MESSAGE_TASK_DELETED, taskName));
				feedback.append("\n");
			}
		}
		
		if (isChanged) {
			History.addNewVersion();
			Storage.writeContentIntoFile();
		}
		
		displayMem.clear();
		return feedback.toString();
	}

	private boolean isOutOfBound(int taskListSize, int seqNo) {
		return (taskListSize < seqNo || seqNo <= 0);
	}
}
