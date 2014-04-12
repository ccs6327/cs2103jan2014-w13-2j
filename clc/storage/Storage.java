package clc.storage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;
import clc.common.LogHelper;
import clc.logic.Task;
import static clc.common.Constants.*;

public class Storage {
	private static File dataFile;
	private static ArrayList<Task> internalMem = new ArrayList<Task>();
	private static ArrayList<Integer> displayMem = new ArrayList<Integer>();
	//@author A0105749Y
	/**
	 * Create the data file if it does not exist. 
	 * Otherwise read in the content from the data file.
	 */
	
	public static void initializeDataFile() {
		dataFile = new File(OUTFILE);
		if (!dataFile.exists()) {
			// Create the file if it does not exist
			try {
				dataFile.createNewFile();
				LogHelper.info(String.format(LOG_DATA_FILE_CREATED, dataFile.getPath()));
			} catch (IOException e) {
				LogHelper.severe(e.getMessage());
			}
		} else {
			try {
				readContentFromFile(OUTFILE);
			} catch (Exception e) {
				LogHelper.severe(e.getMessage());
			}
		}
		
		assert dataFile.exists();
	}
	
	//@author A0105712U
	/**
	 * Read from the data file and store them into the internal memory
	 * @param path The location of the file to be read
	 */
	
	public static void readContentFromFile(String path) {
		File fileIn = new File(path);

		try {

			BufferedReader bf = new BufferedReader(new FileReader(fileIn));
			
			String contentToRead = null;
			String taskName;
			String recurringPeriod;
			int taskType;
			int intervalToBeReminded = 0;
			int numberOfRecurring;
			Long taskId;
			Calendar startTime = null;
			Calendar endTime = null;
			boolean isDone;
			boolean isReminderNeeded;

			while((contentToRead = bf.readLine()) != null) {
				Task task;
				taskName = contentToRead;
				taskId = Long.parseLong(bf.readLine());
				taskType = Integer.parseInt(bf.readLine());
				//start time and end time
				switch (taskType) {
				case TYPE_TIMED_TASK:
					startTime = new GregorianCalendar();
					startTime.setTimeInMillis(Long.parseLong(bf.readLine()));
					endTime = new GregorianCalendar();
					endTime.setTimeInMillis(Long.parseLong(bf.readLine()));
					break;
				case TYPE_DEADLINE_TASK:
					startTime = null;
					endTime = new GregorianCalendar();
					endTime.setTimeInMillis(Long.parseLong(bf.readLine()));
					break;
				case TYPE_FLOATING_TASK:
					startTime = null;
					endTime = null;
					break;
				}
				// is done
				if (Integer.parseInt(bf.readLine()) == IS_DONE) {
					isDone = true;
				} else {
					isDone = false;
				}
				// is reminder needed
				if (Integer.parseInt(bf.readLine()) == IS_REMINDER_NEEDED) {
					isReminderNeeded = true;
				} else {
					isReminderNeeded = false;
				}
				intervalToBeReminded = Integer.parseInt(bf.readLine());
				numberOfRecurring = Integer.parseInt(bf.readLine());
				recurringPeriod = bf.readLine();

				task = new Task(taskName, taskId, taskType, startTime, endTime, isDone
								, isReminderNeeded, intervalToBeReminded, numberOfRecurring, recurringPeriod);
				if (hasNoIdenticalTask(task)) {
					internalMem.add(task);
				}
			}

			bf.close();
			LogHelper.info(String.format(LOG_DATA_FILE_READ, path));
		} catch (IOException e) {
			LogHelper.severe(e.getMessage());
		}

	}

