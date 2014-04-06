package clc.ui;

import static org.junit.Assert.*;

import static clc.common.Constants.ERROR_INVALID_DISPLAY_REQUEST;
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


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import clc.common.InvalidInputException;

public class TestingDisplayAnalyzer {

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


		//display today
		Analyzer.analyze("display today");

		DisplayAnalyzer.analyze();
		assertEquals(TODAY, DisplayAnalyzer.getDisplayQuery());


		//display tomorrow
		Analyzer.analyze("display tomorrow");

		DisplayAnalyzer.analyze();
		assertEquals(TOMORROW, DisplayAnalyzer.getDisplayQuery());


		//display this week
		Analyzer.analyze("display this week");

		DisplayAnalyzer.analyze();
		assertEquals(THIS_WEEK, DisplayAnalyzer.getDisplayQuery());


		//display next week
		Analyzer.analyze("display next week");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT_WEEK, DisplayAnalyzer.getDisplayQuery());

		//display coming week with multiple "next"
		Analyzer.analyze("display next next next week");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + NEXT + SPACE + NEXT_WEEK, DisplayAnalyzer.getDisplayQuery());

		//display this month
		Analyzer.analyze("display this month");

		DisplayAnalyzer.analyze();
		assertEquals(THIS_MONTH, DisplayAnalyzer.getDisplayQuery());


		//display next month
		Analyzer.analyze("display next month");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT_MONTH, DisplayAnalyzer.getDisplayQuery());


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


		//display Monday
		Analyzer.analyze("display monday");

		DisplayAnalyzer.analyze();
		assertEquals(MONDAY, DisplayAnalyzer.getDisplayQuery());

		Analyzer.analyze("display mon");

		DisplayAnalyzer.analyze();
		assertEquals(MONDAY, DisplayAnalyzer.getDisplayQuery());


		//display Tuesday
		Analyzer.analyze("display tuesday");

		DisplayAnalyzer.analyze();
		assertEquals(TUESDAY, DisplayAnalyzer.getDisplayQuery());

		Analyzer.analyze("display tue");

		DisplayAnalyzer.analyze();
		assertEquals(TUESDAY, DisplayAnalyzer.getDisplayQuery());


		//display Wednesday
		Analyzer.analyze("display wednesday");

		DisplayAnalyzer.analyze();
		assertEquals(WEDNESDAY, DisplayAnalyzer.getDisplayQuery());

		Analyzer.analyze("display wed");

		DisplayAnalyzer.analyze();
		assertEquals(WEDNESDAY, DisplayAnalyzer.getDisplayQuery());


		//display Thursday
		Analyzer.analyze("display thursday");

		DisplayAnalyzer.analyze();
		assertEquals(THURSDAY, DisplayAnalyzer.getDisplayQuery());

		Analyzer.analyze("display thu");

		DisplayAnalyzer.analyze();
		assertEquals(THURSDAY, DisplayAnalyzer.getDisplayQuery());


		//display Friday
		Analyzer.analyze("display friday");

		DisplayAnalyzer.analyze();
		assertEquals(FRIDAY, DisplayAnalyzer.getDisplayQuery());

		Analyzer.analyze("display fri");

		DisplayAnalyzer.analyze();
		assertEquals(FRIDAY, DisplayAnalyzer.getDisplayQuery());


		//display Saturday
		Analyzer.analyze("display saturday");

		DisplayAnalyzer.analyze();
		assertEquals(SATURDAY, DisplayAnalyzer.getDisplayQuery());

		Analyzer.analyze("display sat");

		DisplayAnalyzer.analyze();
		assertEquals(SATURDAY, DisplayAnalyzer.getDisplayQuery());


		//display Sunday
		Analyzer.analyze("display sunday");

		DisplayAnalyzer.analyze();
		assertEquals(SUNDAY, DisplayAnalyzer.getDisplayQuery());

		Analyzer.analyze("display sun");

		DisplayAnalyzer.analyze();
		assertEquals(SUNDAY, DisplayAnalyzer.getDisplayQuery());

		//display next Monday
		Analyzer.analyze("display next Monday");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + MONDAY, DisplayAnalyzer.getDisplayQuery());
		

		//display Tuesday
		Analyzer.analyze("display NEXT NEXT tUESDAY");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + NEXT + SPACE + TUESDAY, DisplayAnalyzer.getDisplayQuery());


		//display Wednesday
		Analyzer.analyze("display nExT NeXt NEXT WEDNESDAY");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + NEXT + SPACE + NEXT + SPACE + WEDNESDAY, DisplayAnalyzer.getDisplayQuery());


		//display Thursday
		Analyzer.analyze("display NEXT ThUrSdAy");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + THURSDAY, DisplayAnalyzer.getDisplayQuery());


		//display Friday
		Analyzer.analyze("display next Next fRiDaY");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + NEXT + SPACE + FRIDAY, DisplayAnalyzer.getDisplayQuery());


		//display Saturday
		Analyzer.analyze("display nexT SATURday");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + SATURDAY, DisplayAnalyzer.getDisplayQuery());


		//display Sunday
		Analyzer.analyze("display neXT NExt sunDAY");

		DisplayAnalyzer.analyze();
		assertEquals(NEXT + SPACE + NEXT + SPACE + SUNDAY, DisplayAnalyzer.getDisplayQuery());

		//display calendar **
	}

	@Test
	public void testInvalidInputException() {
		//display unsupported query
		Analyzer.analyze("display /oj1289asdf;l@#$");

		try {
			DisplayAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_INVALID_DISPLAY_REQUEST, e.getMessage());
		}

	}
}
