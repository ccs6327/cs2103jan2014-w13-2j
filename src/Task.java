import java.util.Calendar;

public class Task {
	private static final int TYPE_TIMED_TASK = 0;
	private static final int TYPE_DEADLINE_TASK = 1;
	private static final int TYPE_FLOATING_TASK = 2;
	private static final int TYPE_UNSUPPORTED_TASK = 3;
	
	String taskName;
	Calendar startTime;
	Calendar endTime;
	private int taskType; //0 timed task, 1 deadline task, 2 floating task, 3 unsupported task
	private boolean isDone = false;

	public Task() {
		
	}

	public Task(String _taskName, Calendar _startTime, Calendar _endTime) {
		taskName = _taskName;
		startTime = _startTime;
		endTime = _endTime;
	}

	//accessor
	public String getTaskName() {
		return taskName;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public Calendar getEndTime() {
		return endTime;
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


}



