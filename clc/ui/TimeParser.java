package clc.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import clc.common.InvalidInputException;
import static clc.common.Constants.ERROR_START_TIME;
import static clc.common.Constants.ERROR_END_TIME;
import static clc.common.Constants.ERROR_START_TIME_LATER_THAN_END_TIME;
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
import static clc.common.Constants.AM;
import static clc.common.Constants.PM;
import static clc.common.Constants.TIME_12H_PATTERNS;
import static clc.common.Constants.TIME_24H_PATTERNS;
import static clc.common.Constants.DATE_PATTERNS;
import static clc.common.Constants.DOT;
import static clc.common.Constants.COLON;
import static clc.common.Constants.SLASH;
import static clc.common.Constants.DASH;
import static clc.common.Constants.EMPTY;
import static clc.common.Constants.FROM;
import static clc.common.Constants.BY;
import static clc.common.Constants.DUE;
import static clc.common.Constants.AT;
import static clc.common.Constants.NEXT;

public class TimeParser extends Analyzer {
	private static SimpleDateFormat[] dateFormat, time12Format, time24Format;
	private static GregorianCalendar analyzedCalendar;
	private static boolean doesContainAmOrPm;
	private static boolean isDate, isKeywordDate, isTime, isTodayTomorrow;
	protected static GregorianCalendar startCalendar, endCalendar;
	protected static String[] infoToBeProcessed = null;
	protected static boolean isStartDateSet, isStartTimeSet, isEndDateSet, isEndTimeSet;
	protected static boolean isRecurringTime;
	protected static int startCalendarIndex, endCalendarIndex;

	protected TimeParser(String input) {
		super(input);
	}

	protected static ArrayList<GregorianCalendar> getCalendar() {
		ArrayList<GregorianCalendar> time = new ArrayList<GregorianCalendar>();
		time.add(startCalendar);
		time.add(endCalendar);
		return time;
	}

	protected static void processCalendarInfo() throws InvalidInputException {
		initializeVariable();
		int currIndex = infoToBeProcessed.length - 1;

		while (currIndex >= 0 && !hasAllTimeSet()) {
			String toBeAnalyzedString = EMPTY;
			int loopIndex = currIndex;
			for (int i = 0; i < 3; i ++) { // calendar at most represent by 3 Strings
				toBeAnalyzedString = infoToBeProcessed[loopIndex] +  toBeAnalyzedString;
				try {
					analyzeTime(toBeAnalyzedString);
					currIndex = caseKeywordNextOrEveryBeforeKeywordDate(loopIndex);
					setStartAndEndCalendarIndex(currIndex, loopIndex);
					break;
				} catch (ParseException e) { //catch parsing error
					if (loopIndex - 1 < 0) {
						break;
					}
				}
				loopIndex --;
			}
			currIndex --;
			setCalendar();
		}

		setStartCalendarAsNullIfNotSet();
		setEndCalendarAsNullIfNotSet();
		caseIfStartAndEndCalendarShareOneDate();
		caseIfCalendarBeforeCurrentTime();
		caseIfStartTimeLaterThanEndTime();
		removePrepositionOfCalendar();
	}

	private static int caseKeywordNextOrEveryBeforeKeywordDate(int loopIndex) {
		int nNext = 0, nEvery = 0;
		if (isKeywordDate && !isTodayTomorrow) {
			nNext = countExtraNext(loopIndex);
			nEvery = checkIfKeywordEveryExists(loopIndex);
			analyzedCalendar.add(Calendar.WEEK_OF_YEAR, nNext);
		}
		return loopIndex - nNext - nEvery;
	}

	private static int checkIfKeywordEveryExists(int loopIndex) {
		if (--loopIndex >= 0 && infoToBeProcessed[loopIndex].equalsIgnoreCase("every")) {
			isRecurringTime = true;
			return 1;
		}
		return 0; //no "every"
	}

	private static void setStartAndEndCalendarIndex(int currIndex, int loopIndex) {
		if (endCalendarIndex == -1) { //never set before
			startCalendarIndex = currIndex; //consider case extra "next"
			endCalendarIndex = loopIndex;
		} else if (startCalendarIndex == -1 || loopIndex < startCalendarIndex) {
			startCalendarIndex = currIndex;
		}
	}

	private static int countExtraNext(int loopIndex) {
		int nNext = 0;
		while (--loopIndex >= 0) {
			if (infoToBeProcessed[loopIndex].equals(NEXT)) {
				nNext ++;
			}
			else {
				break;
			}
		}
		return nNext;
	}

