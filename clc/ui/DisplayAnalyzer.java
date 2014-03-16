package clc.ui;

import static clc.common.Constants.NEXT_MONTH;
import static clc.common.Constants.NEXT_WEEK;
import static clc.common.Constants.SPACE;
import static clc.common.Constants.THIS_MONTH;
import static clc.common.Constants.THIS_WEEK;
import static clc.common.Constants.TODAY;
import static clc.common.Constants.TOMORROW;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DisplayAnalyzer extends TimeAnalyzer{
	private static boolean isCaseDisplayCalendar;
	
	protected DisplayAnalyzer(String input) {
		super(input);
	}

	protected static void analyze() {
		infoToBeProcessed = commandDetails.split(SPACE);
		
		if (isCaseDisplayAll()){
			isCaseDisplayCalendar = false;
		} else if (isCaseDisplayDeadline()) {
			recordAndProcessCalendarInfoProvided();
			isCaseDisplayCalendar = true;
		} else if (isCaseDisplayToday()) {
			setToday();
			isCaseDisplayCalendar = true;
		} else if (isCaseDisplayTomorrow()) {
			setTomorrow();
			isCaseDisplayCalendar = true;
		} else if (isCaseDisplayThisWeek()) {
			setThisWeek();
			isCaseDisplayCalendar = true;
		} else if (isCaseDisplayNextWeek()) {
			setNextWeek();
			isCaseDisplayCalendar = true;
		} else if (isCaseDisplayThisMonth()) {
			setThisMonth();
			isCaseDisplayCalendar = true;
		} else if (isCaseDisplayNextMonth()) {
			setNextMonth();
			isCaseDisplayCalendar = true;
		} else {
			System.out.println("Invalid Format");
			// ***throw exception here
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
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int date = Calendar.getInstance().get(Calendar.DATE) + 1;
		startTime = new GregorianCalendar();
		endTime = new GregorianCalendar(year, month, date);
	}

	private static void setTomorrow() {
		// set the start time to tomorrow 0000, end time to end of the day
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int date = Calendar.getInstance().get(Calendar.DATE) + 1;
		startTime = new GregorianCalendar(year, month, date);
		endTime = new GregorianCalendar(year, month, ++date);
	}

	private static void setThisWeek() {
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

	private static void setNextWeek() {
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

	private static void setThisMonth() {
		// set the start time to now, end time to end of the month
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int date = 1; //start of the month
		startTime = new GregorianCalendar();
		endTime = new GregorianCalendar(year, month, date);
	}

	private static void setNextMonth() {
		// set the start time to beginning to next month, end time to end of next month
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int date = 1; //start of the month
		startTime = new GregorianCalendar(year, month, date);
		endTime = new GregorianCalendar(year, ++month, date);
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

	private static boolean isCaseDisplayAll() {
		return !doesCommandDetailsExist(commandDetails) 
				|| commandDetails.equals("all");
	}

	private static boolean isCaseDisplayDeadline() {
		return doesContainTimeInfo();
	}

	private static boolean isNextMonday(GregorianCalendar cal) {
		return cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
				&& endTime.get(Calendar.DATE) != startTime.get(Calendar.DATE);
	}
}
