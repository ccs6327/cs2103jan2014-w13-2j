package clc.ui;

import static clc.common.Constants.ERROR_INVALID_DISPLAY_REQUEST;
import static clc.common.Constants.SPACE;
import static clc.common.Constants.TODAY;
import static clc.common.Constants.TODAY_SHORT;
import static clc.common.Constants.TOMORROW;
import static clc.common.Constants.TOMORROW_SHORT;
import static clc.common.Constants.MONDAY;
import static clc.common.Constants.MONDAY_SHORT;
import static clc.common.Constants.TUESDAY;
import static clc.common.Constants.TUESDAY_SHORT;
import static clc.common.Constants.WEDNESDAY;
import static clc.common.Constants.WEDNESDAY_SHORT;
import static clc.common.Constants.THURSDAY;
import static clc.common.Constants.THURSDAY_SHORT;
import static clc.common.Constants.FRIDAY;
import static clc.common.Constants.FRIDAY_SHORT;
import static clc.common.Constants.SATURDAY;
import static clc.common.Constants.SATURDAY_SHORT;
import static clc.common.Constants.SUNDAY;
import static clc.common.Constants.SUNDAY_SHORT;
import static clc.common.Constants.THIS_WEEK;
import static clc.common.Constants.THIS_MONTH;
import static clc.common.Constants.NEXT_WEEK;
import static clc.common.Constants.NEXT_MONTH;
import static clc.common.Constants.ALL;
import static clc.common.Constants.INCOMPLETE_TASK;
import static clc.common.Constants.TIMED_TASK;
import static clc.common.Constants.DEADLINE_TASK;
import static clc.common.Constants.FLOATING_TASK;

import java.util.Calendar;
import java.util.GregorianCalendar;

import clc.common.InvalidInputException;

public class DisplayAnalyzer extends TimeParser{
	private static boolean isCaseDisplayCalendar; //display Calendar with date format
	private static boolean isCaseKeywordCalendar; //display Calendar with keyword
	private static int year, month, date, dayOfWeek;

	protected DisplayAnalyzer(String input) {
		super(input);
	}

	protected static void analyze() throws InvalidInputException{
		infoToBeProcessed = commandDetails.split(SPACE);
		isCaseDisplayCalendar = true;
		isCaseKeywordCalendar = true;

		year = Calendar.getInstance().get(Calendar.YEAR);
		month = Calendar.getInstance().get(Calendar.MONTH);
		date = Calendar.getInstance().get(Calendar.DATE);
		dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

		if (isCaseDisplayToday()) {
			setToday();
		} else if (isCaseDisplayTomorrow()) {
			setTomorrow();
		} else if (isCaseDisplayMonday()) {
			setMonday();
		} else if (isCaseDisplayTuesday()) {
			setTuesday();
		} else if (isCaseDisplayWednesday()) {
			setWednesday();
		} else if (isCaseDisplayThursday()) {
			setThursday();
		} else if (isCaseDisplayFriday()) {
			setFriday();
		} else if (isCaseDisplaySaturday()) {
			setSaturday();
		} else if (isCaseDisplaySunday()) {
			setSunday();
		} else if (isCaseDisplayThisWeek()) {
			setThisWeek();
		} else if (isCaseDisplayNextWeek()) {
			setNextWeek();
		} else if (isCaseDisplayThisMonth()) {
			setThisMonth();
		} else if (isCaseDisplayNextMonth()) {
			setNextMonth();
		} else if (isCaseDisplayCalendar()) {
			isCaseKeywordCalendar = false;
		} else if(isCaseDisplayString()){
			isCaseDisplayCalendar = false;
			isCaseKeywordCalendar = false;
		} else {
			throw new InvalidInputException(ERROR_INVALID_DISPLAY_REQUEST);
		}
	}

	protected static boolean getDisplayCase() {
		return isCaseDisplayCalendar;
	}

	protected static boolean getDisplayCalendarCase() {
		return isCaseKeywordCalendar;
	}