	private static void removePrepositionOfCalendar() {
		int infoLength = infoToBeProcessed.length;

		if (startCalendarIndex - 1 > 0) {
			if (infoToBeProcessed[startCalendarIndex -1].equalsIgnoreCase(FROM)
					|| infoToBeProcessed[startCalendarIndex -1].equalsIgnoreCase(BY)
					|| infoToBeProcessed[startCalendarIndex -1].equalsIgnoreCase(DUE)
					|| infoToBeProcessed[startCalendarIndex -1].equalsIgnoreCase(AT)) {
				startCalendarIndex --;
			}
		}
		
		try {	
			if (startCalendarIndex != -1 && endCalendarIndex != -1) {
				if (endCalendarIndex + 1 < infoLength) {
					checkIfContainCalendarInfo(infoToBeProcessed[endCalendarIndex] + infoToBeProcessed[endCalendarIndex + 1]);
					endCalendarIndex ++;
				} else if (endCalendarIndex + 2 < infoLength) {
					checkIfContainCalendarInfo(infoToBeProcessed[endCalendarIndex] + infoToBeProcessed[endCalendarIndex + 1] 
							+ infoToBeProcessed[endCalendarIndex + 2]);
					endCalendarIndex += 2;
				}
			}
		} catch (ParseException e) {}
	}

	private static void checkIfContainCalendarInfo (String currStr) throws ParseException {
		parseIfDateFormat(currStr);
		parseIfTime12Format(currStr);
		//Time24Format does not contains Date Format that is represent by more than one String
	}

	protected static void initializeVariable() {
		dateFormat = initializeDateFormat();
		time12Format = initializeTime12Format();
		time24Format = initializeTime24Format();
		isStartDateSet = false;
		isStartTimeSet = false; 
		isEndDateSet = false; 
		isEndTimeSet = false;
		isDate = false;
		isKeywordDate = false;
		isTime = false;
		startCalendar = new GregorianCalendar();
		endCalendar = new GregorianCalendar();
		startCalendarIndex = -1;
		endCalendarIndex = -1;
	}

	public static void analyzeTime(String currStr) throws ParseException {
		doesContainAmOrPm = false;
		determineIfFormatError(currStr); //e.g. 13pm or 13 am etc

		isDate = parseIfDateFormat(currStr);
		isKeywordDate = parseIfKeywordDateFormat(currStr);
		if (doesContainAmOrPm) {
			isTime = parseIfTime12Format(currStr);
		} else {
			isTime = parseIfTime24Format(currStr);
		}
		determineIfFailsAllParsing();
	}

	protected static void setCalendar() {
		if (isDate || isKeywordDate) {
			setDate();
		} else if (isTime) {
			setTime();
		}
	}

	protected static void caseIfCalendarBeforeCurrentTime()
			throws InvalidInputException {
		if (startCalendar != null && startCalendar.compareTo(Calendar.getInstance()) == -1) {
			throw new InvalidInputException(ERROR_START_TIME);
		} else if (endCalendar != null && endCalendar.compareTo(Calendar.getInstance()) == -1) {
			throw new InvalidInputException(ERROR_END_TIME);
		}
	}

	private static void caseIfStartTimeLaterThanEndTime() throws InvalidInputException {
		if (startCalendar != null && endCalendar != null && isStartCalendarLaterThanEndCalendar()) {
			throw new InvalidInputException(ERROR_START_TIME_LATER_THAN_END_TIME);
		}
	}

