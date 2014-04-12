package clc.common;

import java.util.ArrayList;
import java.text.SimpleDateFormat;

public class Constants {
	public static final int TYPE_TIMED_TASK = 0;
	public static final int TYPE_DEADLINE_TASK = 1; 
	public static final int TYPE_FLOATING_TASK = 2;
	public static final int TYPE_UNSUPPORTED_TASK = 3;
	public static final String TIMED_TASK_TO_STRING = "[%2$s - %3$s] %1$s";
	public static final String DEADLINE_TASK_TO_STRING = "[by %2$s] %1$s";
	public static final String FLOATING_TASK_TO_STRING = "%s";

	public static final String TYPE_ADD = "add";
	public static final String TYPE_CREATE = "create";
	public static final String TYPE_DISPLAY = "display";
	public static final String TYPE_DISPLAY_SHORT = "dis";
	public static final String TYPE_SHOW = "show";
	public static final String TYPE_LIST = "list";
	public static final String TYPE_DELETE = "delete";
	public static final String TYPE_DELETE_SHORT = "del";
	public static final String TYPE_REMOVE = "remove";
	public static final String TYPE_ERASE = "erase";
	public static final String TYPE_CLEAR = "clear";
	public static final String TYPE_EXIT = "exit";
	public static final String TYPE_HELP = "help";
	public static final String TYPE_MARK = "mark";
	public static final String TYPE_MARK_SHORT = "mrk";
	public static final String TYPE_UNMARK = "unmark";
	public static final String TYPE_UNMARK_SHORT = "umrk";
	public static final String TYPE_UNDO = "undo";
	public static final String TYPE_REDO = "redo";
	public static final String TYPE_UPDATE = "update";
	public static final String TYPE_UPDATE_SHORT = "up";
	public static final String TYPE_EDIT = "edit";
	public static final String TYPE_IMPORT = "import";
	public static final String TYPE_IMPORT_SHORT = "imp";
	public static final String TYPE_EXPORT = "export";
	public static final String TYPE_EXPORT_SHORT = "exp";
	public static final String TYPE_SEARCH = "search";

	public static final String EMPTY_STRING = "";
	public static final String NEW_LINE = "\r\n";

	public static final String MESSAGE_WELCOME = "Welcome to CLC.\nPress F1 for help";
	public static final String MESSAGE_ENTER_COMMAND = "Command: ";
	public static final String MESSAGE_INVALID_FORMAT = "Invalid command format: %1$s";
	public static final String MESSAGE_TIMED_TASK_ADDED = "%1$s(%2$s - %3$s) is added.";
	public static final String MESSAGE_DEADLINE_TASK_ADDED = "%1$s(ending at %2$s) is added.";
	public static final String MESSAGE_FLOATING_TASK_ADDED = "%1$s is added.";
	public static final String MESSAGE_TASK_DELETED = "[%1$s] is deleted.";
	public static final String MESSAGE_OUT_OF_BOUND = "sequence number %d is out of bound";
	public static final String MESSAGE_MARK_DONE = "mark [%1$s] as done successfully.";
	public static final String MESSAGE_MARK_NOT_DONE = "mark [%1$s] as not done sucessfully";
	public static final String MESSAGE_PREVIOUSLY_MARK_DONE = "[%1$s] is already marked as done";
	public static final String MESSAGE_PREVIOUSLY_MARK_NOT_DONE = "[%1$s] is originally not mark as done";
	public static final String MESSAGE_UNDONE = "Last command has been undone.";
	public static final String MESSAGE_UNDONE_FAILED = "No previous version is available.";
	public static final String MESSAGE_REDONE = "Last command has been redone.";
	public static final String MESSAGE_REDONE_FAILED = "No next version is available.";
	public static final String MESSAGE_CLEARED = "Task list is cleared. Tip: You may undo to recover.";

