package clc.ui;

import static org.junit.Assert.*;

import static clc.common.Constants.ERROR_INVALID_DISPLAY_REQUEST;
import static clc.common.Constants.TODAY;
import static clc.common.Constants.TOMORROW;
import static clc.common.Constants.THIS_WEEK;
import static clc.common.Constants.THIS_MONTH;
import static clc.common.Constants.NEXT_WEEK;
import static clc.common.Constants.NEXT_MONTH;
import static clc.common.Constants.ALL;
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
		assertEquals(ALL, DisplayAnalyzer.getDisplayQuery());
		
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
