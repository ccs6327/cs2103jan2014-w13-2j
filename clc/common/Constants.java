package clc.common;

import static clc.common.Constants.currentVersion;
import static clc.common.Constants.historyMem;
import static clc.common.Constants.internalMem;

import java.util.ArrayList;
import java.text.SimpleDateFormat;

import clc.logic.Task;

public class Constants {
	public static final int TYPE_TIMED_TASK = 0;
	public static final int TYPE_DEADLINE_TASK = 1;
	public static final int TYPE_FLOATING_TASK = 2;
	public static final int TYPE_UNSUPPORTED_TASK = 3;
	
	public static final String TYPE_ADD = "add";
	public static final String TYPE_DISPLAY = "display";
	public static final String TYPE_DELETE = "delete";
	public static final String TYPE_CLEAR = "clear";
	public static final String TYPE_EXIT = "exit";
	public static final String TYPE_HELP = "help";
	public static final String TYPE_MARK = "mark";
	public static final String TYPE_UNDO = "undo";
	public static final String TYPE_REDO = "redo";
	public static final String TYPE_UNMARK = "unmark";
	public static final String TYPE_UPDATE = "update";
	
	public static final String EMPTY_STRING = "";
	public static final String NEW_LINE = "\r\n";
	
	public static final String MESSAGE_WELCOME = "Welcome to CLC.";
	public static final String MESSAGE_ENTER_COMMAND = "Command: ";
	public static final String MESSAGE_INVALID_FORMAT = "Invalid command format: %1$s";
	public static final String MESSAGE_TIMED_TASK_ADDED = "%1$s(%2$s - %3$s) is added.";
	public static final String MESSAGE_DEADLINE_TASK_ADDED = "%1$s(ending at %2$s) is added.";
	public static final String MESSAGE_FLOATING_TASK_ADDED = "%1$s is added.";
	public static final String MESSAGE_TASK_DELETED = "[%1$s] is deleted.";
	public static final String MESSAGE_OUT_OF_BOUND = "sequence number %d is out of bound";
	public static final String MESSAGE_MARK_DONE = "mark %1$s as done sucessfuly";
	public static final String MESSAGE_MARK_NOT_DONE = "mark %1$s as not done sucessfuly";
	public static final String MESSAGE_PREVIOUSLY_MARK_DONE = "%1$s is already marked as done";
	public static final String MESSAGE_PREVIOUSLY_MARK_NOT_DONE = "%1$s is originally not mark as done";
	public static final String MESSAGE_UNDONE = "Last command has been undone.";
	public static final String MESSAGE_UNDONE_FAILED = "No previous version is available.";
	public static final String MESSAGE_REDONE = "Last command has been redone.";
	public static final String MESSAGE_REDONE_FAILED = "No next version is available.";
	public static final String MESSAGE_CLEARED = "Task list is cleared. Tip: You may undo to recover.";

	//Display
	public static final String TO = " to ";
	public static final String MESSAGE_SHOW_ALL_TASKS = "All Tasks";
	public static final String MESSAGE_OUTPUT_TIMED_TASKS = "%1$d. %2$s, (starts at %3$s; ends at %4$s)"; 
	public static final String MESSAGE_OUTPUT_DEADLINE_TASKS = "%1$d. %2$s, (ends at %3$s)";
	public static final String MESSAGE_OUTPUT_FLOATING_TASKS = "%d. %s";
	public static final String MESSAGE_DISPLAY = "All tasks have been displayed.";
	public static final String MESSAGE_EMPTY_LIST = "Your list is empty.";
	public static final String MESSAEG_DISPLAY_IN_PERIOD = "This period's task(s) has(have) been displayed.";
	public static final String MESSAGE_NO_TASKS_IN_PERIOD =  "No task is found in this period";
	
	//Update
	public static final String MESSAGE_TASK_NAME_UPDATED_SUCCESS = "Task (%1$s) is updated to (%2$s) successful.";
	public static final String MESSAGE_TASK_STARTTIME_UPDATED_SUCCESS = "Task (%1$s) starttime is updated to (%2$s) successful.";
	public static final String MESSAGE_TASK_ENDTIME_UPDATED_SUCCESS = "Task (%1$s) endtime is updated to (%2$s) successful.";
	public static final String MESSAGE_NO_TASK_TO_UPDATE = "Your list is empty! There is nothing to update.";
	public static final String MESSAGE_INEXISTANCE_SEQNO = "You have input an inexistence sequence number.";
	public static final String MESSAGE_NO_CHANGE = "Nothing has been updated";
	
	//Filename
	public static final String OUTFILE = "data.txt";
	public static final String HELPFILE = "generalHelp.txt";
	
	// Storage
	public static final int IS_DONE = 1;
	public static final int IS_NOT_DONE = 0;
	
	public static ArrayList<Task> internalMem = new ArrayList<Task>();
	public static ArrayList<ArrayList<Task>> historyMem = new ArrayList<ArrayList<Task>>();
	public static ArrayList<String> DisplayOutput = new ArrayList<String>();
	public static ArrayList<Task> displayMem = new ArrayList<Task>();
	public static SimpleDateFormat D_M_Y_DateFormatter = new SimpleDateFormat("EEE, d MMM yyyy h.mm a");
	public static int currentVersion = -1;
	
	//Analyzer
	public static final String COMMA = ",";
	public static final String PM = "pm";
	public static final String AM = "am";
	public static final String SPACE = " ";
	public static final String SLASH = "/";
	public static final String DOT = ".";
	public static final String TODAY = "today";
	public static final String TOMORROW = "tomorrow";
	public static final String THIS_WEEK = "this week";
	public static final String NEXT_WEEK = "next week";
	public static final String THIS_MONTH = "this month";
	public static final String NEXT_MONTH = "next month";
	
	public static void addNewVersion() {
		currentVersion++;
		ArrayList<Task> tempMem = new ArrayList<Task>();
		tempMem.addAll(internalMem);
		historyMem.add(tempMem);
	}
}
