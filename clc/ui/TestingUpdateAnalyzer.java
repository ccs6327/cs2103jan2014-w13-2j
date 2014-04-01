package clc.ui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import clc.common.InvalidInputException;

import static clc.common.Constants.ERROR_NO_COMMA;
import static clc.common.Constants.ERROR_NO_SEQUENCE_NUMBER;
import static clc.common.Constants.ERROR_NO_NEW_TASK_NAME;
import static clc.common.Constants.ERROR_EMPTY_COMMAND_DETAILS;

public class TestingUpdateAnalyzer {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCalendarProvided() throws InvalidInputException {
		/*
		 * D for Date
		 * T for Time
		 */
		
		//case update , T
		Analyzer.analyze("update 1 , 1159pm");
		System.out.println("x");
		UpdateAnalyzer.analyze();
		assertEquals(1, UpdateAnalyzer.getCalendarProvidedCase());
		
		//case update , D
		Analyzer.analyze("update 1 , 1/1/2100");

		System.out.println("x");
		UpdateAnalyzer.analyze();
		assertEquals(2, UpdateAnalyzer.getCalendarProvidedCase());
		
		//case update , D T
		Analyzer.analyze("update 1 , 1/1/2100 1159pm");

		System.out.println("x");
		UpdateAnalyzer.analyze();
		assertEquals(3, UpdateAnalyzer.getCalendarProvidedCase());
		
		//case update T ,
		Analyzer.analyze("update 1 1159pm ,");

		System.out.println("x");
		UpdateAnalyzer.analyze();
		assertEquals(4, UpdateAnalyzer.getCalendarProvidedCase());
		
		//case update T , T
		Analyzer.analyze("update 1 1158pm , 1159pm");

		System.out.println("x");
		UpdateAnalyzer.analyze();
		assertEquals(5, UpdateAnalyzer.getCalendarProvidedCase());
		
		//case update T , D
		Analyzer.analyze("update 1 1159pm , 1/1/2100");

		System.out.println("x");
		UpdateAnalyzer.analyze();
		assertEquals(6, UpdateAnalyzer.getCalendarProvidedCase());
		
		//case update T , D T
		Analyzer.analyze("update 1 1159pm , 1/1/2100 1159pm");
		
		UpdateAnalyzer.analyze();
		assertEquals(7, UpdateAnalyzer.getCalendarProvidedCase());
		
		//case update D ,
		Analyzer.analyze("update 1 1/1/2100 ,");
		
		UpdateAnalyzer.analyze();
		assertEquals(8, UpdateAnalyzer.getCalendarProvidedCase());
		
		//case update D , T
		Analyzer.analyze("update 1 1/1/2100 , 1159pm");
		
		UpdateAnalyzer.analyze();
		assertEquals(9, UpdateAnalyzer.getCalendarProvidedCase());
		
		//case update D , D
		Analyzer.analyze("update 1 1/1/2100 , 2/1/2100");
		
		UpdateAnalyzer.analyze();
		assertEquals(10, UpdateAnalyzer.getCalendarProvidedCase());
		
		//case update D , D T
		Analyzer.analyze("update 1 1/1/2100 , 2/1/2100 1159pm");
		
		UpdateAnalyzer.analyze();
		assertEquals(11, UpdateAnalyzer.getCalendarProvidedCase());
		
		//case update D T ,
		Analyzer.analyze("update 1 1/1/2100 1159pm ,");
		
		UpdateAnalyzer.analyze();
		assertEquals(12, UpdateAnalyzer.getCalendarProvidedCase());
		
		//case update D T , T
		Analyzer.analyze("update 1 1/1/2100 1159pm , 1159pm");
		
		UpdateAnalyzer.analyze();
		assertEquals(13, UpdateAnalyzer.getCalendarProvidedCase());
		
		//case update D T , D
		Analyzer.analyze("update 1 1/1/2100 1159pm , 2/1/2100");
		
		UpdateAnalyzer.analyze();
		assertEquals(14, UpdateAnalyzer.getCalendarProvidedCase());
		
		//case update D T , D T
		Analyzer.analyze("update 1 1/1/2100 1159pm , 2/1/2100 1159pm");
		
		UpdateAnalyzer.analyze();
		assertEquals(15, UpdateAnalyzer.getCalendarProvidedCase());
	}
	
	@Test
	public void test() {
		//no details
		Analyzer.analyze("update");
		
		try {
			UpdateAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_EMPTY_COMMAND_DETAILS, e.getMessage());
		}
		
		
		//spaces details
		Analyzer.analyze("update           ");
		
		try {
			UpdateAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_EMPTY_COMMAND_DETAILS, e.getMessage());
		}
		
		
		//no sequence number
		//case1 update task name
		Analyzer.analyze("update taskname");

		try {
			UpdateAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_NO_SEQUENCE_NUMBER, e.getMessage());
		}
		
		//case 2 update calendar
		Analyzer.analyze("update 12/12/2112 4pm ,");

		try {
			UpdateAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_NO_SEQUENCE_NUMBER, e.getMessage());
		}
		
		
		//no task name **CALENDAR CASE IS CONSIDERED BY doesContainTimeInfo() ALREADY
		Analyzer.analyze("update 1");

		try {
			UpdateAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_NO_NEW_TASK_NAME, e.getMessage());
		}
		
		
		//no comma in calendar details
		Analyzer.analyze("update 1 4pm 5pm");

		try {
			UpdateAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_NO_COMMA, e.getMessage());
		}
	}
}
