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
		
		// append message with the sequence enter by user
		for (int i = 0; i < taskSeqNo.size(); i++) {
			int seqNo = taskSeqNo.get(i);
			if (isOutOfBound(displayMem.size(), seqNo)) {
				//build error message
				feedback.append(String.format(MESSAGE_OUT_OF_BOUND, seqNo));
				feedback.append("\n");
			} else {
				int internalSeqNo = displayMem.get(seqNo - 1);
				String taskName = internalMem.get(internalSeqNo).getTaskName();
				feedback.append(String.format(MESSAGE_TASK_DELETED, taskName));
				feedback.append("\n");
			}
		}
		
		History.setDisplayMem();
		Collections.sort(taskSeqNo);
		
		//delete from the back of the list
		for (int i = taskSeqNo.size() - 1; i >= 0; i--) {
			int seqNo = taskSeqNo.get(i);
			
			if (!isOutOfBound(displayMem.size(), seqNo)) {
				isChanged = true;
				int internalSeqNo = displayMem.get(seqNo - 1); 
				displayMem.remove(seqNo - 1);
				internalMem.remove(internalSeqNo);
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
