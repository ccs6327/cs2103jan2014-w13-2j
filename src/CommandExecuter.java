import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class CommandExecuter {
	private static final String COMMAND_TYPE_ADD = "add";
	private static final String COMMAND_TYPE_DELETE = "delete";
	private static final String COMMAND_TYPE_DISPLAY = "display";
	private static final String COMMAND_TYPE_UNDO = "undo";
	private static final String COMMAND_TYPE_EXIT = "exit";
	private static final String COMMAND_TYPE_UPDATE = "update";
	private static final String COMMAND_TYPE_MARK = "mark";
	private static final String COMMAND_TYPE_HELP = "help";
	
	private static final String MESSAGE_ADDED = "Task \"%1$s\" is added";
	
	private static final int TYPE_TIMED_TASK = 0;
	private static final int TYPE_DEADLINE_TASK = 1;
	private static final int TYPE_FLOATING_TASK = 2;
	private static final int TYPE_UNSUPPORTED_TASK = 3;


	private Storage storage = new Storage();
	private String operation, taskName; //taskName use for operationName as well(help)
	private Calendar startTime, endTime;
	private int taskType;
	private ArrayList<Integer> taskSeqNo; //for delete and mark function

	//constructor
	public CommandExecuter() {}

	public CommandExecuter(Command command) {
		operation = command.getOperation();
		taskName = command.getTaskName();
		startTime = command.getStartTime();
		endTime = command.getEndTime();
		taskType = command.getTaskType(startTime, endTime);
		taskSeqNo = command.getTaskSeqNo();
	}

	//public method
	public void executeCommand() {
		if(operation.equals(COMMAND_TYPE_ADD)) {
			addTask();
		} else if(operation.equals(COMMAND_TYPE_DELETE)) {
			deleteTask();
		} else if(operation.equals(COMMAND_TYPE_DISPLAY)) {
			displayTask();
		} else if(operation.equals(COMMAND_TYPE_UPDATE)) {
			updateTask();
		} else if(operation.equals(COMMAND_TYPE_MARK)) {
			markTask();
		} else if(operation.equals(COMMAND_TYPE_UNDO)) {
			undoOperation();
		} else if(operation.equals(COMMAND_TYPE_HELP)) {
			help();
		} else if(operation.equals(COMMAND_TYPE_EXIT)) {
			System.exit(0);
		} else {
			//print invalid operation
		}
		storage.updateTasksList();
	}

	private void help() {
		// print out user manual
		String operationName = taskName;
		if (operationName.equals("")) {
			//general help
		} else {
			if (operationName.equals("COMMAND_TYPE_ADD")) {

			} else if (operationName.equals("COMMAND_TYPE_DELETE")) {

			} else if (operationName.equals("COMMAND_TYPE_DISPLAY")) {

			} else if (operationName.equals("COMMAND_TYPE_UPDATE")) {

			} else if (operationName.equals("COMMAND_TYPE_MARK")) {

			} else if (operationName.equals("COMMAND_TYPE_UNDO")) {

			}
		}
	}

	//private method
	private void addTask() {
		// add new task to the file
		// print confirmation message
		storage.write(taskName + "\r\n");
		storage.write(taskType + "\r\n");
		switch (taskType) {
			case TYPE_TIMED_TASK :
				storage.write(startTime.getTimeInMillis() + "\r\n");
				storage.write(endTime.getTimeInMillis() + "\r\n");
				break;
				
			case TYPE_DEADLINE_TASK :
				storage.write(endTime.getTimeInMillis() + "\r\n");
				break;
				
			case TYPE_FLOATING_TASK :
				break;
				
		}
		HistoryTask hTask 
		= new HistoryTask(taskName, startTime, endTime, operation);
		storage.getHistoryList().add(hTask);
		
		System.out.println(String.format(MESSAGE_ADDED, taskName));
	}

	private void deleteTask() {
		// delete single/multiple tasks

		Collections.sort(taskSeqNo);

		for (int i = taskSeqNo.size() - 1; i >= 0; i--) {
			ArrayList<Task> taskList = storage.getTaskList();
			int seqNo = taskSeqNo.get(i);

			if (isOutOfBound(taskList.size(), seqNo)) {
				//print error msg
				System.out.println("NO");
			} else {
				String taskName = taskList.get(seqNo - 1).getTaskName();
				Calendar startTime = taskList.get(seqNo - 1).getStartTime();
				Calendar endTime = taskList.get(seqNo - 1).getEndTime();

				HistoryTask hTask 
				= new HistoryTask(taskName, startTime, endTime, operation);
				storage.getHistoryList().add(hTask);

				taskList.remove(i);
				// print confirmation message
				System.out.println("YES");
			}
		}
	}

	private boolean isOutOfBound(int taskListSize, int seqNo) {
		return (taskListSize < seqNo || seqNo <= 0);
	}

	private void displayTask() {
		// display all task/ by deadline / by "today/tmr etc"
		// print all the requested task
	}

	private void updateTask() {
		// update task
		// print confirmation message
	}

	private void markTask() {
		// mark single/ multiple tasks
		// print confirmation message
	}

	private void undoOperation() {
		// undo previous operation
		// print confirmation message
		try {
			HistoryTask hTask = storage.getHistoryList().get(storage.getHistoryList().size() - 1);
			if (hTask.getOperation().equals(COMMAND_TYPE_ADD)) {
				deleteTask(hTask.getTaskName());
			} else if (hTask.getOperation().equals(COMMAND_TYPE_DELETE)) {
				addTask();
			}
		} catch (IndexOutOfBoundsException e) {
			System.err.println("No previous operation");
		}
		
	}

	private void deleteTask(String taskName) {
		// TODO Auto-generated method stub
		
	}
}