	//Display
	public static final String ANOTHER_LINE = "";
	public static final String MESSAGE_SHOW_ALL_TASKS = "ALL TASKS";
	public static final String MESSAGE_SHOW_INCOMPLETE_TASKS = "INCOMPLETE TASKS";
	public static final String MESSAGE_SHOW_FLOATING_TASKS = "FLOATING TASKS";
	public static final String MESSAGE_SHOW_DEADLINE_TASKS = "DEADLINE TASKS";
	public static final String MESSAGE_SHOW_TIMED_TASKS = "TIMED TASKS";
	public static final String MESSAGE_OUTPUT_TASKS = "%d. %s";
	public static final String MESSAGE_DISPLAY = "All tasks have been displayed.";
	public static final String MESSAGE_DISPLAY_INCOMPLETE_TASKS = "All incomplete tasks have been displayed.";
	public static final String MESSAGE_DISPLAY_INCOMPLETE_TASKS_PLURAL = "All incomplete tasks have been displayed.";
	public static final String MESSAGE_DISPLAY_INCOMPLETE_TASKS_SINGULAR = "The incomplete task has been displayed.";
	public static final String MESSAGE_NO_INCOMPLETE_TASKS =  "All tasks are done.";
	public static final String MESSAGE_DISPLAY_FLOATING_TASKS = "All floating tasks have been displayed.";
	public static final String MESSAGE_DISPLAY_FLOATING_TASKS_PLURAL = "All floating tasks have been displayed.";
	public static final String MESSAGE_DISPLAY_FLOATING_TASKS_SINGULAR = "The floating task has been displayed.";
	public static final String MESSAGE_NO_FLOATING_TASKS =  "No floating task is found.";
	public static final String MESSAGE_DISPLAY_DEADLINE_TASKS = "All deadline tasks have been displayed.";
	public static final String MESSAGE_DISPLAY_DEADLINE_TASKS_PLURAL = "All deadline tasks have been displayed.";
	public static final String MESSAGE_DISPLAY_DEADLINE_TASKS_SINGULAR = "The deadline task has been displayed.";
	public static final String MESSAGE_NO_DEADLINE_TASKS =  "No deadline task is found";
	public static final String MESSAGE_DISPLAY_TIMED_TASKS = "All timed tasks have been displayed.";
	public static final String MESSAGE_DISPLAY_TIMED_TASKS_PLURAL = "All timed tasks have been displayed.";
	public static final String MESSAGE_DISPLAY_TIMED_TASKS_SINGULAR = "The timed task has been displayed.";
	public static final String MESSAGE_NO_TIMED_TASKS =  "No timed task is found";
	public static final String MESSAGE_DISPLAY_QUERY = "[%1$s's Tasks]";
	public static final String MESSAGE_COMPLETE_TASKS = "===COMPLETE    Tasks===";
	public static final String MESSAGE_INCOMPLETE_TASKS = "===INCOMPLETE Tasks===";
	public static final String MESSAGE_DISPLAY_TASKS_BY_DEADLINE = "Tasks end before [%1$s]";
	public static final String MESSAGE_DISPLAY_TASKS_IN_PERIOD = "Tasks from [%1$s] to [%2$s]";
	public static final String MESSAGE_EMPTY_LIST = "Your task list is empty.";
	public static final String MESSAGE_DISPLAY_IN_PERIOD = "This period's task(s) has(have) been displayed.";
	public static final String MESSAGE_DISPLAY_IN_PERIOD_PLURAL = "This period's tasks have been displayed.";
	public static final String MESSAGE_DISPLAY_IN_PERIOD_SINGULAR = "This period's task has been displayed.";
	public static final String MESSAGE_NO_TASKS_IN_PERIOD = "No task is found in this period";
	public static final String TYPE_DISPLAY_INCOMPLETE = "";
	public static final String TYPE_DISPLAY_ALL = "all";
	public static final String TYPE_DISPLAY_FLOATING_TASK = "floating tasks";
	public static final String TYPE_DISPLAY_DEADLINE_TASK = "deadline tasks";
	public static final String TYPE_DISPLAY_TIMED_TASK = "timed tasks";
	
	// Search
	public final static String BLANK_STRING = "";
	public final static String MESSAGE_RESULTS_FOUND = "Found: ";
    public final static String MESSAGE_MATCHING_ENTRIES = "All entries containing: '%s'";
    public final static String MESSAGE_SEARCH_FEEDBACK = "Task(s) containing '%s' has(have) been displayed.";
    public final static String MESSAGE_SEARCH_FEEDBACK_PLURAL = "Tasks containing '%s' have been displayed.";
    public final static String MESSAGE_SEARCH_FEEDBACK_SINGULAR = "The task containing '%s' has been displayed.";
    public final static String ERROR_TASK_NOT_FOUND = "Cannot find any task about '%s'.";
	
