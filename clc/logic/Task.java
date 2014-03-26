package clc.logic;
import java.util.Calendar;

import static clc.common.Constants.*;

public class Task {
	
	
	private String taskName;
	private long taskId;
	private Calendar startTime;
	private Calendar endTime;
	private int taskType; //0 timed task, 1 deadline task, 2 floating task, 3 unsupported task
	private boolean isDone;
	private boolean isReminderNeeded;

	public Task() {
		
	}

	public Task(String _taskName, Calendar _startTime, Calendar _endTime) {
		taskName = _taskName;
		taskId = setTaskId();
		startTime = _startTime;
		endTime = _endTime;
		taskType = TYPE_TIMED_TASK;
		isDone = false;
		setTaskId();
	}
	
	public Task(String _taskName, Calendar _endTime) {
		taskName = _taskName;
		taskId = setTaskId();
		startTime = null;
		endTime = _endTime;
		taskType = TYPE_DEADLINE_TASK;
		isDone = false;
		setTaskId();
	}
	
	public Task(String _taskName) {
		taskName = _taskName;
		taskId = setTaskId();
		startTime = null;
		endTime = null;
		taskType = TYPE_FLOATING_TASK;
		isDone = false;
		setTaskId();
	}

	public Task(String _taskName, Long _taskId, int _taskType,
			Calendar _startTime, Calendar _endTime, boolean _isDone) {
		taskName = _taskName;
		taskId = _taskId;
		taskType = _taskType;
		startTime = _startTime;
		endTime = _endTime;
		isDone = _isDone;
	}

	//accessor
	public String getTaskName() {
		return taskName;
	}
	
	public long getTaskId() {
		return taskId;
	}
	
	public Calendar getStartTime() {
		return startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}
	
	public int getTaskType() {
		return taskType;
	}

	//mutator for update function
	public void setTaskType(int _taskType){
		taskType = _taskType;
	}
	
	public void updateTaskName(String newTaskName) {
		taskName = newTaskName;
	}

	public void updateStartTime(Calendar newStartTime) {
		startTime = newStartTime;
	}

	public void updateEndTime(Calendar newEndTime) {
		endTime = newEndTime;
	}
	
	public void updateTaskType(Calendar newStartTime, Calendar newEndTime) {
		if (newStartTime != null && newEndTime != null) {
			taskType = TYPE_TIMED_TASK;
	    } else if (newStartTime == null && newEndTime != null) {
	    	taskType = TYPE_DEADLINE_TASK;
	    } else if(newStartTime == null && newEndTime == null) {
	    	taskType = TYPE_FLOATING_TASK;
	    } else {
	    	taskType = TYPE_UNSUPPORTED_TASK;
	    }
	}

	//public method
	public boolean markDone() {
		if (!isDone) {
			isDone = true;
			return true;
		}
		return false;
	}

	public boolean markUndone() {
		if (isDone) {
			isDone = false;
			return true;
		}
		return false;
	}
	
	private long setTaskId() {
		return System.currentTimeMillis();
	}

	public boolean isDone() {
		return isDone;
	}

	public void setReminder() {
		isReminderNeeded = true;
	}

	public boolean isReminderNeeded() {
		return isReminderNeeded;
	}
}



