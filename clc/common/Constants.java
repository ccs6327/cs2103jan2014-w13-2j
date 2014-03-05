package clc.common;

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
	
	public static final String EMPTY_STRING = "";
	
	public static final String MESSAGE_WELCOME = "Welcome to CLC.";
	public static final String MESSAGE_ENTER_COMMAND = "Command: ";
	public static final String MESSAGE_INVALID_FORMAT = "Invalid command format: %1$s\r\n";
	
	public static final String OUTFILE = "data.txt";
	public static final String HELP_FILE = "generalHelp.txt";
	
	public static ArrayList<Task> internalMem = new ArrayList<Task>();
}