	protected static String getDisplayQuery() {
		return commandDetails;
	}

	private static void setToday() {
		commandDetails = TODAY;
		// set the start time as now, end time as the end of today
		startCalendar = new GregorianCalendar();
		endCalendar = new GregorianCalendar(year, month, ++date);
	}

	private static void setTomorrow() {
		commandDetails = TOMORROW;
		// set the start time as tomorrow 0000, end time as the end of the day
		startCalendar = new GregorianCalendar(year, month, ++date);
		endCalendar = new GregorianCalendar(year, month, ++date);
	}

	private static void setMonday() {
		commandDetails = MONDAY;
		//set start time as the beginning of coming Monday, end time as the end of the day
		int addValue = (Calendar.MONDAY - dayOfWeek + 7) % 7;
		startCalendar = new GregorianCalendar(year, month, date + addValue);
		endCalendar = new GregorianCalendar(year, month, date + addValue + 1);
	}

	private static void setTuesday() {
		commandDetails = TUESDAY;
		//set start time as the beginning of coming Tuesday, end time as the end of the day
		int addValue = (Calendar.TUESDAY - dayOfWeek + 7) % 7;
		startCalendar = new GregorianCalendar(year, month, date + addValue);
		endCalendar = new GregorianCalendar(year, month, date + addValue + 1);
	}

	private static void setWednesday() {
		commandDetails = WEDNESDAY;
		//set start time as the beginning of coming Wednesday, end time as the end of the day
		int addValue = (Calendar.WEDNESDAY - dayOfWeek + 7) % 7;
		startCalendar = new GregorianCalendar(year, month, date + addValue);
		endCalendar = new GregorianCalendar(year, month, date + addValue + 1);
	}

	private static void setThursday() {
		commandDetails = THURSDAY;
		//set start time as the beginning of coming Thursday, end time as the end of the day
		int addValue = (Calendar.THURSDAY - dayOfWeek + 7) % 7;
		startCalendar = new GregorianCalendar(year, month, date + addValue);
		endCalendar = new GregorianCalendar(year, month, date + addValue + 1);
	}

	private static void setFriday() {
		commandDetails = FRIDAY;
		//set start time as the beginning of coming Friday, end time as the end of the day
		int addValue = (Calendar.FRIDAY - dayOfWeek + 7) % 7;
		startCalendar = new GregorianCalendar(year, month, date + addValue);
		endCalendar = new GregorianCalendar(year, month, date + addValue + 1);
	}

	private static void setSaturday() {
		commandDetails = SATURDAY;
		//set start time as the beginning of coming Saturday, end time as the end of the day
		int addValue = (Calendar.SATURDAY - dayOfWeek + 7) % 7;
		startCalendar = new GregorianCalendar(year, month, date + addValue);
		endCalendar = new GregorianCalendar(year, month, date + addValue + 1);
	}

	private static void setSunday() {
		commandDetails = SUNDAY;
		//set start time as the beginning of coming Sunday, end time as the end of the day
		int addValue = (Calendar.SUNDAY - dayOfWeek + 7) % 7;
		startCalendar = new GregorianCalendar(year, month, date + addValue);
		endCalendar = new GregorianCalendar(year, month, date + addValue + 1);
	}

	private static void setThisWeek() {
		commandDetails = THIS_WEEK;
		// set the start time as now, end time as the end of the week
		startCalendar = new GregorianCalendar();
		endCalendar = new GregorianCalendar(year, month , date);
		while (!isNextMonday(endCalendar)) { //end of the week = beginning of next week
			endCalendar.add(Calendar.DAY_OF_WEEK, + 1);
		}
	}

	private static void setNextWeek() {
		commandDetails = NEXT_WEEK;
		// set the start time as next Monday, end time as the end of next week
		startCalendar = new GregorianCalendar(year, month, date);
		endCalendar = new GregorianCalendar(year, month , date);
		while (!isNextMonday(startCalendar)) { //end of the week = beginning of next week
			startCalendar.add(Calendar.DAY_OF_WEEK, + 1);
		}
		while (!isNextMonday(endCalendar)) { //end of the week = beginning of next week
			endCalendar.add(Calendar.DAY_OF_WEEK, + 1);
		}
	}

