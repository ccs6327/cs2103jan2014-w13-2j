package clc.logic;
import java.util.Calendar;
import java.util.Comparator;

import static clc.common.Constants.*;

public class Task {
	
	
	private String taskName;
    private String taskToString;
	private String taskTypeToString;
	private long taskId;
	private Calendar startTime;
	private Calendar endTime;
	private int taskType;//0 timed task, 1 deadline task, 2 floating task, 3 unsupported task
	private boolean isDone;
	private boolean isReminderNeeded; 
	private int numberOfRecurring;
	private String recurringPeriod;
	private int intervalToBeReminded;

	public Task() {
		
	}
	
	public Task(Task task) {
		taskName = task.getTaskName();
		taskTypeToString = task.taskTypeToString();
		taskId = Calendar.getInstance().getTimeInMillis();
		if (task.getStartTime() != null) {
			startTime = (Calendar) task.getStartTime().clone();
		} else {
			startTime = null;
		}
		if (task.getEndTime() != null) {
			endTime = (Calendar) task.getEndTime().clone();
		} else {
			endTime = null;
		}
		taskType = task.getTaskType();
		isDone = false;
		isReminderNeeded = task.getIsReminderNeeded();
		numberOfRecurring = task.getNumberOfRecurring();
		recurringPeriod = task.getRecurringPeriod();
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
			Calendar _startTime, Calendar _endTime, boolean _isDone, 
			boolean _isReminderNeeded, int _numberOfRecurring, int _intervalToBeReminded, String _recurringPeriod) {
		taskName = _taskName;
		taskId = _taskId;
		taskType = _taskType;
		startTime = _startTime;
		endTime = _endTime;
		isDone = _isDone;
		isReminderNeeded = _isReminderNeeded;
		intervalToBeReminded = _intervalToBeReminded;
		numberOfRecurring = _numberOfRecurring;
		recurringPeriod = _recurringPeriod;
	}

	//accessor
	public String getTaskName() {
		return taskName;
	}
	
	public long getTaskId() {
		return taskId;
	}
	
	public int getTaskType() {
		return taskType;
	}
	
	public Calendar getStartTime() {
		return startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public boolean getIsDone() {
		return isDone;
	}

	public boolean getIsReminderNeeded() {
		return isReminderNeeded;
	}
	
	public int getReminderInterval() {
		return intervalToBeReminded;
	}
	
	public int getNumberOfRecurring() {
		return numberOfRecurring;
	}

	public String getRecurringPeriod() {
		return recurringPeriod;
	}
	
	public String toString(){
		
	    if(taskType == TYPE_TIMED_TASK){
    		String startTimeString = D_M_Y_DateFormatter.format(startTime.getTime());
    		String endTimeString = D_M_Y_DateFormatter.format(endTime.getTime());
	    	taskToString = String.format(TIMED_TASK_TO_STRING, taskName, startTimeString, endTimeString);
	    }else if (taskType == TYPE_DEADLINE_TASK) {
    		String endTimeString = D_M_Y_DateFormatter.format(endTime.getTime());
	    	taskToString = String.format(DEADLINE_TASK_TO_STRING, taskName, endTimeString);
	    }else {
	    	taskToString = String.format(FLOATING_TASK_TO_STRING, taskName);
	    }
		return taskToString;
	}
	
	public String toStringTimeFormatter(){
	    if(taskType == TYPE_TIMED_TASK){
    		String startTimeString = D_M_Y_TimeFormatter.format(startTime.getTime());
    		String endTimeString = D_M_Y_TimeFormatter.format(endTime.getTime());
	    	taskToString = String.format(TIMED_TASK_TO_STRING, taskName, startTimeString, endTimeString);
	    }else if (taskType == TYPE_DEADLINE_TASK) {
    		String endTimeString = D_M_Y_TimeFormatter.format(endTime.getTime());
	    	taskToString = String.format(DEADLINE_TASK_TO_STRING, taskName, endTimeString);
	    }else {
	    	taskToString = String.format(FLOATING_TASK_TO_STRING, taskName);
	    }
		return taskToString;
	}

	public String taskTypeToString(){
	    if(taskType == TYPE_TIMED_TASK){
	    	taskTypeToString = STRING_TIMED_TASK;
	    }else if (taskType == TYPE_DEADLINE_TASK) {
	    	taskTypeToString = STRING_DEADLINE_TASK;
	    }else if (taskType == TYPE_FLOATING_TASK){
	    	taskTypeToString = STRING_FLOATING_TASK;
	    }else {
	    	taskTypeToString = STRING_UNSUPPORTED_TASK;
	    }
		return taskTypeToString;
	}
	
	//mutator 
	public void setTaskName(String newTaskName) {
		taskName = newTaskName;
	}
	
	public long setTaskId() {
		return System.currentTimeMillis();
	}
	
	public void setTaskType(int _taskType){
		taskType = _taskType;
	}
	
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}
	
	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}
	
	public void setIsDone(boolean isDone) {
		this.isDone = isDone;
	}

	public void setReminder(int _intervalToBeReminded) {
		isReminderNeeded = true;
		intervalToBeReminded = _intervalToBeReminded;
	}
	
	public void setNumberOfRecurring(int nRecurring) {
		numberOfRecurring = nRecurring;
	}

	public void setRecurringPeriod(String recurringPeriod) {
		this.recurringPeriod = recurringPeriod;
	}
	
	// Check whether the task has both start time and end time
	public boolean isTimedTask() {
		return getStartTime() != null && getEndTime() != null;
	}
	// Check whether the task has only the end date
	public boolean isDeadlineTask() {
		return getStartTime() == null && getEndTime() != null;
	}
	// Check whether the event has neither start date nor end date
	public boolean isFloatingTask() {
		return getStartTime() == null && getEndTime() == null;
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

	public void postponeStartAndEndTime(int typeOfCalendar) {
		if (startTime != null) {
			startTime.add(typeOfCalendar, 1);
			endTime.add(typeOfCalendar, 1);
		} else if (endTime != null) {
			endTime.add(typeOfCalendar, 1);
		}
	}

	public Task getNewCopy() {
		Task task = new Task();
		
		task.setTaskName(taskName);
	    task.setTaskToString(taskToString);
		task.setTaskTypeToString(taskTypeToString);
		task.setTaskId(taskId);
		task.setStartTime(startTime);
		task.setEndTime(endTime);
		task.setTaskType(taskType);
		task.setIsDone(isDone);
		task.setIsDone(isReminderNeeded); 
		task.setNumberOfRecurring(numberOfRecurring);
		task.setRecurringPeriod(recurringPeriod);
		
		return task;
	}

	private void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	private void setTaskTypeToString(String taskTypeToString) {
		this.taskTypeToString = taskTypeToString;
		
	}

	private void setTaskToString(String taskToString) {
		this.taskToString = taskToString;
	}
}

class TaskComparator implements Comparator<Task> {
	@Override
	public int compare(Task task1, Task task2) {
		if(task1.isFloatingTask()){
			if(task2.isFloatingTask()) {
				return task1.getTaskName().compareTo(task2.getTaskName());
			}
			else {
				return -1;
			}
		}
		else {
			if(task2.isFloatingTask()) {
				return 1;
			}
			else if(!task1.getEndTime().equals(task2.getEndTime())) {
				if(task1.getEndTime().before(task2.getEndTime())) {
					return -1;
				}
				else {
					return 1;
				}
			}
			else {
				return task1.getTaskName().compareTo(task2.getTaskName());
			}
		}
	}
}


