package clc.ui;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import clc.common.InvalidInputException;

public class TestingAddAnalyzer {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws InvalidInputException {

		//command details without date and time 
		Analyzer.analyze("add taskname");

		AddAnalyzer.analyze();
		assertEquals("taskname", AddAnalyzer.getToBeAddedTask().getTaskName());
		assertEquals(null, AddAnalyzer.getToBeAddedTask().getStartTime());
		assertEquals(null, AddAnalyzer.getToBeAddedTask().getEndTime());


		//command details with one time only
		Analyzer.analyze("add taskname1 1159pm");

		AddAnalyzer.analyze();
		GregorianCalendar gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59);
		assertEquals("taskname1", AddAnalyzer.getToBeAddedTask().getTaskName());
		assertEquals(null, AddAnalyzer.getToBeAddedTask().getStartTime());
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());


		//command details with one time and one date only
		//case 1: date before time
		Analyzer.analyze("add taskname2 1/1/2100 1159pm");

		AddAnalyzer.analyze();
		gc = new GregorianCalendar(2100, 0, 1, 23, 59); // month is 0 to 11
		assertEquals("taskname2", AddAnalyzer.getToBeAddedTask().getTaskName());
		assertEquals(null, AddAnalyzer.getToBeAddedTask().getStartTime());
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 2: time before date
		Analyzer.analyze("add taskname3 1159pm 1/1/2100");

		AddAnalyzer.analyze();
		gc = new GregorianCalendar(2100, 0, 1, 23, 59); // month is 0 to 11
		assertEquals("taskname3", AddAnalyzer.getToBeAddedTask().getTaskName());
		assertEquals(null, AddAnalyzer.getToBeAddedTask().getStartTime());
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());


		//command details with two time and one date only
		//case 1: date at the beginning of calendar info
		Analyzer.analyze("add taskname4 1/1/2100 1158pm 1159pm");

		AddAnalyzer.analyze();
		assertEquals("taskname4", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(2100, 0, 1, 23, 58); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(2100, 0, 1, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 2: date in between the time
		Analyzer.analyze("add taskname5 1158pm 1/1/2100 1159pm");

		AddAnalyzer.analyze();
		assertEquals("taskname5", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(2100, 0, 1, 23, 58); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(2100, 0, 1, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 3: date at the end
		Analyzer.analyze("add taskname6 1158pm 1159pm 1/1/2100");

		AddAnalyzer.analyze();
		assertEquals("taskname6", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(2100, 0, 1, 23, 58); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(2100, 0, 1, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//command details with two time and two date
		//case 1: date time date time
		Analyzer.analyze("add taskname7 1/1/2100 1159pm 2/1/2100 1159pm");

		AddAnalyzer.analyze();
		assertEquals("taskname7", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(2100, 0, 1, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(2100, 0, 2, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());
		
		//case 2: time date time date
		Analyzer.analyze("add taskname8 1159pm 1/1/2100 1159pm 2/1/2100");

		AddAnalyzer.analyze();
		assertEquals("taskname8", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(2100, 0, 1, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(2100, 0, 2, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());
		
		//case 3: date time time date
		Analyzer.analyze("add taskname9 1/1/2100 1159pm 1159pm 2/1/2100");

		AddAnalyzer.analyze();
		assertEquals("taskname9", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(2100, 0, 1, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(2100, 0, 2, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());
		
		//case 4: time date date time
		Analyzer.analyze("add taskname10 1159pm 1/1/2100 2/1/2100 1159pm");

		AddAnalyzer.analyze();
		assertEquals("taskname10", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(2100, 0, 1, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(2100, 0, 2, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());
		

		//command details with keyword "today" and "tomorrow"
		//similar to previous tests have 4 cases
		Analyzer.analyze("add taskname11 today 1159pm tomorrow 1159pm"); 

		AddAnalyzer.analyze();
		assertEquals("taskname11", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE) + 1, 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());
	}

	@Test
	public void testInvalidInputException() {
		//empty command details
		Analyzer.analyze("add");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals("ERROR: empty command details", e.getMessage());
		}

		//spaces command details
		Analyzer.analyze("add            ");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals("ERROR: empty command details", e.getMessage());
		}

		//command details without task name 
		Analyzer.analyze("add 2/2/2100 1159pm");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals("ERROR: no task name is given", e.getMessage());
		}

		//add with reminder for floating task
		Analyzer.analyze("add -r taskname");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals("ERROR: no reminder is allowed for floating task", e.getMessage());
		}

		//command details end time is earlier than start time
		Analyzer.analyze("add taskname 25/5/2100 4pm 22/5/2100 3pm");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals("ERROR: Start time is later than end time", e.getMessage());
		}		

		//enter a year before current year
		Analyzer.analyze("add taskname 25/5/2001 4pm");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			int currentYear = Calendar.getInstance().get(Calendar.YEAR); 
			assertEquals("ERROR: you have entered a year before " + currentYear, e.getMessage());
		}		

		//start time(or both) is earlier than current time
		Analyzer.analyze("add taskname 1/1/2014 4pm 1/1/2100 4pm");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals("ERROR: start time is a calendar before now", e.getMessage());
		}	

		//end time for deadline task is earlier than current time
		Analyzer.analyze("add taskname 1/1/2014 4pm");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals("ERROR: end time is a calendar before now", e.getMessage());
		}

		//no time(only date) is given
		Analyzer.analyze("add taskname 1/1/2100");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals("ERROR: no exact time is given", e.getMessage());
		}

	}
}