	private static void setThisMonth() {
		commandDetails = THIS_MONTH;
		// set the start time as now, end time as the end of the month
		startCalendar = new GregorianCalendar();
		endCalendar = new GregorianCalendar(year, ++month, 1);
	}

	private static void setNextMonth() {
		commandDetails = NEXT_MONTH;
		// set the start time as the beginning of next month, end time as the end of next month
		startCalendar = new GregorianCalendar(year, ++month, 1);
		endCalendar = new GregorianCalendar(year, ++month, 1);
		
	}
	
	private static boolean isCaseDisplayToday() {
		return commandDetails.equalsIgnoreCase(TODAY)
				|| commandDetails.equalsIgnoreCase(TODAY_SHORT);
	}

	private static boolean isCaseDisplayTomorrow() {
		return commandDetails.equalsIgnoreCase(TOMORROW)
				|| commandDetails.equalsIgnoreCase(TOMORROW_SHORT);
	}
	
	private static boolean isCaseDisplayMonday() {
		return commandDetails.equalsIgnoreCase(MONDAY)
				|| commandDetails.equalsIgnoreCase(MONDAY_SHORT);
	}
	
	private static boolean isCaseDisplayTuesday() {
		return commandDetails.equalsIgnoreCase(TUESDAY)
				|| commandDetails.equalsIgnoreCase(TUESDAY_SHORT);
	}
	
	private static boolean isCaseDisplayWednesday() {
		return commandDetails.equalsIgnoreCase(WEDNESDAY)
				|| commandDetails.equalsIgnoreCase(WEDNESDAY_SHORT);
	}
	
	private static boolean isCaseDisplayThursday() {
		return commandDetails.equalsIgnoreCase(THURSDAY)
				|| commandDetails.equalsIgnoreCase(THURSDAY_SHORT);
	}
	
	private static boolean isCaseDisplayFriday() {
		return commandDetails.equalsIgnoreCase(FRIDAY)
				|| commandDetails.equalsIgnoreCase(FRIDAY_SHORT);
	}
	
	private static boolean isCaseDisplaySaturday() {
		return commandDetails.equalsIgnoreCase(SATURDAY)
				|| commandDetails.equalsIgnoreCase(SATURDAY_SHORT);
	}
	
	private static boolean isCaseDisplaySunday() {
		return commandDetails.equalsIgnoreCase(SUNDAY)
				|| commandDetails.equalsIgnoreCase(SUNDAY_SHORT);
	}

	private static boolean isCaseDisplayThisWeek() {
		return commandDetails.equalsIgnoreCase(THIS_WEEK);
	}

	private static boolean isCaseDisplayNextWeek() {
		return commandDetails.equalsIgnoreCase(NEXT_WEEK);
	}

	private static boolean isCaseDisplayThisMonth() {
		return commandDetails.equalsIgnoreCase(THIS_MONTH);
	}

	private static boolean isCaseDisplayNextMonth() {
		return commandDetails.equalsIgnoreCase(NEXT_MONTH);
	}

	//change for display string
	private static boolean isCaseDisplayString() {

		return commandDetails.equals(ALL) ||
				commandDetails.equals(INCOMPLETE_TASK) ||
				commandDetails.equals(FLOATING_TASK) ||
				commandDetails.equals(DEADLINE_TASK) ||
				commandDetails.equals(TIMED_TASK);
	}

	private static boolean isCaseDisplayCalendar() throws InvalidInputException {
		processCalendarInfo();
		return doesContainTimeInfo();
	}

	private static boolean isNextMonday(GregorianCalendar cal) {
		return cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
				&& endCalendar.get(Calendar.DATE) != startCalendar.get(Calendar.DATE);
	}
}
