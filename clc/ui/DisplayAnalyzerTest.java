package clc.ui;

import static org.junit.Assert.*;
import static clc.common.Constants.ERROR_INVALID_DISPLAY_REQUEST;
import static clc.common.Constants.ERROR_START_TIME_LATER_THAN_OR_EQUAL_TO_END_TIME;
import static clc.common.Constants.ERROR_END_TIME;
import static clc.common.Constants.TODAY;
import static clc.common.Constants.TOMORROW;
import static clc.common.Constants.MONDAY;
import static clc.common.Constants.TUESDAY;
import static clc.common.Constants.WEDNESDAY;
import static clc.common.Constants.THURSDAY;
import static clc.common.Constants.FRIDAY;
import static clc.common.Constants.SATURDAY;
import static clc.common.Constants.SUNDAY;
import static clc.common.Constants.THIS_WEEK;
import static clc.common.Constants.THIS_MONTH;
import static clc.common.Constants.NEXT_WEEK;
import static clc.common.Constants.NEXT_MONTH;
import static clc.common.Constants.ALL;
import static clc.common.Constants.NEXT;
import static clc.common.Constants.SPACE;
import static clc.common.Constants.TIMED_TASK;
import static clc.common.Constants.DEADLINE_TASK;
import static clc.common.Constants.FLOATING_TASK;
import static clc.common.Constants.ERROR_DISPLAY_WITH_RECCURING_TIME;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import clc.common.InvalidInputException;

public class DisplayAnalyzerTest {


	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws InvalidInputException {
		//display all
		//case1 empty details
		Analyzer.analyze("display");

		DisplayAnalyzer.analyze();
		assertEquals("", DisplayAnalyzer.getDisplayQuery());

		//case2 with "all" keyword
		Analyzer.analyze("display all");

		DisplayAnalyzer.analyze();
		assertEquals(ALL, DisplayAnalyzer.getDisplayQuery());


		//display timed task
		Analyzer.analyze("display timed tasks");

		DisplayAnalyzer.analyze();
		assertEquals(TIMED_TASK, DisplayAnalyzer.getDisplayQuery());


		//display deadline task
		Analyzer.analyze("display deadline tasks");

		DisplayAnalyzer.analyze();
		assertEquals(DEADLINE_TASK, DisplayAnalyzer.getDisplayQuery());


		//display floating task
		Analyzer.analyze("display floating tasks");

		DisplayAnalyzer.analyze();
		assertEquals(FLOATING_TASK, DisplayAnalyzer.getDisplayQuery());


		//display today
		Analyzer.analyze("display today");

		DisplayAnalyzer.analyze();
		assertEquals(TODAY, DisplayAnalyzer.getDisplayQuery());
		ArrayList<GregorianCalendar> time = DisplayAnalyzer.getCalendar();
		GregorianCalendar gc = new GregorianCalendar();
		//millisecond difference is minor for our product, use to String() to compare current time
		assertEquals(gc.getTime().toString(), time.get(0).getTime().toString());
		gc.add(Calendar.DATE, 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display tomorrow
		GregorianCalendar currTime = new GregorianCalendar();
		Analyzer.analyze("display tomorrow");

		DisplayAnalyzer.analyze();
		assertEquals(TOMORROW, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar(currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH), currTime.get(Calendar.DATE) + 1, 0, 0);
		assertEquals(gc, time.get(0));
		gc.add(Calendar.DATE, 1);
		assertEquals(gc, time.get(1));


		//display this week
		Analyzer.analyze("display this week");

		DisplayAnalyzer.analyze();
		assertEquals(THIS_WEEK, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		assertEquals(gc.getTime().toString(), time.get(0).getTime().toString());
		gc.add(Calendar.DATE, (Calendar.MONDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display next week
		Analyzer.analyze("display next week");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT_WEEK, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.MONDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.MONDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));

		//display coming week with multiple "next"
		Analyzer.analyze("display next next next week");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + NEXT + SPACE + NEXT_WEEK, DisplayAnalyzer.getDisplayQuery());

		//display this month
		Analyzer.analyze("display this month");

		DisplayAnalyzer.analyze();
		assertEquals(THIS_MONTH, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		assertEquals(gc.getTime().toString(), time.get(0).getTime().toString());
		gc.add(Calendar.MONTH, 1);
		gc.set(Calendar.DATE, 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display next month
		Analyzer.analyze("display next month");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT_MONTH, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar(currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH) + 1, 1);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH) + 2, 1);
		assertEquals(gc, time.get(1));


		//display Monday
		Analyzer.analyze("display monday");

		DisplayAnalyzer.analyze();
		assertEquals(MONDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.MONDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.TUESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));

		Analyzer.analyze("display mon");

		DisplayAnalyzer.analyze();
		assertEquals(MONDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.MONDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.MONDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display Tuesday
		Analyzer.analyze("display tuesday");

		DisplayAnalyzer.analyze();
		assertEquals(TUESDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.TUESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.TUESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));

		Analyzer.analyze("display tue");

		DisplayAnalyzer.analyze();
		assertEquals(TUESDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.TUESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.TUESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display Wednesday
		Analyzer.analyze("display wednesday");

		DisplayAnalyzer.analyze();
		assertEquals(WEDNESDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.WEDNESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.WEDNESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));

