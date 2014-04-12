//@author A0112089J

package clc.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import clc.common.InvalidInputException;
import clc.common.LogHelper;
import static clc.common.Constants.ERROR_START_TIME;
import static clc.common.Constants.ERROR_END_TIME;
import static clc.common.Constants.ERROR_START_TIME_LATER_THAN_OR_EQUAL_TO_END_TIME;
import static clc.common.Constants.ERROR_DAILY_RECURRING_ONE_DATE_ONLY;
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
import static clc.common.Constants.TO;
import static clc.common.Constants.NEXT;
import static clc.common.Constants.EVERY;
import static clc.common.Constants.EVERYDAY;
import static clc.common.Constants.EVERY_WEEK;

public class TimeParser extends Analyzer {
	private static SimpleDateFormat[] dateFormat, time12Format, time24Format;
	private static GregorianCalendar analyzedCalendar;
	private static boolean doesContainAmOrPm;
	private static boolean isDate, isKeywordDate, isTime, isMondayToSunday;
	protected static GregorianCalendar startCalendar, endCalendar;
	protected static String[] infoToBeProcessed;
	protected static boolean isStartDateSet, isStartTimeSet, isEndDateSet, isEndTimeSet;
	protected static int startCalendarIndex, endCalendarIndex;
	protected static boolean isRecurringEveryWeek, isRecurringEveryday;
	protected static String recurringPeriod;

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
		analyzeAndSetCalendar();
		setStartCalendarAsNullIfNotSet();
		setEndCalendarAsNullIfNotSet();
		caseIfStartAndEndCalendarShareOneDate();
		caseIfRecurringTimeBeforeCurrentTime();
		caseIfCalendarBeforeOrEqualToCurrentTime();
		caseIfStartTimeLaterThanOrEqualToEndTime();
		adjustToCorrectCalendarIndex();
	}

	protected static void initializeVariable() {
		dateFormat = initializeDateFormat();
		time12Format = initializeTime12Format();
		time24Format = initializeTime24Format();
		isStartDateSet = false;
		isStartTimeSet = false; 
		isEndDateSet = false; 
		isEndTimeSet = false;
		startCalendar = new GregorianCalendar();
		endCalendar = new GregorianCalendar();
		startCalendarIndex = -1;
		endCalendarIndex = -1;
		LogHelper.info("TimeParser variables initialized");
	}

	protected static void analyzeAndSetCalendar() throws InvalidInputException {
		int currIndex = infoToBeProcessed.length - 1;
		while (currIndex >= 0 && !hasAllTimeSet()) {
			currIndex = analyzeCalendar(currIndex);
			setCalendar();

			currIndex --;
			currIndex = skipNextIndexIfPreposition(currIndex);
		}
	}

	private static int analyzeCalendar(int currIndex)
			throws InvalidInputException {
		String toBeAnalyzedString = EMPTY;
		for (int i = 0; i < 3; i ++) { // calendar at most represent by 3 words
			int loopIndex = currIndex - i;
			toBeAnalyzedString = infoToBeProcessed[loopIndex] +  toBeAnalyzedString;
			try {
				parseCalendarAndSetAnalyzedCalendar(toBeAnalyzedString);
				LogHelper.info("'" + toBeAnalyzedString + "'" + " PARSE INTO " + analyzedCalendar.getTime().toString());
				checkIfDateFormatParsedCorrectly(infoToBeProcessed[loopIndex]);
				currIndex = caseKeywordNextOrEveryBeforeKeywordDate(loopIndex);
				setStartAndEndCalendarIndex(currIndex, loopIndex);
				break;
			} catch (ParseException e) { //catch parsing error
				if (loopIndex - 1 < 0) {
					break;
				}
			}
		}
		return currIndex;
	}

	private static void checkIfDateFormatParsedCorrectly(String currWord) {
		if (isDate) { //avoid case "1/1/20 34" parse into "1/1/2034"
			 parseIfDateFormat(currWord);
			 LogHelper.info("'" + currWord + "'" + " PARSE INTO " + analyzedCalendar.getTime().toString());
		}
	}

	private static void setStartAndEndCalendarIndex(int currIndex, int loopIndex) {
		if (endCalendarIndex == -1) { //never set before
			startCalendarIndex = currIndex; //consider case extra "next"
			endCalendarIndex = loopIndex;
		} else if (startCalendarIndex == -1 || loopIndex < startCalendarIndex) {
			startCalendarIndex = currIndex;
		}
	}

	private static int skipNextIndexIfPreposition(int currIndex) {
		if (currIndex >= 0 && isPrepositionOfTime(infoToBeProcessed[currIndex])) {
			LogHelper.info("Preposition of time found: " + infoToBeProcessed[currIndex]);
			return currIndex - 1;
		}
		return currIndex;
	}

	private static boolean isPrepositionOfTime(String currWord) {
		return currWord.equalsIgnoreCase(FROM)
				|| currWord.equalsIgnoreCase(BY)
				|| currWord.equalsIgnoreCase(DUE)
				|| currWord.equalsIgnoreCase(AT)
				|| currWord.equalsIgnoreCase(TO);
	}

	private static void caseIfRecurringTimeBeforeCurrentTime() {
		if (isRecurringEveryWeek) {
			postponeStartAndEndCalendarToNextWeekIfRequired();
		} else if (isRecurringEveryday) {
			postponeStartAndEndCalendarToNextDayIfRequired();
		}
	}

	private static void postponeStartAndEndCalendarToNextWeekIfRequired() {
		if (startCalendar != null && startCalendar.compareTo(Calendar.getInstance()) == -1) {
			startCalendar.add(Calendar.WEEK_OF_YEAR, 1);
			endCalendar.add(Calendar.WEEK_OF_YEAR, 1);
		} else if (endCalendar != null && endCalendar.compareTo(Calendar.getInstance()) == - 1) {
			endCalendar.add(Calendar.WEEK_OF_YEAR, 1);
		}
	}

	private static void postponeStartAndEndCalendarToNextDayIfRequired() {
		if (startCalendar != null && startCalendar.compareTo(Calendar.getInstance()) == -1) {
			startCalendar.add(Calendar.DAY_OF_YEAR, 1);
			endCalendar.add(Calendar.DAY_OF_YEAR, 1);
		} else if (endCalendar != null && endCalendar.compareTo(Calendar.getInstance()) == - 1) {
			endCalendar.add(Calendar.DAY_OF_YEAR, 1);
		}
	}

	private static int caseKeywordNextOrEveryBeforeKeywordDate(int loopIndex) {
		int nNext = 0, nEvery = 0;
		if (isMondayToSunday) {
			nNext = countExtraNext(loopIndex);
			nEvery = checkIfKeywordEveryExists(loopIndex);
			analyzedCalendar.add(Calendar.WEEK_OF_YEAR, nNext);
		}
		return loopIndex - nNext - nEvery;
	}

	private static int countExtraNext(int loopIndex) {
		int nNext = 0;
		while (--loopIndex >= 0 && infoToBeProcessed[loopIndex].equalsIgnoreCase(NEXT)) {
			nNext ++;
		}
		LogHelper.info("Keyword 'next' found: " + nNext);
		return nNext;
	}

	private static int checkIfKeywordEveryExists(int loopIndex) {
		if (--loopIndex >= 0 && infoToBeProcessed[loopIndex].equalsIgnoreCase(EVERY)) {
			isRecurringEveryWeek = true;
			recurringPeriod = EVERY_WEEK;
			LogHelper.info("Keyword 'every' found");
			return 1;
		}
		return 0; //no "every"
	}

	private static void adjustToCorrectCalendarIndex() {
		determineIfWordBeforeIsPreposition();

		int infoLength = infoToBeProcessed.length;
		determineIfEndCalendarIndexIsCorrect(infoLength);
	}

	private static void determineIfWordBeforeIsPreposition() {
		if (startCalendarIndex - 1 >= 0) {
			if (isPrepositionOfTime(infoToBeProcessed[startCalendarIndex - 1])) {
				startCalendarIndex --;
			}
		}
	}

	private static void determineIfEndCalendarIndexIsCorrect(int infoLength) {
		if (isStartAndEndCalendarIndexSet() && 
				!doesContainCalendarInfo(infoToBeProcessed[endCalendarIndex])) {
			if (endCalendarIndex + 1 < infoLength) { //doesn't exceed the infoLength
				if (doesContainCalendarInfo(infoToBeProcessed[endCalendarIndex] 
						+ infoToBeProcessed[endCalendarIndex + 1])) { //two words is a calendar
					endCalendarIndex ++;
				}
			} else if (endCalendarIndex + 2 < infoLength) { //doesn't exceed the infoLength
				if (doesContainCalendarInfo(infoToBeProcessed[endCalendarIndex] //three words is a calendar
						+ infoToBeProcessed[endCalendarIndex + 1] 
								+ infoToBeProcessed[endCalendarIndex + 2])) {
					endCalendarIndex += 2;
				}
			}
		}
	}

	private static boolean isStartAndEndCalendarIndexSet() {
		return startCalendarIndex != -1 && endCalendarIndex != -1;
	}

	private static boolean doesContainCalendarInfo (String currStr) {
		return parseIfDateFormat(currStr) || parseIfTime12Format(currStr);
		//Time24Format does not contains time format that is represented by more than one String
	}

	private static void parseCalendarAndSetAnalyzedCalendar(String currStr) 
			throws ParseException, InvalidInputException {
		doesContainAmOrPm = false;
		isMondayToSunday = false;
		isTime = false;
		isDate = false;
		isKeywordDate = false;
		determineIfFormatError(currStr); //e.g. 13pm or 13 am etc

		if (doesContainAmOrPm) {
			isTime = parseIfTime12Format(currStr);
		} else {
			isTime = parseIfTime24Format(currStr);
			isDate = parseIfDateFormat(currStr);
			isKeywordDate = parseIfKeywordDateFormat(currStr);
		}
		throwParseExceptionIfFailsAllParsing();
	}

	private static void setCalendar() {
		if (isDate || isKeywordDate) {
			setDate();
		} else if (isTime) {
			setTime();
		}
	}

	private static void caseIfCalendarBeforeOrEqualToCurrentTime()
			throws InvalidInputException {
		if (startCalendar != null && startCalendar.compareTo(Calendar.getInstance()) != 1) { 
			LogHelper.info("Start time is before or equal to current time");
			throw new InvalidInputException(ERROR_START_TIME);
		} else if (endCalendar != null && endCalendar.compareTo(Calendar.getInstance()) != 1) {
			LogHelper.info("End time is before or equal to current time");
			throw new InvalidInputException(ERROR_END_TIME);
		}
	}

	private static void caseIfStartTimeLaterThanOrEqualToEndTime() throws InvalidInputException {
		if (doesStartAndEndCalendarExist() && isStartCalendarLaterThanOrEqualToEndCalendar()) {
			LogHelper.info("Start time is later than or equal to end time");
			throw new InvalidInputException(ERROR_START_TIME_LATER_THAN_OR_EQUAL_TO_END_TIME);
		}
	}

	private static void caseIfStartAndEndCalendarShareOneDate() {
		if (doesStartAndEndCalendarExist() && !isEndDateSet 
				&& isStartCalendarLaterThanOrEqualToEndCalendar()) {
			//e.g. 1/1/2100 5pm 6pm
			endCalendar.set(Calendar.YEAR, startCalendar.get(Calendar.YEAR));
			endCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH));
			endCalendar.set(Calendar.DATE, startCalendar.get(Calendar.DATE));
		}
	}

	protected static boolean doesStartAndEndCalendarExist() {
		return startCalendar != null && endCalendar != null;
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
			LogHelper.info("End time is set: " + endCalendar.getTime().toString());
		} else if (!isStartTimeSet) {
			setStartTime();
			LogHelper.info("Start time is set: " + startCalendar.getTime().toString());
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
		//if isStartTimeSet is true implies that the date is placed before the start time
		if (!isEndDateSet && !isStartTimeSet) { 
			setEndDate();
			LogHelper.info("End date is set: " + endCalendar.getTime().toString());
		} else if (!isStartDateSet) {
			setStartDate();
			LogHelper.info("Start date is set: " + startCalendar.getTime().toString());
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

	private static boolean parseIfKeywordDateFormat(String currStr) throws InvalidInputException {
		Calendar currTime = Calendar.getInstance(); //get current Calendar
		int addValue = -1;

		if (isToday(currStr)) {
			isMondayToSunday = false;
			addValue = 0;
		} else if (isTomorrow(currStr)) {
			isMondayToSunday = false;
			addValue = 1;
		} else if (isEveryday(currStr)) {
			addValue = setAsEverydayIfNoEndDateIsSet(addValue);
		} else if (isMonday(currStr)) {
			isMondayToSunday = true;
			addValue = (Calendar.MONDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7;
		} else if (isTuesday(currStr)) {
			isMondayToSunday = true;
			addValue = (Calendar.TUESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7;
		} else if (isWednesday(currStr)) {
			isMondayToSunday = true;
			addValue = (Calendar.WEDNESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7;
		} else if (isThursday(currStr)) {
			isMondayToSunday = true;
			addValue = (Calendar.THURSDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7;
		} else if (isFriday(currStr)) {
			isMondayToSunday = true;
			addValue = (Calendar.FRIDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7;
		} else if (isSaturday(currStr)) {
			isMondayToSunday = true;
			addValue = (Calendar.SATURDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7;
		} else if (isSunday(currStr)) {
			isMondayToSunday = true;
			addValue = (Calendar.SUNDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7;
		} else { //not calendar keyword
			return false;
		}

		analyzedCalendar = new GregorianCalendar();
		analyzedCalendar.add(GregorianCalendar.DAY_OF_WEEK, addValue);

		return true;
	}

	private static int setAsEverydayIfNoEndDateIsSet(int addValue) throws InvalidInputException {
		if (!isEndDateSet) {
			isMondayToSunday = false;
			addValue = 0;
			isRecurringEveryday = true;
			recurringPeriod = EVERYDAY;
		} else {
			throw new InvalidInputException(ERROR_DAILY_RECURRING_ONE_DATE_ONLY);
		}
		return addValue;
	}

	private static boolean parseIfTime12Format(String currStr) {
		Date date;
		for (int i = 0; i < time12Format.length; i++) { //check if time format
			try {
				date = time12Format[i].parse(currStr);
				setAnalyzedCalendarWithDateParsed(date);
				LogHelper.info("'" + currStr + "'" + " is 12 hour format");
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
				setAnalyzedCalendarWithDateParsed(date);
				LogHelper.info("'" + currStr + "'" + "is 24 hour format");
				return true;
			} catch (ParseException e) {}
		}
		return false;
	}

	private static boolean parseIfDateFormat(String currStr) {
		Date date;
		for (int i = 0; i < dateFormat.length; i++) { //check if date format
			try {
				date = dateFormat[i].parse(currStr);
				setAnalyzedCalendarWithDateParsed(date);
				parseInto4DigitsYearIf2DigitsYearIsGiven();
				parseIntoCurrentYearIfNoYearInfoIsGiven();
				LogHelper.info("'" + currStr + "'" + "is date format");
				return true;
			} catch (ParseException e) {}
		}
		return false;
	}

	private static void setAnalyzedCalendarWithDateParsed(Date date) {
		analyzedCalendar = new GregorianCalendar();
		analyzedCalendar.setTime(date);
	}

	private static void parseInto4DigitsYearIf2DigitsYearIsGiven() {
		if (analyzedCalendar.get(Calendar.YEAR) < 100) { //two digits year is given
			analyzedCalendar.add(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR)/100*100);
		}
	}

	private static void parseIntoCurrentYearIfNoYearInfoIsGiven() {
		if (analyzedCalendar.get(Calendar.YEAR) == 1970) { //calendar provided without year
			analyzedCalendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
		}
	}

	private static void determineIfFormatError(String currStr) throws ParseException {
		if (currStr.toLowerCase().contains(AM) || currStr.toLowerCase().contains(PM)) {
			doesContainAmOrPm = true;
			if (!doesContainSpecialNotation(currStr)) {
				String strAfterRemovingAmOrPm = currStr.substring(0,currStr.length() - 2);
				try {
					if (Integer.parseInt(strAfterRemovingAmOrPm) > 12 
							&& Integer.parseInt(strAfterRemovingAmOrPm) < 100) {
						LogHelper.info("'" + currStr + "'" + " is invalid hour format");
						throw new ParseException(EMPTY, 0); //handle case e.g. 13pm to 99pm
					}
				}
				catch(NumberFormatException nfe) {}
			}
		}
	}

	private static boolean doesContainSpecialNotation(String currStr) {
		return currStr.contains(DOT) && currStr.contains(COLON) 
				&& currStr.contains(SLASH) && currStr.contains(DASH);
	}

	private static void throwParseExceptionIfFailsAllParsing() throws ParseException {
		if (hasFailedAllParsing()) { // all nFail
			throw new ParseException(EMPTY, 0);
		}
	}

	private static boolean hasFailedAllParsing() {
		return !isTime && !isDate && !isKeywordDate;
	}

	private static boolean isStartCalendarLaterThanOrEqualToEndCalendar() {
		return startCalendar.compareTo(endCalendar) != -1;
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

	private static boolean isToday(String currStr) {
		return currStr.equalsIgnoreCase(TODAY) || currStr.equalsIgnoreCase(TODAY_SHORT);
	}

	private static boolean isTomorrow(String currStr) {
		return currStr.equalsIgnoreCase(TOMORROW) || currStr.equalsIgnoreCase(TOMORROW_SHORT);
	}

	private static boolean isEveryday(String currStr) {
		return currStr.equalsIgnoreCase(EVERYDAY);
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
