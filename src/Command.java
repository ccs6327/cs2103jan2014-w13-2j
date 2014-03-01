
import java.util.ArrayList;
import java.util.Calendar;

public class Command {
	private static final int TYPE_TIMED_TASK = 0;
	private static final int TYPE_DEADLINE_TASK = 1;
	private static final int TYPE_FLOATING_TASK = 2;
	private static final int TYPE_UNSUPPORTED_TASK = 3;
	
	private String operation, taskName;
	private Calendar startTime, endTime;
	private ArrayList<Integer> taskSeqNo = new ArrayList<Integer>(); //for delete and mark function

	//constructor
	public Command() {}

	public Command(String _operation, String _taskName, Calendar _startTime
			, Calendar _endTime, ArrayList<Integer> _taskSeqNo) {
		operation = _operation;
		taskName = _taskName;
		startTime = _startTime;
		endTime = _endTime;
		taskSeqNo = _taskSeqNo;
	}


	//mutator
	public String getOperation() {
		return operation;
	}

	public String getTaskName() {
		return taskName;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}
	
	public int getTaskType(Calendar startTime, Calendar endTime) {
		if ((startTime != null) && (endTime != null)) {
			return TYPE_TIMED_TASK;
		}
		if ((startTime == null) && (endTime != null)) {
			return TYPE_DEADLINE_TASK;
		}
		if ((startTime == null) && (endTime == null)) {
			return TYPE_FLOATING_TASK;
		}
		return TYPE_UNSUPPORTED_TASK;
	}

	public ArrayList<Integer> getTaskSeqNo() {
		return taskSeqNo;
	}

	//accessor
	public void setOperation(String _operation) {
		operation = _operation;
	}

	public void setTaskName(String _taskName) {
		taskName = _taskName;
	}

	public void setStartTime(Calendar _startTime) {
		startTime = _startTime;
	}

	public void setEndTime(Calendar _endTime) {
		endTime = _endTime;
	}

	public void setTaskSeqNo(ArrayList<Integer> _taskSeqNo) {
		taskSeqNo = _taskSeqNo;
	}
}


