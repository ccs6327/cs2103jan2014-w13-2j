package clc.ui;

import static clc.common.Constants.AM;
import static clc.common.Constants.COMMA;
import static clc.common.Constants.DOT;
import static clc.common.Constants.PM;
import static clc.common.Constants.SLASH;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeAnalyzer extends Analyzer{
	private static final String TODAY = "today";
	private static final String TOMORROW = "tomorrow";
	protected static int firstDateIndex;
	protected static ArrayList<Integer> monthInfo;
	protected static ArrayList<Integer> dateInfo;
	protected static ArrayList<Integer> timeInfo;
	protected static GregorianCalendar startTime = null;
	protected static GregorianCalendar endTime = null;
	private static boolean isPm = false;
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

	protected static void recordAndProcessCalendarInfoProvided() {
		monthInfo = new ArrayList<Integer>();
		dateInfo = new ArrayList<Integer>();
		timeInfo = new ArrayList<Integer>();
		recordCalendarInfo();
		if (dateInfo.size() > 0 || timeInfo.size() > 0) { //have Calendar Info to be processed
			processCalendarInfo();
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
				int day = Integer.parseInt(splitDate[0]);
				int month = Integer.parseInt(splitDate[1]);
				dateInfo.add(day);
				monthInfo.add(month);
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
			}
		}
	}

	private static void addKeywordDayInfo(String currWord) {
		int date = 0, month = 0;
		switch (currWord) {
		case TODAY:
			date = Calendar.getInstance().get(Calendar.DATE);
			month = Calendar.getInstance().get(Calendar.MONTH);
			break;
		case TOMORROW:
			date = Calendar.getInstance().get(Calendar.DATE) + 1;
			month = Calendar.getInstance().get(Calendar.MONTH);
			break;
		}
		dateInfo.add(date);
		monthInfo.add(month);
	}

	private static void processCalendarInfo() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int endMonth = Calendar.getInstance().get(Calendar.MONTH);
		int endDate = Calendar.getInstance().get(Calendar.DATE);
		int startMonth = 0, startDate = 0;
		int startHour = 0, startMin = 0, endHour = 0, endMin = 0;

		//process day and month
		if (dateInfo.size() >= 1) { //monthInfo == 0 as well
			endMonth = monthInfo.get(0) - 1;
			endDate = dateInfo.get(0);
			if (dateInfo.size() == 2) {
				startMonth = monthInfo.get(1) - 1;
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
			startTime = new GregorianCalendar(year, startMonth, startDate, startHour, startMin);
		}
		endTime = new GregorianCalendar(year, endMonth, endDate, endHour, endMin);
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

		//only support dd/mm/yy format or dd/mm format
		if (splitDate.length < 2 || splitDate.length > 3) {
			return false;
		}


		if (isNumeric(splitDate[0]) && isNumeric(splitDate[1])) {
			isDate = true;
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
		case TODAY:
		case TOMORROW:
			return true;
		default:
			return false;
		}
	}
}