	//Update
	public static final String MESSAGE_TASK_NAME_UPDATED_SUCCESS = "Task name [%1$s] is updated to [%2$s] successfully.";
	public static final String MESSAGE_TASK_STARTTIME_UPDATED_SUCCESS = "Task No.%3$d [%1$s] start time is updated to [%2$s] successfully.";
	public static final String MESSAGE_TASK_ENDTIME_UPDATED_SUCCESS = "Task No.%3$d [%1$s] end  time is updated to [%2$s] successfully.";
	public static final String MESSAGE_TASK_TYPE_CHANGED = "No.%1$d [%3$s] [%2$s] TASK-TYPE changes to [%4$s]";
	public static final String MESSAGE_NO_TASK_TO_UPDATE = "Your list is empty! There is nothing to update.";
	public static final String MESSAGE_INEXISTANCE_SEQNO = "You have input an inexistence sequence number.";
	public static final String MESSAGE_ERROR_UPDATE_FLOATING = "Floating task cannot only update start time.";
	public static final String MESSAGE_NO_CHANGE = "Nothing has been updated";
	public static final String STRING_FLOATING_TASK = "TYPE FLOATING";
	public static final String STRING_DEADLINE_TASK = "TYPE DEADLINE";
	public static final String STRING_TIMED_TASK = "TYPE TIMED";
	public static final String STRING_UNSUPPORTED_TASK = "TYPE UNSUPPORTED";
    public static final String MESSAGE_UPDUATE_SUCCESSFUL = "Updated Sucessful!";
    public static final String MESSAGE_UPDATE_TIME_ERROR = "UPDATE ERROR: The task's end  time [%2$s] \r\n"
    		+ "                                                  is before/equal \r\n"
    		+ "                                             start time [%1$s].\r\n\r\n=*=*=*=UPDATE UNSUCCESSFUL!=*=*=*=";
    		
    //Help
	public static final String DATE = "date";
	public static final String HOTKEY = "hotkey";
    
	//Remind
	public static final int DEFAULT_REMINDER_INTERVAL = 15; //minutes
	public static final String MESSAGE_REMIND_TIMED_TASKS = "%1$s, (starts at %2$s; ends at %3$s)"; 
	public static final String MESSAGE_REMIND_DEADLINE_TASKS = "%1$s, (ends at %2$s)";

	// Export/Import
	public static final String BACKSLASH = "\\";
	public static final String MESSAGE_EXPORT = "Your calendar has been exported to %1$s";
	public static final String MESSAGE_EXPORT_FAILED = "Failed to export to %1$s. Please check the destination path.";
	public static final String MESSAGE_EXPORT_FAILED_CANNOT_WRITE = "Failed to export to %1$s since CLC cannot access the destination."
			+ "\r\nPlease grant the access permission. You may have to provide administrator permission to do so.";
	public static final String MESSAGE_EXPORT_FAILED_INVALID_PATH = "%1$s is not a valid path. Please revise the path.";
	public static final String MESSAGE_IMPORT = "Your calendar has been imported from %1$s";
	public static final String MESSAGE_IMPORT_NO_ACCESS = "Failed to import from %1$s. Please check the origin path.";
	public static final String MESSAGE_IMPORT_FILE_CORRUPTED = "The data file is corrupted.";
	
	//Filename
	public static final String OUTFILE = "metadata.clc";
	public static final String CLC_EXPORT_DIRECTORY = "CLC Exported Files\\";
	public static final String SEARCH_HELP_FILE = "searchHelp.clc";
	public static final String IMPORT_EXPORT_HELP_FILE = "importExportHelp.clc";
	public static final String UPDATE_HELP_FILE = "updateHelp.clc";
	public static final String MARK_UNMARK_HELP_FILE = "markUnmarkHelp.clc";
	public static final String DISPLAY_HELP_FILE = "displayHelp.clc";
	public static final String DELETE_HELP_FILE = "deleteHelp.clc";
	public static final String ADD_HELP_FILE = "addHelp.clc";
	public static final String GENERAL_HELP_FILE = "generalHelp.clc";
	public static final String DATE_HELP_FILE = "dateHelp.clc";
	public static final String HOTKEY_HELP_FILE = "hotkeyHelp.clc";
	public static final String LOG_FILE = "clc.log";

	// Storage
	public static final int IS_DONE = 1;
	public static final int IS_NOT_DONE = 0;
	public static final int IS_REMINDER_NEEDED = 1;
	public static final int IS_REMINDER_NOT_NEEDED = 0;


	public static ArrayList<String> DisplayOutput = new ArrayList<String>();
	public static SimpleDateFormat D_M_Y_DateFormatter = new SimpleDateFormat("EEE, d MMM yyyy h.mm a");
	public static SimpleDateFormat D_M_Y_TimeFormatter = new SimpleDateFormat("h.mm a");
	
