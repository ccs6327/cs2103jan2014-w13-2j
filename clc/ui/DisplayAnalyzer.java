package clc.ui;

import static clc.common.Constants.ERROR_INVALID_DISPLAY_REQUEST;
import static clc.common.Constants.SPACE;
import static clc.common.Constants.TODAY;
import static clc.common.Constants.TOMORROW;
import static clc.common.Constants.THIS_WEEK;
import static clc.common.Constants.THIS_MONTH;
import static clc.common.Constants.NEXT_WEEK;
import static clc.common.Constants.NEXT_MONTH;
import static clc.common.Constants.ALL;
import static clc.common.Constants.TIMED_TASK;
import static clc.common.Constants.DEADLINE_TASK;
import static clc.common.Constants.FLOATING_TASK;

import java.util.Calendar;
import java.util.GregorianCalendar;

import clc.common.InvalidInputException;

public class DisplayAnalyzer extends TimeAnalyzer{
	private static boolean isCaseDisplayCalendar;
	private static int year, month, date;
	
	protected DisplayAnalyzer(String input) {
		super(input);
	}

	protected static void analyze() throws InvalidInputException{
		infoToBeProcessed = commandDetails.split(SPACE);
		isCaseDisplayCalendar = true;
		
		year = Calendar.getInstance().get(Calendar.YEAR);
		month = Calendar.getInstance().get(Calendar.MONTH);
		date = Calendar.getInstance().get(Calendar.DATE);
		
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
		} else if (isCaseDisplayDeadline()) {
			recordAndProcessCalendarInfoProvided();
		} else if(isCaseDisplayString()){
			isCaseDisplayCalendar = false;
		} else {
			throw new InvalidInputException(ERROR_INVALID_DISPLAY_REQUEST);
		}
	}
	
	protected static boolean getDisplayCase() {
		return isCaseDisplayCalendar;
	}

	protected static String getDisplayQuery() {
		return commandDetails;
	}

	private static void setToday() {
		// set the start time to now, end time to end of today
		startTime = new GregorianCalendar();
		endTime = new GregorianCalendar(year, month, ++date);
	}

	private static void setTomorrow() {
		// set the start time to tomorrow 0000, end time to end of the day
		startTime = new GregorianCalendar(year, month, ++date);
		endTime = new GregorianCalendar(year, month, ++date);
	}

	private static void setThisWeek() {
		// set the start time to now, end time to end of the week
		startTime = new GregorianCalendar();
		endTime = new GregorianCalendar(year, month , date);
		while (!isNextMonday(endTime)) { //end of the week = beginning of next week
			endTime.add(Calendar.DAY_OF_WEEK, + 1);
		}
	}

	private static void setNextWeek() {
		// set the start time to next Monday, end time to end of next week
		startTime = new GregorianCalendar(year, month, date);
		endTime = new GregorianCalendar(year, month , date);
		while (!isNextMonday(startTime)) { //end of the week = beginning of next week
			startTime.add(Calendar.DAY_OF_WEEK, + 1);
		}
		while (!isNextMonday(endTime)) { //end of the week = beginning of next week
			endTime.add(Calendar.DAY_OF_WEEK, + 1);
		}
	}

	private static void setThisMonth() {
		// set the start time to now, end time to end of the month
		startTime = new GregorianCalendar();
		endTime = new GregorianCalendar(year, ++month, 1);
	}

	private static void setNextMonth() {
		// set the start time to beginning to next month, end time to end of next month
		startTime = new GregorianCalendar(year, ++month, 1);
		endTime = new GregorianCalendar(year, ++month, 1);
	}

	private static boolean isCaseDisplayToday() {
		return commandDetails.equals(TODAY);
	}

	private static boolean isCaseDisplayTomorrow() {
		return commandDetails.equals(TOMORROW);
	}

	private static boolean isCaseDisplayThisWeek() {
		return commandDetails.equals(THIS_WEEK);
	}

	private static boolean isCaseDisplayNextWeek() {
		return commandDetails.equals(NEXT_WEEK);
	}

	private static boolean isCaseDisplayThisMonth() {
		return commandDetails.equals(THIS_MONTH);
	}

	private static boolean isCaseDisplayNextMonth() {
		return commandDetails.equals(NEXT_MONTH);
	}

    //change for display string
	private static boolean isCaseDisplayString() {
		if(!doesCommandDetailsExist(commandDetails)){
			commandDetails = ALL;
		}
		
		return commandDetails.equals(ALL) ||
			   commandDetails.equals(FLOATING_TASK) ||
			   commandDetails.equals(DEADLINE_TASK) ||
			   commandDetails.equals(TIMED_TASK);
	}
	
	private static boolean isCaseDisplayDeadline() {
		return doesContainTimeInfo();
	}

	private static boolean isNextMonday(GregorianCalendar cal) {
		return cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
				&& endTime.get(Calendar.DATE) != startTime.get(Calendar.DATE);
	}
}
