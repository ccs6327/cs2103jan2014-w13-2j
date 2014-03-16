package clc.logic;

import java.util.ArrayList;
import java.util.Collections;

import clc.storage.Storage;
import clc.storage.History;
import static clc.common.Constants.*;
import static clc.storage.History.historyMem;
import static clc.storage.History.currentVersion;
import static clc.storage.Storage.internalMem;

public class Delete implements Command {
	private ArrayList<Integer> taskSeqNo;
	
	public Delete(ArrayList<Integer> taskSeqNo) {
		this.taskSeqNo = taskSeqNo;
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
				String taskName = displayMem.get(seqNo - 1).getTaskName();
				long idNum = displayMem.get(seqNo - 1).getTaskId();
				displayMem.remove(seqNo - 1);
				searchAndDeleteInternalMemTask(idNum);
				
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

	private void searchAndDeleteInternalMemTask(long idNum) {
		for (int i = 0; i < internalMem.size(); i++) {
			if (internalMem.get(i).getTaskId() == idNum) {
				internalMem.remove(i);
			}
		}
	}

	private boolean isOutOfBound(int taskListSize, int seqNo) {
		return (taskListSize < seqNo || seqNo <= 0);
	}
}