	//Analyzer
	public static final String EMPTY = "";
	public static final String SLASH_R = "/r";
	public static final String COMMA = ",";
	public static final String PM = "pm";
	public static final String AM = "am";
	public static final String SPACE = " ";
	public static final String SLASH = "/";
	public static final String DOT = ".";
	public static final String COLON = ":";
	public static final String DASH = "-";
	public static final String QUOTATION_MARK = "'";
	public static final String ALL = "all";
	public static final String INCOMPLETE_TASK = "";
	public static final String FLOATING_TASK = "floating tasks";
	public static final String DEADLINE_TASK = "deadline tasks";
	public static final String TIMED_TASK = "timed tasks";
	public static final String THIS_WEEK = "This Week";
	public static final String NEXT_WEEK = "Next Week";
	public static final String THIS_MONTH = "This Month";
	public static final String NEXT_MONTH = "Next Month";

	//TimeParser
	public static final String TODAY = "Today";
	public static final String TODAY_SHORT = "tdy";
	public static final String TOMORROW = "Tomorrow";
	public static final String TOMORROW_SHORT = "tmr";
	public static final String MONDAY = "Monday";
	public static final String MONDAY_SHORT = "mon";
	public static final String TUESDAY = "Tuesday";
	public static final String TUESDAY_SHORT = "tue";
	public static final String WEDNESDAY = "Wednesday";
	public static final String WEDNESDAY_SHORT = "wed";
	public static final String THURSDAY = "Thursday";
	public static final String THURSDAY_SHORT = "thu";
	public static final String FRIDAY = "Friday";
	public static final String FRIDAY_SHORT = "fri";
	public static final String SATURDAY = "Saturday";
	public static final String SATURDAY_SHORT = "sat";
	public static final String SUNDAY = "Sunday";
	public static final String SUNDAY_SHORT = "sun";
	public static final String[] DATE_PATTERNS = {"ddMMMyyyy", "ddMMM", "dd/MM/yyyy", "dd/MM", "dd-MM-yyyy", "dd-MM", "dd.MM.yyyy", "dd.MM"};
	public static final String[] TIME_12H_PATTERNS = {"h:mma", "h.mma", "hha", "hmma", "hhmma"};
	public static final String[] TIME_24H_PATTERNS = {"HH:mm"};
	public static final String FROM = "from";
	public static final String BY = "by";
	public static final String DUE = "due";
	public static final String AT = "at";
	public static final String TO = "to";
	public static final String NEXT = "Next";
	public static final String EVERY = "every";
	public static final String EVERY_WEEK = "every week";
	public static final String EVERYDAY = "everyday";
	
	//GUI
	public static final String MESSAGE_SEPARATOR = "=========================================================================\n";
	public static final String NEWLINE = "\n";

	//InvalidInputException messages
	public static final String ERROR_NO_TASK_NAME = "ERROR: No task name is given";
	public static final String ERROR_REMINDER_FOR_FLOATING_TASK = "ERROR: No reminder is allowed for floating task";
	public static final String ERROR_EMPTY_COMMAND_DETAILS = "ERROR: Empty command details";
	public static final String ERROR_START_TIME = "ERROR: Start time is a calendar before now";
	public static final String ERROR_END_TIME = "ERROR: End time is a calendar before now";
	public static final String ERROR_START_TIME_LATER_THAN_OR_EQUAL_TO_END_TIME = "ERROR: Start time is later than or equal to end time";
	public static final String ERROR_NO_EXACT_TIME = "ERROR: No exact time is given";
	public static final String ERROR_INVALID_DISPLAY_REQUEST = "ERROR: Invalid display request";
	public static final String ERROR_NO_NEW_TASK_NAME = "ERROR: No new task name is given";
	public static final String ERROR_NO_SEQUENCE_NUMBER = "ERROR: Please indicate the sequence number";
	public static final String ERROR_CONTAIN_NON_NUMERIC_INFO = "ERROR: Command details contain non-numeric info";
	public static final String ERROR_ONLY_ALLOW_NEXT = "ERROR: Display query can be extended with 'next' only";
	public static final String ERROR_DAILY_RECURRING_ONE_DATE_ONLY = "ERROR: Daily recurring event can contain one date only";
	public static final String ERROR_DISPLAY_WITH_RECCURING_TIME = "ERROR: Cannot display with reccuring time";
	public static final String ERROR_CANNOT_UPDATE_TO_RECURRING_TASK = "ERROR: Cannot update to recurring task";
	
	
}
