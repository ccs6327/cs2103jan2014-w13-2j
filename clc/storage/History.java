package clc.storage;

import java.util.Stack;
import clc.logic.Task;
import clc.logic.Command;

public class History {
	//Variables to store the history data
	private static Stack<Command> operationHistory = new Stack<Command>();
	private static Stack<Task> taskHistory = new Stack<Task>();
	private static Stack<Task> updateHistory = new Stack<Task>(); //for update operation to store updated task
	
	private static void addOperationHistory(Command operation){
		operationHistory.push(operation);
	}
	
	private static void addTaskHistory(Task task){
		taskHistory.push(task);
	}
	
	public static void addUpdateHistory(Task updatedTask) {
		updateHistory.push(updatedTask);
	}
	
	public static Command popOperationHistory() {
		return operationHistory.pop();
	}
	
	public static Task popTasksHistory() {
		return taskHistory.pop();
	}
	
	public static Task popUpdateTasksHistory() {
		return updateHistory.pop();
	}
	
	public static boolean isEmptyHistory(){
		return operationHistory.isEmpty(); 
	}
	
	

}