package clc.common;

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
	public static final String TYPE_UNMARK = "unmark";
	public static final String TYPE_UPDATE = "update";
	
	public static final String EMPTY_STRING = "";
	
	public static final String MESSAGE_WELCOME = "Welcome to CLC.";
	public static final String MESSAGE_ENTER_COMMAND = "Command: ";
	public static final String MESSAGE_INVALID_FORMAT = "Invalid command format: %1$s";
	public static final String MESSAGE_TIMED_TASK_ADDED = "%1$s(%2$s - %3$s) is added.";
	public static final String MESSAGE_DEADLINE_TASK_ADDED = "%1$s(ending at %2$s) is added.";
	public static final String MESSAGE_FLOATING_TASK_ADDED = "%1$s is added.";
	public static final String MESSAGE_TASK_DELETED = "�%1$s� is deleted.";
	public static final String MESSAGE_OUT_OF_BOUND = "%d is out of bound";
	public static final String MESSAGE_MARK_DONE = "mark �%1$s� as done sucessfuly";
	public static final String MESSAGE_MARK_NOT_DONE = "mark �%1$s� as not done sucessfuly";
	public static final String MESSAGE_PREVIOUSLY_MARK_DONE = "�%1$s� is already marked as done";
	public static final String MESSAGE_PREVIOUSLY_MARK_NOT_DONE = "�%1$s� is originally not mark as done";
	
	//Display
	public final static String TO = "to";
	public static final String MESSAGE_SHOW_ALL_TASKS = "All Tasks";
	public static final String MESSAGE_OUTPUT_TIMED_TASKS = "%1$d. %2$s, starts at %3$s; ends at %4$s"; 
	public static final String MESSAGE_OUTPUT_DEADLINE_TASKS = "%1$d. %2$s, ends at %3$s";
	public static final String MESSAGE_OUTPUT_FLOATING_TASKS = "%d. %s";
	public static final String MESSAGE_DISPLAY = "All tasks have been displayed.";
	public static final String MESSAGE_EMPTY_LIST = "Your list is empty";
	public final static String MESSAEG_DISPLAY_IN_PERIOD = "This period's task(s) has(have) been displayed.";
	public final static String MESSAGE_NO_TASKS_IN_PERIOD =  "No task is found in this period";
	
	public static final String OUTFILE = "data.txt";
	public static final String HELPFILE = "generalHelp.txt";
	
	public static ArrayList<Task> internalMem = new ArrayList<Task>();
	public static ArrayList<String> DisplayOutput = new ArrayList<String>();
	public static ArrayList<Task> displayMem = new ArrayList<Task>();
	public static SimpleDateFormat D_M_Y_DateFormatter = new SimpleDateFormat("EEE, d MMM yyyy h.mm a");
}
