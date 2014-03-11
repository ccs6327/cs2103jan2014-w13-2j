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
	public void updateTaskName(String newTaskName) {
		taskName = newTaskName;
	}

	public void updateStartTime(Calendar newStartTime) {
		startTime = newStartTime;
	}

	public void updateEndTime(Calendar newEndTime) {
		endTime = newEndTime;
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
}