	private static void caseIfStartAndEndCalendarShareOneDate() {
		if (startCalendar != null && endCalendar != null 
				&& !isEndDateSet && isStartCalendarLaterThanEndCalendar()) {
			System.out.println("YES");
			endCalendar.set(Calendar.YEAR, startCalendar.get(Calendar.YEAR));
			endCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH));
			endCalendar.set(Calendar.DATE, startCalendar.get(Calendar.DATE));
		}
	}

	protected static void setStartCalendarAsNullIfNotSet() {
		if (!isStartTimeSet && !isStartDateSet) {
			startCalendar = null;
		}
	}

	protected static void setEndCalendarAsNullIfNotSet() {
		if (!isEndTimeSet && !isEndDateSet) {
			endCalendar = null;
		}
	}

	private static void setTime() {
		if (!isEndTimeSet) {
			setEndTime();
		} else if (!isStartTimeSet) {
			setStartTime();
		}
	}

	private static void setStartTime() {
		isStartTimeSet = true;
		startCalendar.set(Calendar.HOUR_OF_DAY, analyzedCalendar.get(Calendar.HOUR_OF_DAY));
		startCalendar.set(Calendar.MINUTE, analyzedCalendar.get(Calendar.MINUTE));
		startCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MILLISECOND, 0);
	}

	private static void setEndTime() {
		isEndTimeSet = true;
		endCalendar.set(Calendar.HOUR_OF_DAY, analyzedCalendar.get(Calendar.HOUR_OF_DAY));
		endCalendar.set(Calendar.MINUTE, analyzedCalendar.get(Calendar.MINUTE));
		endCalendar.set(Calendar.SECOND, 0);
		endCalendar.set(Calendar.MILLISECOND, 0);
	}

	private static void setDate() {
		//if isStartTimeSet is true implies that the date is before the start time
		if (!isEndDateSet && !isStartTimeSet) { 
			setEndDate();
		} else if (!isStartDateSet) {
			setStartDate();
		}
	}

	private static void setStartDate() {
		isStartDateSet = true;
		startCalendar.set(Calendar.YEAR, analyzedCalendar.get(Calendar.YEAR));
		startCalendar.set(Calendar.MONTH, analyzedCalendar.get(Calendar.MONTH));
		startCalendar.set(Calendar.DATE, analyzedCalendar.get(Calendar.DATE));
	}

	private static void setEndDate() {
		isEndDateSet = true;
		endCalendar.set(Calendar.YEAR, analyzedCalendar.get(Calendar.YEAR));
		endCalendar.set(Calendar.MONTH, analyzedCalendar.get(Calendar.MONTH));
		endCalendar.set(Calendar.DATE, analyzedCalendar.get(Calendar.DATE));
	}

	private static boolean parseIfKeywordDateFormat(String currStr) {
		Calendar currTime = Calendar.getInstance(); //get current Calendar
		isTodayTomorrow = false;
		int addValue = -1;

		if (isToday(currStr)) {
			isTodayTomorrow = true;
			addValue = 0;
		} else if (isTomorrow(currStr)) {
			isTodayTomorrow = true;
			addValue = 1;
		} else if (isMonday(currStr)) {
			addValue = Calendar.MONDAY - currTime.get(Calendar.DAY_OF_WEEK);
		} else if (isTuesday(currStr)) {
			addValue = Calendar.TUESDAY - currTime.get(Calendar.DAY_OF_WEEK);
		} else if (isWednesday(currStr)) {
			addValue = Calendar.WEDNESDAY - currTime.get(Calendar.DAY_OF_WEEK);
		} else if (isThursday(currStr)) {
			addValue = Calendar.THURSDAY - currTime.get(Calendar.DAY_OF_WEEK);
		} else if (isFriday(currStr)) {
			addValue = Calendar.FRIDAY - currTime.get(Calendar.DAY_OF_WEEK);
		} else if (isSaturday(currStr)) {
			addValue = Calendar.SATURDAY - currTime.get(Calendar.DAY_OF_WEEK);
		} else if (isSunday(currStr)) {
			addValue = Calendar.SUNDAY - currTime.get(Calendar.DAY_OF_WEEK);
		} else {
			return false;
		}

		if (!isTodayTomorrow && addValue <= 0) { // day before today will get negative value
			addValue += 7;
		}

		analyzedCalendar = new GregorianCalendar();
		analyzedCalendar.add(GregorianCalendar.DAY_OF_WEEK, addValue);

		return true;
	}

	private static boolean parseIfDateFormat(String currStr) {
		Date date;
		for (int i = 0; i < dateFormat.length; i++) { //check if date format
			try {
				date = dateFormat[i].parse(currStr);
				analyzedCalendar = new GregorianCalendar();
				analyzedCalendar.setTime(date);

				if (analyzedCalendar.get(Calendar.YEAR) < 100) { //two digits year is given
					analyzedCalendar.add(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR)/100*100);
				}

				if (analyzedCalendar.get(Calendar.YEAR) == 1970) { //calendar provided without year
					analyzedCalendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
				}

				return true;
			} catch (ParseException e) {}
		}

		return false;
	}

	private static boolean parseIfTime12Format(String currStr) {
		Date date;
		for (int i = 0; i < time12Format.length; i++) { //check if time format
			try {
				date = time12Format[i].parse(currStr);
				analyzedCalendar = new GregorianCalendar();
				analyzedCalendar.setTime(date);
				//System.out.println("[" + currStr + "]: " + analyzedCalendar.getTime().toString());
				return true;
			} catch (ParseException e) {}
		}
		return false;
	}

	private static boolean parseIfTime24Format(String currStr) {
		Date date;
		for (int i = 0; i < time24Format.length; i++) { //check if time format
			try {
				date = time24Format[i].parse(currStr);
				analyzedCalendar = new GregorianCalendar();
				analyzedCalendar.setTime(date);
				//System.out.println("[" + currStr + "]: " + analyzedCalendar.getTime().toString());
				return true;
			} catch (ParseException e) {}
		}
		return false;
	}

	private static void determineIfFormatError(String currStr) throws ParseException {
		if (currStr.length() > 2 && (currStr.toLowerCase().contains(AM) || currStr.toLowerCase().contains(PM))) {
			doesContainAmOrPm = true;
			if (!currStr.contains(DOT) && !currStr.contains(COLON) && !currStr.contains(SLASH) && !currStr.contains(DASH)) {
				//remove am or pm
				String firstTwoChar = currStr.substring(0,currStr.length() - 2).trim(); //firstTwoCharacter
				try {
					if (Integer.parseInt(firstTwoChar) > 12 && Integer.parseInt(firstTwoChar) < 100) {
						throw new ParseException(EMPTY, 0); //handle this e.g. 13pm
					}
				}
				catch(NumberFormatException nfe) {}
			}
		}
	}

	private static void determineIfFailsAllParsing() throws ParseException {
		if (hasFailedAllParsing()) { // all nFail
			throw new ParseException(EMPTY, 0);
		}
	}

	private static boolean hasFailedAllParsing() {
		return !isTime && !isDate && !isKeywordDate; //not time nor date / not supported
	}

	private static boolean isStartCalendarLaterThanEndCalendar() {
		return startCalendar.compareTo(endCalendar) == 1;
	}

	protected static boolean hasAllTimeSet() {
		return isStartDateSet && isStartTimeSet
				&& isEndDateSet && isEndTimeSet;
	}

	protected static boolean doesContainTimeInfo() throws InvalidInputException {
		processCalendarInfo();
		return isStartDateSet || isStartTimeSet 
				|| isEndDateSet || isEndTimeSet;
	}

	protected static boolean isCaseTimedTask() {
		return startCalendar != null && endCalendar != null;
	}

	protected static boolean isCaseDeadlineTask() {
		return startCalendar == null && endCalendar != null;
	}

	private static boolean isToday(String currStr) {
		return currStr.equalsIgnoreCase(TODAY) || currStr.equalsIgnoreCase(TODAY_SHORT);
	}

	private static boolean isTomorrow(String currStr) {
		return currStr.equalsIgnoreCase(TOMORROW) || currStr.equalsIgnoreCase(TOMORROW_SHORT);
	}

	private static boolean isMonday(String currStr) {
		return currStr.equalsIgnoreCase(MONDAY) || currStr.equalsIgnoreCase(MONDAY_SHORT);
	}

	private static boolean isTuesday(String currStr) {
		return currStr.equalsIgnoreCase(TUESDAY) || currStr.equalsIgnoreCase(TUESDAY_SHORT);
	}

	private static boolean isWednesday(String currStr) {
		return currStr.equalsIgnoreCase(WEDNESDAY) || currStr.equalsIgnoreCase(WEDNESDAY_SHORT);
	}

	private static boolean isThursday(String currStr) {
		return currStr.equalsIgnoreCase(THURSDAY) || currStr.equalsIgnoreCase(THURSDAY_SHORT);
	}

	private static boolean isFriday(String currStr) {
		return currStr.equalsIgnoreCase(FRIDAY) || currStr.equalsIgnoreCase(FRIDAY_SHORT);
	}

	private static boolean isSaturday(String currStr) {
		return currStr.equalsIgnoreCase(SATURDAY) || currStr.equalsIgnoreCase(SATURDAY_SHORT);
	}

	private static boolean isSunday(String currStr) {
		return currStr.equalsIgnoreCase(SUNDAY) || currStr.equalsIgnoreCase(SUNDAY_SHORT);
	}

	private static SimpleDateFormat[] initializeTime12Format() {
		// initialize time12Format
		SimpleDateFormat[] timeFormat = new SimpleDateFormat[TIME_12H_PATTERNS.length];
		for (int i = 0; i < timeFormat.length; i++) {
			timeFormat[i] = new SimpleDateFormat(TIME_12H_PATTERNS[i]);
			timeFormat[i].setLenient(false);
		}
		return timeFormat;
	}

	private static SimpleDateFormat[] initializeTime24Format() {
		// initialize time24Format
		SimpleDateFormat[] timeFormat = new SimpleDateFormat[TIME_24H_PATTERNS.length];
		for (int i = 0; i < timeFormat.length; i++) {
			timeFormat[i] = new SimpleDateFormat(TIME_24H_PATTERNS[i]);
			timeFormat[i].setLenient(false);
		}
		return timeFormat;
	}

	private static SimpleDateFormat[] initializeDateFormat() {
		// initialize dateFormat
		SimpleDateFormat[] dateFormat = new SimpleDateFormat[DATE_PATTERNS.length];
		for (int i = 0; i < dateFormat.length; i++) {
			dateFormat[i] = new SimpleDateFormat(DATE_PATTERNS[i]);
			dateFormat[i].setLenient(false);
		}
		return dateFormat;
	}
}
