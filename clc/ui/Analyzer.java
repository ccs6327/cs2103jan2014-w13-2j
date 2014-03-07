package clc.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import clc.logic.Task;

public class Analyzer {
	private static final String PM = "pm";
	private static final String AM = "am";
	private static final String SPACE = " ";
	private static final String SLASH = "/";
	private static final String DOT = ".";
	private String commandType, commandDetails;
	private Task task;
	private String taskName;
	private int firstDateIndex; 
	private ArrayList<Integer> monthInfo, dayInfo, timeInfo;
	private GregorianCalendar startTime = null, endTime = null;

	public Analyzer(String input) {
		monthInfo = new ArrayList<Integer>(); 
		dayInfo = new ArrayList<Integer>();
		timeInfo = new ArrayList<Integer>();
		commandType = getFirstWord(input);
		commandDetails = removeFirstWord(input);
	}

	public String getCommandType() {
		return commandType;
	}
	
	public Task getTask() {
		return task;
	}

	private String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	private String removeFirstWord(String userCommand) {
		return userCommand.replaceFirst(getFirstWord(userCommand), "").trim();
	}

	public Task analyzeAdd() {
		String[] splitDetails = commandDetails.split(SPACE);

		CountAndProcessCalendarInfoProvided(splitDetails);

		
		if (timeInfo.size() > 0) { //timed task or deadline task
			//merge the taskName
			taskName = mergeTaskName(splitDetails);
			
			if (isCaseDeadlineTask()) {
				return new Task(taskName, endTime);
			} else if (isCaseTimedTask()) {
				return new Task(taskName, startTime, endTime);
			} else {
				//handle exception ****
				System.out.println("invalid calendar format");
			}
		} else { // floating task
			taskName = commandDetails;
			return new Task(taskName);
		}
		
		return null;
	}

	private String mergeTaskName(String[] splitDetails) {
		String taskName = "";
		for (int i = 0; i < firstDateIndex; i++) {
			taskName += (splitDetails[i] + SPACE);
		}
		return taskName;
	}
	
	private void CountAndProcessCalendarInfoProvided (String[] splitDetails) {
		countCalendarInfo(splitDetails);
		if (timeInfo.size() > 0) { //have Calendar Info to be processed
			processCalendarInfo(splitDetails);
		}
	}

	private void countCalendarInfo(String[] splitDetails) {
		int endLoopIndex = splitDetails.length - 5; //loop 4 times from the back only
		if (endLoopIndex < 0) {
			endLoopIndex = 0;
		}
		for (int i = splitDetails.length - 1; i > endLoopIndex; i--) {
			
			String[] splitSymbol = null;
			String currWord = splitDetails[i];
			boolean isPm = false; //handle pm case in timeInfo
			
			if (doesContainSlash(currWord)) {
				splitSymbol = currWord.split(SLASH);
			} else if (doesContainDot(currWord)) {
				splitSymbol = currWord.split(DOT);
			} else if (doesContainAm(currWord)) {
				splitSymbol = currWord.split(AM);
			} else if (doesContainPm(currWord)) {
				splitSymbol = currWord.split(PM);
				isPm = true;
			}

			//contains either SLASH, DOT, AM or PM
			if (splitSymbol != null) {  
				if (isDateFormat(splitSymbol)) {
					int day = Integer.parseInt(splitSymbol[0]);
					int month = Integer.parseInt(splitSymbol[1]);
					dayInfo.add(day);
					monthInfo.add(month);
				} else if (isTimeFormat(splitSymbol)) {
					int time = Integer.parseInt(splitSymbol[0]);
					if (isPm) {
						if (time <= 12) {
							time += 12;
						} else if (time <= 1259) {
							time += 1200;
						}
					}
					timeInfo.add(time);
				}
				firstDateIndex = i;
			} 
		}
	}

	private void processCalendarInfo(String[] splitDetails) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int endMonth = Calendar.getInstance().get(Calendar.MONTH);
		int endDay = Calendar.getInstance().get(Calendar.DATE);
		int startMonth = 0, startDay = 0;
		int startHour = 0, startMin = 0, endHour = 0, endMin = 0;
		
		//process day and month
		if (dayInfo.size() >= 1) { //monthInfo == 0 as well
			endMonth = monthInfo.get(0) - 1;
			endDay = dayInfo.get(0);
			if (dayInfo.size() == 2) {
				startMonth = monthInfo.get(1) - 1;
				startDay = dayInfo.get(1);
			}
		}
		
		//process hour and minute
		if (timeInfo.size() >= 1) { 
			if (timeInfo.get(0) < 24) {
				endHour = timeInfo.get(0);
			} else {
				endHour = timeInfo.get(0) / 100;
				endMin = timeInfo.get(0) % 100;
			}
			if (timeInfo.size() == 2) {
				if (timeInfo.get(0) < 24) {
					startHour = timeInfo.get(1);
				} else {
					startHour = timeInfo.get(1) / 100;
					startMin = timeInfo.get(1) % 100;
				}
			}
		}
		
		//for case that user provide only one or no date
		if (startMonth == 0) {
			startMonth = endMonth;
			startDay = endDay;
		}
		
		startTime 
			= new GregorianCalendar(year, startMonth, startDay, startHour, startMin);
		endTime
			= new GregorianCalendar(year, endMonth, endDay, endHour, endMin);
	}

	private boolean isCaseDeadlineTask() {
		return (dayInfo.size() == 0 && timeInfo.size() == 1) 
				|| (dayInfo.size() == 1 && timeInfo.size() == 1);
	}
	
	private boolean isCaseTimedTask() {
		return (dayInfo.size() == 0 && timeInfo.size() == 2) 
				|| (dayInfo.size() == 1 && timeInfo.size() == 2) 
				|| (dayInfo.size() == 2 && timeInfo.size() == 2);
	}

	private boolean isDateFormat(String[] splitDate) {
		boolean isDate = false;
		//only support dd/mm/yy format or dd/mm format
		if (splitDate.length < 2 || splitDate.length > 3) {
			return false;
		}

		if (isNumeric(splitDate[0]) && isNumeric(splitDate[1])) {
			isDate = true;
		}
		return isDate;
	}

	private boolean isTimeFormat(String[] splitTime) {
		boolean isTime = false;
		//consider case for error time range input in future
		
		if (splitTime.length == 1 && isNumeric(splitTime[0])) {
			int time = Integer.parseInt(splitTime[0]);
			if (time >= 0 && time <= 1259) {
				isTime = true;
			}
		}
		return isTime;
	}

	private boolean doesContainPm(String currWord) {
		return currWord.contains(PM);
	}

	private boolean doesContainAm(String currWord) {
		return currWord.contains(AM);
	}

	private boolean doesContainDot(String currWord) {
		return currWord.contains(DOT);
	}

	private boolean doesContainSlash(String currWord) {
		return currWord.contains(SLASH);
	}

	public static boolean isNumeric(String str) {  
		try {  
			Integer.parseInt(str);  
		} catch(NumberFormatException nfe) {  
			return false;  
		}  
		return true;  
	}
}
