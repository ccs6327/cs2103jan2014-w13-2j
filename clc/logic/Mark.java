package clc.logic;

import java.util.ArrayList;
import java.util.Calendar;

import clc.storage.History;
import clc.storage.Storage;

import static clc.common.Constants.MESSAGE_OUT_OF_BOUND;
import static clc.common.Constants.MESSAGE_MARK_DONE;
import static clc.common.Constants.MESSAGE_PREVIOUSLY_MARK_DONE;
import static clc.common.Constants.EVERYDAY;
import static clc.common.Constants.EVERY_WEEK;

public class Mark implements Command {
	private ArrayList<Integer> taskSeqNo;
	private StringBuilder feedback = new StringBuilder();
	private ArrayList<Integer> displayMem;
	private ArrayList<Task> internalMem;
	private ArrayList<Task> reminderMem;
	private boolean isReminderTask;
	private long targetTaskId;
	
	public Mark(ArrayList<Integer> taskSeqNo) {
		this.taskSeqNo = taskSeqNo;
		displayMem = Storage.getDisplayMem();
		internalMem = Storage.getInternalMem();
	}
	
	//constructor for marking reminder task
	public Mark(long taskId) {
		isReminderTask = true;
		reminderMem = Remind.getReminderMem();
		targetTaskId = taskId;
	}

	@Override
	public String execute() {
		if (isReminderTask) {
			return markReminderMem();
		}
		return markDisplayMem();
	}

	private String markReminderMem() {
		for (int i = reminderMem.size() - 1; i >= 0; i --) {
			Task task = reminderMem.get(i);
			if (task.getTaskId() == targetTaskId) {
				task.markDone();
	    		Storage.writeContentIntoFile();
				break;
			}
		}
		return "";
	}

	private String markDisplayMem() {
		boolean isChanged = false;
		
		for (int i = 0; i < taskSeqNo.size(); i++) {
			int seqNo = taskSeqNo.get(i);
			
			if (isOutOfBound(displayMem.size(), seqNo)) {
				feedback.append(String.format(MESSAGE_OUT_OF_BOUND, seqNo));
				feedback.append("\n");
			} else {
				boolean isMarked = false;
				int internalSeqNo = displayMem.get(seqNo - 1); 
				
				Task toBeMarkedTask = internalMem.get(internalSeqNo);
				
				caseMarkRecurringTask(toBeMarkedTask);
				
				isMarked = toBeMarkedTask.markDone();
				isChanged = true;
				String taskName = internalMem.get(internalSeqNo).getTaskName();
				if (isMarked) {
					isChanged = true;
					feedback.append(String.format(MESSAGE_MARK_DONE, taskName));
					feedback.append("\n");
				} else { //task is marked before this
					feedback.append(String.format(MESSAGE_PREVIOUSLY_MARK_DONE, taskName));
					feedback.append("\n");
				}
			}
		}
		
		if (isChanged) {
			History.addNewVersion();
    		Storage.writeContentIntoFile();
		}
		
		return feedback.toString().trim();
	}

	private void caseMarkRecurringTask(Task toBeMarkedTask) {
		int nRecurring = toBeMarkedTask.getNumberOfRecurring();
		if (nRecurring > 0) {
			Task newRecurringTask = new Task(toBeMarkedTask);
			//pass the recurring time to the new task
			toBeMarkedTask.setNumberOfRecurring(0);
			newRecurringTask.setNumberOfRecurring(--nRecurring);
			if (newRecurringTask.getRecurringPeriod().equals(EVERY_WEEK)) {
				newRecurringTask.postponeStartAndEndTime(Calendar.WEEK_OF_YEAR);
			} else if (newRecurringTask.getRecurringPeriod().equals(EVERYDAY)) {
				newRecurringTask.postponeStartAndEndTime(Calendar.DAY_OF_YEAR);
			}
			Command addRecurringTask = new Add(newRecurringTask);
			addRecurringTask.execute();
		}
	}

	private boolean isOutOfBound(int taskListSize, int seqNo) {
		return (taskListSize < seqNo || seqNo <= 0);
	}
}