	private static boolean hasNoIdenticalTask(Task task) {
		for (Task taskInInternalMem : internalMem) {
			if (taskInInternalMem.getTaskId() == task.getTaskId()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Write contents in the list into the data file
	 */
	public static void writeContentIntoFile() {
		try {

			Formatter formatter = new Formatter(OUTFILE);
			for (Task task : internalMem) {
				// task name
				formatter.format(task.getTaskName());
				formatter.format(NEW_LINE);
				// task id
				formatter.format(String.valueOf(task.getTaskId()));
				formatter.format(NEW_LINE);
				// task type
				formatter.format(String.valueOf(task.getTaskType()));
				formatter.format(NEW_LINE);
				// start time and end time
				switch (task.getTaskType()) {
				case TYPE_TIMED_TASK:
					formatter.format(String.valueOf(task.getStartTime().getTimeInMillis()));
					formatter.format(NEW_LINE);
					formatter.format(String.valueOf(task.getEndTime().getTimeInMillis()));
					formatter.format(NEW_LINE);
					break;
				case TYPE_DEADLINE_TASK:
					formatter.format(String.valueOf(task.getEndTime().getTimeInMillis()));
					formatter.format(NEW_LINE);
					break;
				case TYPE_FLOATING_TASK:
					break;
				}
				//is done
				if (task.getIsDone()) {
					formatter.format(String.valueOf(IS_DONE));
				} else {
					formatter.format(String.valueOf(IS_NOT_DONE));
				}
				formatter.format(NEW_LINE);
				// is reminder needed
				if (task.getIsReminderNeeded()) {
					formatter.format(String.valueOf(IS_REMINDER_NEEDED));
				} else {
					formatter.format(String.valueOf(IS_REMINDER_NOT_NEEDED));
				}
				formatter.format(NEW_LINE);
				// interval to be reminded
				formatter.format(String.valueOf(task.getReminderInterval()));
				formatter.format(NEW_LINE);
				// number of recurring
				formatter.format(String.valueOf(task.getNumberOfRecurring()));
				formatter.format(NEW_LINE);
				// recurring period
				formatter.format(String.valueOf(task.getRecurringPeriod()));
				formatter.format(NEW_LINE);
			}
			formatter.flush();
			formatter.close();
			LogHelper.info(String.format(LOG_DATA_FILE_WRITTEN, OUTFILE));
		} catch (Exception e) {
			LogHelper.severe(e.getMessage());
		}
	}

	//@author A0105749Y
	/**
	 * Read from the Help.txt and return to string
	 * @param fileName The file name of help manual file
	 * @return The contents of the help manual file
	 */
	public static String readManualFromHelpFile(String fileName) {
		InputStream in = Storage.class.getResourceAsStream(fileName);
		
		StringBuilder sb = new StringBuilder();
		try {

			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			String contentToRead = null;

			while((contentToRead = bf.readLine()) != null) {
				sb.append(contentToRead);
				sb.append(NEWLINE);
			}

			bf.close();
			LogHelper.info(String.format(LOG_DATA_FILE_READ, fileName));
		} catch (IOException e) {
			LogHelper.severe(e.getMessage());
		}
		return sb.toString();
	}
	
    //@author A0105712U
	public static ArrayList<Task> getInternalMem() {
		return internalMem;
	}

	public static ArrayList<Integer> getDisplayMem() {
		return displayMem;
	}

	public static void setInternalMem(ArrayList<Task> taskList) {
		internalMem.clear();
		internalMem.addAll(taskList);
	}

	public static void setDisplayMem(ArrayList<Integer> taskList) {
		displayMem.clear();
		displayMem.addAll(taskList);
	}
	
	/** 
	 * Export the database to the path which User entered
	 * @param path The desired path of exported files
	 * @return Feedback message
	 */
	public static String exportDataFile(String path) {
		try {
			copyFileToTarget(path);
		} catch (NullPointerException e) {
			LogHelper.warning(e.getMessage());
			return MESSAGE_EXPORT_FAILED;
		} catch (NoSuchFileException e) {
			LogHelper.warning(e.getMessage());
			return MESSAGE_EXPORT_FAILED_CANNOT_WRITE;
		} catch (InvalidPathException e) {
			LogHelper.warning(e.getMessage());
			return MESSAGE_EXPORT_FAILED_INVALID_PATH;
		} catch (IOException e) {
			LogHelper.warning(e.getMessage());
			return MESSAGE_EXPORT_FAILED;
		}
		return MESSAGE_EXPORT;
	}

	private static void copyFileToTarget(String path) throws IOException {
		File destination = new File(path + OUTFILE);
		File destinationDirectory = new File(path);
		Files.createDirectories(destinationDirectory.toPath());
		Files.copy(dataFile.toPath(), destination.toPath(), REPLACE_EXISTING);
		LogHelper.info(String.format(LOG_EXPORTED, destination.getPath()));
	}

	/**
	 * Import the database from the path which User entered
	 * @param path The desired path of imported files
	 * @return Feedback messages
	 */
	public static String importDataFile(String path) {
		File origin = new File(path + OUTFILE);
		try {
			Files.copy(origin.toPath(), dataFile.toPath(), REPLACE_EXISTING);
			History.addNewVersion();
		} catch (Exception e) {
			LogHelper.warning(e.getMessage());
			return MESSAGE_IMPORT_NO_ACCESS;
		} 
		internalMem.clear();
		readContentFromFile(OUTFILE);
		LogHelper.info(String.format(LOG_IMPORTED, path));
		return MESSAGE_IMPORT;
	}
}


