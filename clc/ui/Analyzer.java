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
	private static final String TODAY = "today";
	private static final String TOMORROW = "tomorrow";
	private static final String THIS_WEEK = "this week";
	private static final String NEXT_WEEK = "next week";
	private static final String THIS_MONTH = "this month";
	private static final String NEXT_MONTH = "next month";
	/*
	private static final String MONDAY = "monday";
	private static final String TUESDAY = "tuesday";
	private static final String WEDNESDAY = "wednesday";
	private static final String THURSDAY = "thursday";
	private static final String FRIDAY = "friday";
	private static final String SATURDAY = "saturday";
	private static final String SUNDAY = "sunday";*/

	private String commandType, commandDetails, taskName;
	private Task task;
	private int firstDateIndex; 
	private ArrayList<Integer> monthInfo, dayInfo, timeInfo;
	private GregorianCalendar startTime = null, endTime = null;
	private boolean isPm = false;
	private String[] splitDate = null, splitTime = null;

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

		recordAndProcessCalendarInfoProvided(splitDetails);

		if(startTime != null)System.out.println(startTime.getTime().toString());
		if(endTime != null)System.out.println(endTime.getTime().toString());

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

	public ArrayList<GregorianCalendar> analyzeDisplay() {
		ArrayList<GregorianCalendar> time = new ArrayList<GregorianCalendar>(); 

		String[] splitDetails = commandDetails.split(SPACE);

		if (doesCommandDetailsExist()){
			if (isCaseDisplayDeadline(splitDetails)) {
				recordAndProcessCalendarInfoProvided (splitDetails);			
			} else { // case displayPeriod e.g. display today
				if (isCaseDisplayToday()) {
					setToday();
				} else if (isCaseDisplayTomorrow()) {
					setTomorrow();
				} else if (isCaseDisplayThisWeek()) {
					setThisWeek();
				} else if (isCaseDisplayNextWeek()) {
					setNextWeek();
				} else if (isCaseDisplayThisMonth()) {
					setThisMonth();
				} else if (isCaseDisplayNextMonth()) {
					setNextMonth();
				} else {
					System.out.println("Invalid Format");
				}
			}
		}
		time.add(startTime);
		time.add(endTime);


		return time; 
	}

	private boolean doesCommandDetailsExist() {
		return !commandDetails.equals("");
	}

	private void setToday() {
		// set the start time to now, end time to end of today
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int date = Calendar.getInstance().get(Calendar.DATE) + 1;
		startTime = new GregorianCalendar();
		endTime = new GregorianCalendar(year, month, date);
	}

	private void setTomorrow() {
		// set the start time to tomorrow 0000, end time to end of the day
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int date = Calendar.getInstance().get(Calendar.DATE) + 1;
		startTime = new GregorianCalendar(year, month, date);
		endTime = new GregorianCalendar(year, month, ++date);
	}

	private void setThisWeek() {
		// set the start time to now, end time to end of the week
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int date = Calendar.getInstance().get(Calendar.DATE);
		startTime = new GregorianCalendar();
		endTime = new GregorianCalendar(year, month , date);
		while (!isNextMonday(endTime)) { //end of the week = beginning of next week
			endTime.add(Calendar.DAY_OF_WEEK, + 1);
		}
	}

	private void setNextWeek() {
		// set the start time to next Monday, end time to end of next week
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int date = Calendar.getInstance().get(Calendar.DATE);
		startTime = new GregorianCalendar(year, month, date);
		endTime = new GregorianCalendar(year, month , date);
		while (!isNextMonday(startTime)) { //end of the week = beginning of next week
			startTime.add(Calendar.DAY_OF_WEEK, + 1);
		}
		while (!isNextMonday(endTime)) { //end of the week = beginning of next week
			endTime.add(Calendar.DAY_OF_WEEK, + 1);
		}
	}

	private void setThisMonth() {
		// set the start time to now, end time to end of the month
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int date = 1; //start of the month
		startTime = new GregorianCalendar();
		endTime = new GregorianCalendar(year, month, date);
	}

	private void setNextMonth() {
		// set the start time to beginning to next month, end time to end of next month
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int date = 1; //start of the month
		startTime = new GregorianCalendar(year, month, date);
		endTime = new GregorianCalendar(year, ++month, date);
	}

	private boolean isCaseDisplayDeadline(String[] splitDetails) {
		return isTimeFormat(splitDetails[0])||isDateFormat(splitDetails[0]);
	}

	private String mergeTaskName(String[] splitDetails) {
		String taskName = "";
		for (int i = 0; i < firstDateIndex; i++) {
			taskName += (splitDetails[i] + SPACE);
		}
		return taskName;
	}

	private void recordAndProcessCalendarInfoProvided (String[] splitDetails) {
		recordCalendarInfo(splitDetails);
		if (dayInfo.size() > 0 || timeInfo.size() > 0) { //have Calendar Info to be processed
			processCalendarInfo(splitDetails);
		}
	}

	private void recordCalendarInfo(String[] splitDetails) {
		int endLoopIndex = splitDetails.length - 4; //loop 4 times from the back only
		if (endLoopIndex < 0) {
			endLoopIndex = 0;
		}
		for (int i = splitDetails.length - 1; i >= endLoopIndex; i--) {

			String currWord = splitDetails[i];

			//contains either SLASH, DOT, AM or PM
			if (isDateFormat(currWord)) {
				int day = Integer.parseInt(splitDate[0]);
				int month = Integer.parseInt(splitDate[1]);
				dayInfo.add(day);
				monthInfo.add(month);
			} else if (isTimeFormat(currWord)) {
				int time = Integer.parseInt(splitTime[0]);
				if (isPm) {
					if (time != 12) {
						if (time < 12) {
							time += 12;
						} else if (time <= 1159) {
							time += 1200;
						}
					}
				} else {
					if (time == 12) {
						time -= 12;
					} else if (time >= 1200) {
						time -= 1200;
					}
				}
				timeInfo.add(time);
			}
			firstDateIndex = i;

		}
	}

	private void processCalendarInfo(String[] splitDetails) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int endMonth = Calendar.getInstance().get(Calendar.MONTH);
		int endDate = Calendar.getInstance().get(Calendar.DATE);
		int startMonth = 0, startDate = 0;
		int startHour = 0, startMin = 0, endHour = 0, endMin = 0;

		//process day and month
		if (dayInfo.size() >= 1) { //monthInfo == 0 as well
			endMonth = monthInfo.get(0) - 1;
			endDate = dayInfo.get(0);
			if (dayInfo.size() == 2) {
				startMonth = monthInfo.get(1) - 1;
				startDate = dayInfo.get(1);
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
			startDate = endDate;
		}

		if(timeInfo.size() == 2) {
			startTime = new GregorianCalendar(year, startMonth, startDate, startHour, startMin);
		}
		endTime = new GregorianCalendar(year, endMonth, endDate, endHour, endMin);
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

	private boolean isCaseDisplayToday() {
		return commandDetails.equals(TODAY);
	}

	private boolean isCaseDisplayTomorrow() {
		return commandDetails.equals(TOMORROW);
	}

	private boolean isCaseDisplayThisWeek() {
		return commandDetails.equals(THIS_WEEK);
	}

	private boolean isCaseDisplayNextWeek() {
		return commandDetails.equals(NEXT_WEEK);
	}

	private boolean isCaseDisplayThisMonth() {
		return commandDetails.equals(THIS_MONTH);
	}

	private boolean isCaseDisplayNextMonth() {
		return commandDetails.equals(NEXT_MONTH);
	}

	private boolean isDateFormat(String currWord) {
		boolean isDate = false;

		if (doesContainSlash(currWord)) {
			splitDate = currWord.split(SLASH);
		} else if (doesContainDot(currWord)) {
			splitDate = currWord.split(DOT);
		} else {
			return false;
		}

		//only support dd/mm/yy format or dd/mm format
		if (splitDate.length < 2 || splitDate.length > 3) {
			return false;
		}

		if (isNumeric(splitDate[0]) && isNumeric(splitDate[1])) {
			isDate = true;
		}
		return isDate;
	}

	private boolean isTimeFormat(String currWord) {
		boolean isTime = false;

		if (doesContainAm(currWord)) {
			splitTime = currWord.split(AM);
		} else if (doesContainPm(currWord)) {
			splitTime = currWord.split(PM);
			isPm = true;
		} else {
			return false;
		}

		//consider case for error time range input in future

		if (splitTime.length == 1 && isNumeric(splitTime[0])) {
			int time = Integer.parseInt(splitTime[0]);
			if (time >= 0 && time <= 1259) {
				isTime = true;
			}
		}
		return isTime;
	}

	private boolean isNextMonday(GregorianCalendar cal) {
		return cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
				&& endTime.get(Calendar.DATE) != startTime.get(Calendar.DATE);
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
