package clc.storage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;

import clc.logic.Task;
import static clc.common.Constants.*;

public class Storage {
	
	private static File dataFile;
	private static ArrayList<Task> internalMem = new ArrayList<Task>();
	private static ArrayList<Task> displayMem = new ArrayList<Task>();
	
	/* If the data file does not exist, create the data file. 
	 *  Otherwise read in the content from the data file.
	 */
	public static void initializeDataFile() {
		dataFile = new File(OUTFILE);
		if (!dataFile.exists()) {
			// Create the file if it does not exist
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		} else {
			try {
				readContentFromFile();
			} catch (Exception e) {
					
			}
		}
	}
	
	// Read from the data file and store them into the internal memory
	public static void readContentFromFile() {
		File fileIn = new File(OUTFILE);

		try {
			
			BufferedReader bf = new BufferedReader(new FileReader(fileIn));
			String contentToRead = null;
			String taskName;
			Long taskId;
			int taskType;
			Calendar startTime = null;
			Calendar endTime = null;
			boolean isDone;
			
			while((contentToRead = bf.readLine()) != null) {
				taskName = contentToRead;
				taskId = Long.parseLong(bf.readLine());
				taskType = Integer.parseInt(bf.readLine());
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
				if (Integer.parseInt(bf.readLine()) == IS_DONE) {
					isDone = true;
				} else {
					isDone = false;
				}
				internalMem.add(new Task(taskName, taskId, taskType, startTime, endTime, isDone));
			}
			
			bf.close();
		} catch (IOException e) {
			
		}
	         
	}

	// Write contents in the list into the data file
	public static void writeContentIntoFile() {
		try {
			
			Formatter formatter = new Formatter(OUTFILE);
	        for (Task task : internalMem) {
	       	 	formatter.format(task.getTaskName());
	       	 	formatter.format(NEW_LINE);
	       	 	formatter.format(String.valueOf(task.getTaskId()));
	       	 	formatter.format(NEW_LINE);
	       	 	formatter.format(String.valueOf(task.getTaskType()));
	       	 	formatter.format(NEW_LINE);
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
	       	 	if (task.isDone()) {
	       	 		formatter.format(String.valueOf(IS_DONE));
	       	 	} else {
	       	 		formatter.format(String.valueOf(IS_NOT_DONE));
	       	 	}
	       	 	formatter.format(NEW_LINE);
	        }
	        formatter.flush();
	        formatter.close();
		} catch (Exception e) {
			
		}
	}
	
	// Read from the Help.txt and return to string
	public static String readManualFromHelpFile() {
		File fileIn = new File(HELPFILE);
		StringBuilder sb = new StringBuilder();
		try {
			
			BufferedReader bf = new BufferedReader(new FileReader(fileIn));
			String contentToRead = null;
			
			while((contentToRead = bf.readLine()) != null) {
				sb.append(contentToRead);
				sb.append("\n");
			}
			
			bf.close();
		} catch (IOException e) {
			
		}
		return sb.toString();
	}
	
	public static ArrayList<Task> getInternalMem() {
		return internalMem;
	}

	public static ArrayList<Task> getDisplayMem() {
		return displayMem;
	}
}


