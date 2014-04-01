package clc.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import clc.common.InvalidInputException;
import static clc.common.Constants.*;

public class TimeParser extends Analyzer {
	private static SimpleDateFormat[] dateFormat, time12Format, time24Format;
	private static boolean doesContainAmOrPm;
	private static boolean isDate, isKeywordDate, isTime;
	protected static boolean isStartDateSet, isStartTimeSet, isEndDateSet, isEndTimeSet;
	private static GregorianCalendar analyzedCalendar;
	protected static GregorianCalendar startCalendar, endCalendar;
	protected static String[] infoToBeProcessed = null;
	protected static int startCalendarIndex, endCalendarIndex;

	protected TimeParser(String input) {
		super(input);
	}

	protected static void processCalendarInfo() throws InvalidInputException {
		initializeVariable();
		int currIndex = infoToBeProcessed.length - 1;
		
		while (currIndex >= 0 && !hasAllTimeSet()) {
			String toBeAnalyzedString = "";
			
			for (int i = 0; i < 3; i ++) { // calendar at most represent by 3 Strings
				toBeAnalyzedString = infoToBeProcessed[currIndex --] + toBeAnalyzedString;
				try {
					analyzeTime(toBeAnalyzedString);
					if (endCalendarIndex == -1) {
						startCalendarIndex = currIndex + 1; // due to currIndex --
						endCalendarIndex = currIndex + 1;
					} else if (startCalendarIndex == -1 || currIndex + 1 < startCalendarIndex) {
						startCalendarIndex = currIndex + 1;
					}
					break;
				} catch (ParseException e) { //catch parsing error
					if (currIndex < 0) {
						break;
					}
				}
			}
			setCalendar();
			System.out.println(isTime + " " + isDate + " " + isKeywordDate);
		}
		
		setStartCalendarAsNullIfNotSet();
		setEndCalendarAsNullIfNotSet();
		caseIfCalendarBeforeCurrentTime();
		caseIfStartAndEndCalendarShareOneDate();
		caseIfStartTimeLaterThanEndTime();

		//if (startCalendar != null) System.out.println("START: " + startCalendar.getTime().toString());
		//if (endCalendar != null) System.out.println("END: " + endCalendar.getTime().toString());
		//if (startCalendar == null && endCalendar == null) System.out.println("NO VALID CALENDAR");
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

	private static void setCalendar() {
		if (isDate || isKeywordDate) {
			setDate();
		} else if (isTime) {
			setTime();
		}
	}

	private static void caseIfCalendarBeforeCurrentTime()
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
			endCalendar.set(Calendar.YEAR, startCalendar.get(Calendar.YEAR));
			endCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH));
			endCalendar.set(Calendar.DATE, startCalendar.get(Calendar.DATE));
		}
	}

	private static void setStartCalendarAsNullIfNotSet() {
		if (!isStartTimeSet && !isStartDateSet) {
			startCalendar = null;
		}
	}

	private static void setEndCalendarAsNullIfNotSet() {
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
		boolean isCaseToday = false;
		int addValue = -1;

		if (currStr.equalsIgnoreCase(TODAY) || currStr.equalsIgnoreCase(TODAY_SHORT)) {
			isCaseToday = true;
			addValue = 0;
		} else if (currStr.equalsIgnoreCase(TOMORROW) || currStr.equalsIgnoreCase(TOMORROW_SHORT)) {
			addValue = 1;
		} else if (currStr.equalsIgnoreCase(MONDAY) || currStr.equalsIgnoreCase(MONDAY_SHORT)) {
			addValue = Calendar.MONDAY - currTime.get(Calendar.DAY_OF_WEEK);
		} else if (currStr.equalsIgnoreCase(TUESDAY) || currStr.equalsIgnoreCase(TUESDAY_SHORT)) {
			addValue = Calendar.TUESDAY - currTime.get(Calendar.DAY_OF_WEEK);
		} else if (currStr.equalsIgnoreCase(WEDNESDAY) || currStr.equalsIgnoreCase(WEDNESDAY_SHORT)) {
			addValue = Calendar.WEDNESDAY - currTime.get(Calendar.DAY_OF_WEEK);
		} else if (currStr.equalsIgnoreCase(THURSDAY) || currStr.equalsIgnoreCase(THURSDAY_SHORT)) {
			addValue = Calendar.THURSDAY - currTime.get(Calendar.DAY_OF_WEEK);
		} else if (currStr.equalsIgnoreCase(FRIDAY) || currStr.equalsIgnoreCase(FRIDAY_SHORT)) {
			addValue = Calendar.FRIDAY - currTime.get(Calendar.DAY_OF_WEEK);
		} else if (currStr.equalsIgnoreCase(SATURDAY) || currStr.equalsIgnoreCase(SATURDAY_SHORT)) {
			addValue = Calendar.SATURDAY - currTime.get(Calendar.DAY_OF_WEEK);
		} else if (currStr.equalsIgnoreCase(SUNDAY) || currStr.equalsIgnoreCase(SUNDAY_SHORT)) {
			addValue = Calendar.SUNDAY - currTime.get(Calendar.DAY_OF_WEEK);
		} else {
			return false;
		}

		if (!isCaseToday && addValue <= 0) { // day before today will get negative value
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
			if (!currStr.contains(".") && !currStr.contains(":") && !currStr.contains("/") && !currStr.contains("-")) {
				//remove am or pm
				String firstTwoChar = currStr.substring(0,currStr.length() - 2).trim(); //firstTwoCharacter
				try {
					if (Integer.parseInt(firstTwoChar) > 12 && Integer.parseInt(firstTwoChar) < 100) {
						throw new ParseException("", 0); //handle this e.g. 13pm
					}
				}
				catch(NumberFormatException nfe) {}
			}
		}
	}

	private static void determineIfFailsAllParsing() throws ParseException {
		if (hasFailedAllParsing()) { // all nFail
			throw new ParseException("", 0);
		}
	}

	private static boolean hasFailedAllParsing() {
		return !isTime && !isDate && !isKeywordDate; //not time nor date / not supported
	}

	private static boolean isStartCalendarLaterThanEndCalendar() {
		return startCalendar.compareTo(endCalendar) == 1;
	}

	private static boolean hasAllTimeSet() {
		return isStartDateSet && isStartTimeSet
				&& isEndDateSet && isEndTimeSet;
	}
	
	protected static boolean doesContainTimeInfo() {
		return isStartDateSet || isStartTimeSet 
				|| isEndDateSet || isEndTimeSet;
	}
	
	protected static boolean isCaseTimedTask() {
		return startCalendar != null && endCalendar != null;
	}
	
	protected static boolean isCaseDeadlineTask() {
		return startCalendar == null && endCalendar != null;
	}
	
	protected static ArrayList<GregorianCalendar> getCalendar() {
		ArrayList<GregorianCalendar> time = new ArrayList<GregorianCalendar>();
		time.add(startCalendar);
		time.add(endCalendar);
		return time;
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