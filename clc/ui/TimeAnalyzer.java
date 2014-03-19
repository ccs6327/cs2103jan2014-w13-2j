package clc.ui;

import static clc.common.Constants.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import clc.common.InvalidInputException;

public class TimeAnalyzer extends Analyzer{
	protected static int firstDateIndex;
	protected static ArrayList<Integer> monthInfo;
	protected static ArrayList<Integer> dateInfo;
	protected static ArrayList<Integer> timeInfo;
	private static ArrayList<Integer> yearInfo;
	protected static GregorianCalendar startTime = null;
	protected static GregorianCalendar endTime = null;
	private static boolean isPm;
	private static String[] splitDate = null;
	private static String[] splitTime = null;
	protected static String[] infoToBeProcessed = null;

	protected TimeAnalyzer(String input) {
		super(input);
	}

	protected static ArrayList<GregorianCalendar> getCalendar() {
		ArrayList<GregorianCalendar> time = new ArrayList<GregorianCalendar>();
		time.add(startTime);
		time.add(endTime);
		return time;
	}

	protected static void recordAndProcessCalendarInfoProvided() throws InvalidInputException {
		instantiateVariable();
		recordCalendarInfo();
		if (doesContainTimeInfo()) { //have Calendar Info to be processed
			processCalendarInfo();
		}
	}

	private static void instantiateVariable() {
		yearInfo = new ArrayList<Integer>();
		monthInfo = new ArrayList<Integer>();
		dateInfo = new ArrayList<Integer>();
		timeInfo = new ArrayList<Integer>();
		isPm = false;
	}

	protected static void determineIfStartTimeLaterThanEndTime()
			throws InvalidInputException {
		if (isStartTimeLaterThanEndTime()) {
			throw new InvalidInputException("ERROR: Start time is later than end time");
		}
	}

