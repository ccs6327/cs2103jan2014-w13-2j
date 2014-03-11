package clc.common;

import static clc.common.Constants.currentVersion;
import static clc.common.Constants.historyMem;
import static clc.common.Constants.internalMem;

import java.util.ArrayList;

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
	
	public static final String MESSAGE_WELCOME = "Welcome to CLC.";
	public static final String MESSAGE_ENTER_COMMAND = "Command: ";
	public static final String MESSAGE_INVALID_FORMAT = "Invalid command format: %1$s";
	public static final String MESSAGE_TIMED_TASK_ADDED = "%1$s(%2$s - %3$s) is added.";
	public static final String MESSAGE_DEADLINE_TASK_ADDED = "%1$s(ending at %2$s) is added.";
	public static final String MESSAGE_FLOATING_TASK_ADDED = "%1$s is added.";
	public static final String MESSAGE_TASK_DELETED = "�1$s�is deleted.";
	public static final String MESSAGE_OUT_OF_BOUND = "%d is out of bound";
	public static final String MESSAGE_MARK_DONE = "mark �1$s�as done sucessfuly";
	public static final String MESSAGE_MARK_NOT_DONE = "mark �1$s�as not done sucessfuly";
	public static final String MESSAGE_PREVIOUSLY_MARK_DONE = "�1$s�is already marked as done";
	public static final String MESSAGE_PREVIOUSLY_MARK_NOT_DONE = "�1$s�is originally not mark as done";
	public static final String MESSAGE_UNDONE = "Last command has been undone.";
	public static final String MESSAGE_UNDONE_FAILED = "No previous version is available.";
	public static final String MESSAGE_REDONE = "Last command has been redone.";
	public static final String MESSAGE_REDONE_FAILED = "No next version is available.";
	
	public static final String OUTFILE = "data.txt";
	public static final String HELPFILE = "generalHelp.txt";
	
	public static ArrayList<Task> internalMem = new ArrayList<Task>();
	public static ArrayList<ArrayList<Task>> historyMem = new ArrayList<ArrayList<Task>>();
	public static int currentVersion = -1;
	
	public static void addNewVersion() {
		currentVersion++;
		historyMem.add(internalMem);
	}
}
