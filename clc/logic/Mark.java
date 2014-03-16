package clc.logic;

import java.util.ArrayList;

import clc.storage.History;
import clc.storage.Storage;
import static clc.common.Constants.*;

public class Mark implements Command {
	private ArrayList<Integer> taskSeqNo;
	private StringBuilder feedback = new StringBuilder();
	private ArrayList<Integer> displayMem;
	private ArrayList<Task> internalMem;
	
	public Mark(ArrayList<Integer> taskSeqNo) {
		this.taskSeqNo = taskSeqNo;
		displayMem = Storage.getDisplayMem();
		internalMem = Storage.getInternalMem();
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
				int internalSeqNo = displayMem.get(seqNo - 1); 
				isMarked = internalMem.get(internalSeqNo).markDone();
				
				String taskName = internalMem.get(internalSeqNo).getTaskName();
				if (isMarked) {
					feedback.append(String.format(MESSAGE_MARK_DONE, taskName));
					feedback.append("\n");
				} else { //task is marked before this
					feedback.append(String.format(MESSAGE_PREVIOUSLY_MARK_DONE, taskName));
					feedback.append("\n");
				}
				History.addNewVersion();
	    		Storage.writeContentIntoFile();
			}
		}
		return feedback.toString().trim();
	}

	private boolean isOutOfBound(int taskListSize, int seqNo) {
		return (taskListSize < seqNo || seqNo <= 0);
	}
}