	private static void recordCalendarInfo() {
		int endLoopIndex = infoToBeProcessed.length - 4; //loop 4 times from the back only
		if (endLoopIndex < 0) {
			endLoopIndex = 0;
		}
		for (int i = infoToBeProcessed.length - 1; i >= endLoopIndex; i--) {

			String currWord = infoToBeProcessed[i];

			//contains either SLASH, DOT, AM or PM
			if (doesContainDateKeyword(currWord)) {
				addKeywordDayInfo(currWord);
				firstDateIndex = i;
			} else if (isDateFormat(currWord)) {
				addDateInfo();
				addMonthInfo();
				addYearInfo();
				firstDateIndex = i;
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
					isPm = false;
				} else {
					if (time == 12) {
						time -= 12;
					} else if (time >= 1200) {
						time -= 1200;
					}
				}
				timeInfo.add(time);
				firstDateIndex = i;
			} else {
				break;
			}
		}
	}

	private static void addDateInfo() {
		int date = Integer.parseInt(splitDate[0]);
		dateInfo.add(date);
	}

	private static void addMonthInfo() {
		int month = Integer.parseInt(splitDate[1]);
		monthInfo.add(month - 1); //calendar get month from 0 to 6
	}

	private static void addYearInfo() {
		if (splitDate.length == 3) { // year
			int year = Integer.parseInt(splitDate[2]);
			int currentYear = Calendar.getInstance().get(Calendar.YEAR); 
			if (year < 100) { //merge with first two digit of current year
				year += currentYear - (currentYear % 100);
			}
			yearInfo.add(year);
		}
	}

	private static void addKeywordDayInfo(String currWord) {
		GregorianCalendar gc = new GregorianCalendar(); //construct a current time Calendar
		int date = gc.get(Calendar.DATE);
		int month = gc.get(Calendar.MONTH);
		int addValue = 10; // -6 <= addValue <= 7

		switch (currWord) {
		case TODAY:
			break;
		case TOMORROW:
			date++;
			break;
		case MONDAY:
			addValue = Calendar.MONDAY - gc.get(Calendar.DAY_OF_WEEK);
			break;
		case TUESDAY:
			addValue = Calendar.TUESDAY - gc.get(Calendar.DAY_OF_WEEK);
			break;
		case WEDNESDAY:
			addValue = Calendar.WEDNESDAY - gc.get(Calendar.DAY_OF_WEEK);
			break;
		case THURSDAY:
			addValue = Calendar.THURSDAY - gc.get(Calendar.DAY_OF_WEEK);
			break;
		case FRIDAY:
			addValue = Calendar.FRIDAY - gc.get(Calendar.DAY_OF_WEEK);
			break;
		case SATURDAY:
			addValue = Calendar.SATURDAY - gc.get(Calendar.DAY_OF_WEEK);
			break;
		case SUNDAY:
			addValue = Calendar.SUNDAY - gc.get(Calendar.DAY_OF_WEEK);
			break;
		}

		if (addValue != 10) {
			if (addValue <= 0) addValue += 7;
			gc.add(GregorianCalendar.DAY_OF_WEEK, addValue);
			date = gc.get(Calendar.DATE);
			month = gc.get(Calendar.MONTH);
		}

		dateInfo.add(date);
		monthInfo.add(month);
	}

	private static void processCalendarInfo() {
		int endYear = Calendar.getInstance().get(Calendar.YEAR);
		int startYear = Calendar.getInstance().get(Calendar.YEAR);
		int endMonth = Calendar.getInstance().get(Calendar.MONTH);
		int endDate = Calendar.getInstance().get(Calendar.DATE);
		int startMonth = 0, startDate = 0;
		int startHour = 0, startMin = 0, endHour = 0, endMin = 0;

		//process year
		if(yearInfo.size() >= 1) {
			endYear = yearInfo.get(0);
			if (yearInfo.size() == 2) {
				startYear = yearInfo.get(1);
			}
		}

		//process day and month
		if (dateInfo.size() >= 1 && monthInfo.size() >= 1) {
			endMonth = monthInfo.get(0);
			endDate = dateInfo.get(0);
			if (dateInfo.size() == 2) {
				startMonth = monthInfo.get(1);
				startDate = dateInfo.get(1);
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
				if (timeInfo.get(1) < 24) {
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
			startTime = new GregorianCalendar(startYear, startMonth, startDate, startHour, startMin);
		}
		endTime = new GregorianCalendar(endYear, endMonth, endDate, endHour, endMin);
	}

	private static boolean isDateFormat(String currWord) {
		boolean isDate = false;

		if (doesContainSlash(currWord)) {
			splitDate = currWord.split(SLASH);
		} else if (doesContainDot(currWord)) {
			splitDate = currWord.split("\\" + DOT);
		} else {
			return false;
		}

		if (splitDate.length == 2 && isNumeric(splitDate[0]) 
				&& isNumeric(splitDate[1])) { //  case dd/mm
			isDate = true;
		} else if (splitDate.length == 3 && isNumeric(splitDate[0]) 
				&& isNumeric(splitDate[1]) && isNumeric(splitDate[2])) { 
			// case dd/mm/yy or //case dd/mm/yyyy
			isDate = true;
		} else {
			isDate = false;
		}

		return isDate;
	}

	private static boolean isTimeFormat(String currWord) {
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

	protected static boolean isCaseDeadlineTask() {
		return (dateInfo.size() == 0 && timeInfo.size() == 1) 
				|| (dateInfo.size() == 1 && timeInfo.size() == 1);
	}

	protected static boolean isCaseTimedTask() {
		return (dateInfo.size() == 0 && timeInfo.size() == 2) 
				|| (dateInfo.size() == 1 && timeInfo.size() == 2) 
				|| (dateInfo.size() == 2 && timeInfo.size() == 2);
	}

	private static boolean isStartTimeLaterThanEndTime() {
		return startTime != null && endTime != null && startTime.compareTo(endTime) == 1;
	}

	private static boolean doesContainPm(String currWord) {
		return currWord.contains(PM);
	}

	private static boolean doesContainAm(String currWord) {
		return currWord.contains(AM);
	}

	private static boolean doesContainDot(String currWord) {
		return currWord.contains(DOT);
	}

	private static boolean doesContainSlash(String currWord) {
		return currWord.contains(SLASH);
	}

	protected static boolean doesContainTimeInfo() {
		// check the last word whether contains date format
		int lastTimeIndex = infoToBeProcessed.length - 1;
		if (infoToBeProcessed[lastTimeIndex].equals(COMMA)) { //case update
			lastTimeIndex --;
		}
		return (isTimeFormat(infoToBeProcessed[lastTimeIndex])
				|| isDateFormat(infoToBeProcessed[lastTimeIndex])
				||doesContainDateKeyword(infoToBeProcessed[lastTimeIndex]));
	}

	protected static boolean doesContainDateKeyword(String currWord) { //today, tomorrow etc
		switch (currWord) {
		case TODAY: case TOMORROW: case MONDAY: 
		case TUESDAY: case WEDNESDAY: case THURSDAY: 
		case FRIDAY: case SATURDAY: case SUNDAY:
			return true;
		default:
			return false;
		}
	}
}
