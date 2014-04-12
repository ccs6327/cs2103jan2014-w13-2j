//@author A0105712U
package clc.storage;

import java.util.ArrayList;
import java.util.Stack;

import clc.logic.Task;
import clc.logic.Command;

public class History {
	//Variables to store the history data
	private static Stack<Command> operationHistory = new Stack<Command>();
	private static Stack<Task> taskHistory = new Stack<Task>();
	private static Stack<Task> updateHistory = new Stack<Task>(); //for update operation to store updated task
	private static ArrayList<ArrayList<Task>> historyMem = new ArrayList<ArrayList<Task>>();
	private static ArrayList<Integer> displayMem = new ArrayList<Integer>();
	private static int currentVersion = -1;
	
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
	
	public static void addNewVersion() {
		currentVersion++;
		ArrayList<Task> tempMem = new ArrayList<Task>();
		// tempMem.addAll(Storage.getInternalMem());
		for (Task task : Storage.getInternalMem()) {
			tempMem.add(task.getNewCopy());
		}
		for (int i = historyMem.size() - 1; i >= currentVersion ; i--) {
			historyMem.remove(i);
		}
		historyMem.add(tempMem);
	}
	
	public static ArrayList<ArrayList<Task>> getHistoryMem() {
		return historyMem;
	}

	public static int getCurrentVersion() {
		return currentVersion;
	}
	
	public static int increaseCurrentVersion() {
		currentVersion += 1;
		return currentVersion;
	}
	
	public static int decreaseCurrentVersion() {
		currentVersion -= 1;
		return currentVersion;
	}

	public static ArrayList<Integer> getDisplayMem() {
		return displayMem;
	}

	public static void setDisplayMem() {
		displayMem.clear();
		displayMem.addAll(Storage.getDisplayMem());
	}
}