		Analyzer.analyze("display wed");

		DisplayAnalyzer.analyze();
		assertEquals(WEDNESDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.WEDNESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.WEDNESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display Thursday
		Analyzer.analyze("display thursday");

		DisplayAnalyzer.analyze();
		assertEquals(THURSDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.THURSDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.THURSDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));

		Analyzer.analyze("display thu");

		DisplayAnalyzer.analyze();
		assertEquals(THURSDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.THURSDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.THURSDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display Friday
		Analyzer.analyze("display friday");

		DisplayAnalyzer.analyze();
		assertEquals(FRIDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.FRIDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.FRIDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		Analyzer.analyze("display fri");

		DisplayAnalyzer.analyze();
		assertEquals(FRIDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.FRIDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.FRIDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display Saturday
		Analyzer.analyze("display saturday");

		DisplayAnalyzer.analyze();
		assertEquals(SATURDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.SATURDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.SATURDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));

		Analyzer.analyze("display sat");

		DisplayAnalyzer.analyze();
		assertEquals(SATURDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.SATURDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.SATURDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display Sunday
		Analyzer.analyze("display sunday");

		DisplayAnalyzer.analyze();
		assertEquals(SUNDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.SUNDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.SUNDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));

		Analyzer.analyze("display sun");

		DisplayAnalyzer.analyze();
		assertEquals(SUNDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.SUNDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.SUNDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));

		//display next Monday
		Analyzer.analyze("display next Monday");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + MONDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.MONDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.MONDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 8);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display Tuesday
		Analyzer.analyze("display NEXT NEXT tUESDAY");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + NEXT + SPACE + TUESDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.TUESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 14);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.TUESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 15);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display Wednesday
		Analyzer.analyze("display nExT NeXt NEXT WEDNESDAY");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + NEXT + SPACE + NEXT + SPACE + WEDNESDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.WEDNESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 21);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.WEDNESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 22);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display Thursday
		Analyzer.analyze("display NEXT ThUrSdAy");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + THURSDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.THURSDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.THURSDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 8);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display Friday
		Analyzer.analyze("display next Next fRiDaY");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + NEXT + SPACE + FRIDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.FRIDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 14);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.FRIDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 15);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display Saturday
		Analyzer.analyze("display nexT SATURday");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + SATURDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.SATURDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 7);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.SATURDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 8);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display Sunday
		Analyzer.analyze("display neXT NExt sunDAY");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + NEXT + SPACE + SUNDAY, DisplayAnalyzer.getDisplayQuery());
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.SUNDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 14);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.SUNDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 15);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));


		//display calendar by a specific time
		Analyzer.analyze("display 31/12 1159pm");

		DisplayAnalyzer.analyze();
		time = DisplayAnalyzer.getCalendar();
		assertEquals(null, time.get(0));
		gc = new GregorianCalendar(currTime.get(Calendar.YEAR), 11, 31, 23, 59);
		assertEquals(gc, time.get(1));


		//display calendar in a specific period of time
		Analyzer.analyze("display 30/12 1159pm 31/12 1159pm");

		DisplayAnalyzer.analyze();
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar(currTime.get(Calendar.YEAR), 11, 30, 23, 59);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar(currTime.get(Calendar.YEAR), 11, 31, 23, 59);
		assertEquals(gc, time.get(1));


		//display calendar by a specific time
		Analyzer.analyze("display 1159pm sunday");

		DisplayAnalyzer.analyze();
		time = DisplayAnalyzer.getCalendar();
		assertEquals(null, time.get(0));
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.SUNDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc.set(Calendar.HOUR_OF_DAY, 23);
		gc.set(Calendar.MINUTE, 59);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));

		
		//display calendar in a specific period of time with keyword Monday to Sunday
		Analyzer.analyze("display from 12am monday to 1159pm next sunday");

		DisplayAnalyzer.analyze();
		time = DisplayAnalyzer.getCalendar();
		gc = new GregorianCalendar(currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH), currTime.get(Calendar.DATE), 0, 0);
		gc.add(Calendar.DATE, (Calendar.MONDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar(currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH), currTime.get(Calendar.DATE), 23, 59);
		gc.add(Calendar.DATE, (Calendar.SUNDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 7);
		assertEquals(gc, time.get(1));
	}

	@Test
	public void testInvalidInputException() throws InvalidInputException {
		//display unsupported query
		Analyzer.analyze("display /oj1289asdf;l@#$");

		try {
			DisplayAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_INVALID_DISPLAY_REQUEST, e.getMessage());
		}

		//display date before current time
		Analyzer.analyze("display 1/1/2010 12am");

		try {
			DisplayAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_END_TIME, e.getMessage());
		}

		//display period that end time before start time
		Analyzer.analyze("display 1159pm 1158pm");

		try {
			DisplayAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_START_TIME_LATER_THAN_OR_EQUAL_TO_END_TIME, e.getMessage());
		}
		
		//display with everyday
		Analyzer.analyze("display everyday");
		
		try {
			DisplayAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_DISPLAY_WITH_RECCURING_TIME, e.getMessage());
		}
		
		//display with every Monday ~ Sunday
		Analyzer.analyze("display every mon");
		
		try {
			DisplayAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_DISPLAY_WITH_RECCURING_TIME, e.getMessage());
		}
	}
}
