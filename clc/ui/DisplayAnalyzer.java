package clc.ui;

import static clc.common.Constants.ERROR_INVALID_DISPLAY_REQUEST;
import static clc.common.Constants.SPACE;
import static clc.common.Constants.EMPTY;
import static clc.common.Constants.NEXT;
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
	private static String lastWord, lastTwoWords;

	protected DisplayAnalyzer(String input) {
		super(input);
	}

	protected static void analyze() throws InvalidInputException{
		infoToBeProcessed = commandDetails.split(SPACE);
		isCaseDisplayCalendar = true;
		isCaseKeywordCalendar = true;
		lastWord = EMPTY;
		lastTwoWords = EMPTY;

		year = Calendar.getInstance().get(Calendar.YEAR);
		month = Calendar.getInstance().get(Calendar.MONTH);
		date = Calendar.getInstance().get(Calendar.DATE);
		dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

		retrieveLastAndLastTwoWordsIfAvailable();

		int nNext = 0;
		if (isCaseDisplayToday()) {
			setToday();
		} else if (isCaseDisplayTomorrow()) {
			setTomorrow();
		} else if (isCaseDisplayMonday()) {
			setMonday();
			nNext = countExtraNext();
		} else if (isCaseDisplayTuesday()) {
			setTuesday();
			nNext = countExtraNext();
		} else if (isCaseDisplayWednesday()) {
			setWednesday();
			nNext = countExtraNext();
		} else if (isCaseDisplayThursday()) {
			setThursday();
			nNext = countExtraNext();
		} else if (isCaseDisplayFriday()) {
			setFriday();
			nNext = countExtraNext();
		} else if (isCaseDisplaySaturday()) {
			setSaturday();
			nNext = countExtraNext();
		} else if (isCaseDisplaySunday()) {
			setSunday();
			nNext = countExtraNext();
		} else if (isCaseDisplayThisWeek()) {
			setThisWeek();
		} else if (isCaseDisplayNextWeek()) {
			setNextWeek();
			nNext = countExtraNext();
		} else if (isCaseDisplayThisMonth()) {
			setThisMonth();
		} else if (isCaseDisplayNextMonth()) {
			setNextMonth();
			nNext = countExtraNext();
		} else {
			isCaseKeywordCalendar = false;
		}

		if (isCaseKeywordCalendar) {
			if (isCaseDisplayMondayToSundayOrNextWeekMonth()) {
			if (isCaseDisplayNextMonth()) {
				startCalendar.add(Calendar.MONTH, nNext);
				endCalendar.add(Calendar.MONTH, nNext);
			} else {
				startCalendar.add(Calendar.WEEK_OF_YEAR, nNext);
				endCalendar.add(Calendar.WEEK_OF_YEAR, nNext);
			}
			}
		} else {
			if (isCaseDisplayCalendar()) {
				isCaseKeywordCalendar = false;
			} else if(isCaseDisplayString()){
				isCaseDisplayCalendar = false;
				isCaseKeywordCalendar = false;
			} else {
				throw new InvalidInputException(ERROR_INVALID_DISPLAY_REQUEST);
			}
		}
	}

	private static void retrieveLastAndLastTwoWordsIfAvailable() {
		if (infoToBeProcessed.length >= 1) {
			lastWord = infoToBeProcessed[infoToBeProcessed.length - 1];
			if (infoToBeProcessed.length >= 2) {
				lastTwoWords = infoToBeProcessed[infoToBeProcessed.length - 2] + SPACE + lastWord;
			}
		}
	}

	private static int countExtraNext() throws InvalidInputException {
		int nNext = 0;

		int toBeAnalyzedLength = infoToBeProcessed.length - 1; //last str

		//ignore the first "next" of next week and next month
		if (isCaseDisplayNextWeek() || isCaseDisplayNextMonth()) {
			toBeAnalyzedLength --;
		}

		for (int i = 0; i < toBeAnalyzedLength; i ++) {
			if (infoToBeProcessed[i].equalsIgnoreCase(NEXT)) {
				nNext ++;
				commandDetails = NEXT + SPACE + commandDetails;
			} else {
				isCaseKeywordCalendar = false;
				return 0;
			}
		}
		
		return nNext;
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
		int addValue = (Calendar.MONDAY - endCalendar.get(Calendar.DAY_OF_WEEK) + 7) % 7;
		endCalendar.add(Calendar.DATE, addValue);
	}

	private static void setNextWeek() {
		commandDetails = NEXT_WEEK;
		// set the start time as next Monday, end time as the end of next week
		startCalendar = new GregorianCalendar(year, month, date);
		endCalendar = new GregorianCalendar(year, month , date);

		int addValue = (Calendar.MONDAY - startCalendar.get(Calendar.DAY_OF_WEEK) + 7) % 7;
		startCalendar.add(Calendar.DATE, addValue);
		addValue = (Calendar.MONDAY - endCalendar.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 7;
		endCalendar.add(Calendar.DATE, addValue);
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
		return lastWord.equalsIgnoreCase(MONDAY)
				|| lastWord.equalsIgnoreCase(MONDAY_SHORT);
	}

	private static boolean isCaseDisplayTuesday() {
		return lastWord.equalsIgnoreCase(TUESDAY)
				|| lastWord.equalsIgnoreCase(TUESDAY_SHORT);
	}

	private static boolean isCaseDisplayWednesday() {
		return lastWord.equalsIgnoreCase(WEDNESDAY)
				|| lastWord.equalsIgnoreCase(WEDNESDAY_SHORT);
	}

	private static boolean isCaseDisplayThursday() {
		return lastWord.equalsIgnoreCase(THURSDAY)
				|| lastWord.equalsIgnoreCase(THURSDAY_SHORT);
	}

	private static boolean isCaseDisplayFriday() {
		return lastWord.equalsIgnoreCase(FRIDAY)
				|| lastWord.equalsIgnoreCase(FRIDAY_SHORT);
	}

	private static boolean isCaseDisplaySaturday() {
		return lastWord.equalsIgnoreCase(SATURDAY)
				|| lastWord.equalsIgnoreCase(SATURDAY_SHORT);
	}

	private static boolean isCaseDisplaySunday() {
		return lastWord.equalsIgnoreCase(SUNDAY)
				|| lastWord.equalsIgnoreCase(SUNDAY_SHORT);
	}

	private static boolean isCaseDisplayThisWeek() {
		return commandDetails.equalsIgnoreCase(THIS_WEEK);
	}

	private static boolean isCaseDisplayNextWeek() {
		return lastTwoWords.equalsIgnoreCase(NEXT_WEEK);
	}

	private static boolean isCaseDisplayThisMonth() {
		return commandDetails.equalsIgnoreCase(THIS_MONTH);
	}

	private static boolean isCaseDisplayNextMonth() {
		return lastTwoWords.equalsIgnoreCase(NEXT_MONTH);
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
		return doesContainTimeInfo();
	}

	private static boolean isCaseDisplayMondayToSundayOrNextWeekMonth() {
		return isCaseKeywordCalendar && !isCaseDisplayToday()
				&& !isCaseDisplayTomorrow() && !isCaseDisplayThisWeek()
				&& !isCaseDisplayThisMonth();
	}
}
