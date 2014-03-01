import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;

public class Storage {
	private static final String TYPE_TIMED_TASK = "timed";
	private static final String TYPE_DEADLINE_TASK = "deadline";
	private static final String TYPE_FLOATING_TASK = "floating";
	
	private static ArrayList<Task> taskList = new ArrayList<Task>();
	private static ArrayList<HistoryTask> historyList = new ArrayList<HistoryTask>();

	private static String fileName;
	private static Formatter formatter;

	public Storage() {}

	public Storage(String _fileName) {
		fileName = _fileName;
		try {
			formatter = new Formatter(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void exitIfFileNotFound() {
		if(!doesFileExists()){
			//printFileNotFound();
			System.exit(0);
		}
	}

	private boolean doesFileExists() {
		File source = new File(fileName);
		return source.exists();
	}

	public void openFileAndRetrieveTaskList() {
		try {
			FileInputStream fis = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String line;
			// retrieve all the lines in the file and store in taskList
			while ((line = br.readLine()) != null) {
				String taskName; //store taskName that retrieve from the file
				String taskType;
				Calendar startTime = new GregorianCalendar(); //store startTime that retrieve from the file
				Calendar endTime = new GregorianCalendar(); //store endTime that retrieve from the file

				//.... code to retrieve
				taskName = line;
				switch (taskType = br.readLine()) {
					case TYPE_TIMED_TASK :
						startTime.setTimeInMillis(Long.parseLong(br.readLine()));
						endTime.setTimeInMillis(Long.parseLong(br.readLine()));
						break;
						
					case TYPE_DEADLINE_TASK :
						startTime = null;
						endTime.setTimeInMillis(Long.parseLong(br.readLine()));
						break;
						
					case TYPE_FLOATING_TASK :
						startTime = null;
						endTime = null;
						break;
						
					default :
						// unsupported type
						break;
				}
				taskList.add(new Task(taskName, startTime, endTime));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateTasksList() {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			for (int i = 0; i < taskList.size(); i++) {
				bw.write(taskList.get(i).getTaskName());
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}        	
	}
	
	public void write(String content) {
		formatter.format(content);
		formatter.flush();
	}

	//accessor
	public ArrayList<Task> getTaskList() {
		return taskList;
	}

	public ArrayList<HistoryTask> getHistoryList() {
		return historyList;
	}


	public String getFileName() {
		return fileName;
	}
}

