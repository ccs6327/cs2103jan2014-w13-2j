import java.util.Calendar;

// store in temporary memory in order to undo
// String operation store the previous operation did to this task
public class HistoryTask extends Task {
	String operation;

	public HistoryTask(String _taskName, Calendar _startTime, Calendar _endTime, String _operation) {
		taskName = _taskName;
		startTime = _startTime;
		endTime = _endTime;
		operation = _operation;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String _operation) {
		operation = _operation;
	}
}



