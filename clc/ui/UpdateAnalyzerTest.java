//@author A0112089J

package clc.ui;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import clc.common.InvalidInputException;
import static clc.common.Constants.ERROR_NO_SEQUENCE_NUMBER;
import static clc.common.Constants.ERROR_NO_NEW_TASK_NAME;
import static clc.common.Constants.ERROR_EMPTY_COMMAND_DETAILS;

public class UpdateAnalyzerTest {

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

		UpdateAnalyzer.analyze();
		assertEquals(1, UpdateAnalyzer.getCalendarProvidedCase());
		ArrayList<GregorianCalendar> time = UpdateAnalyzer.getCalendar();
		assertEquals(null, time.get(0));
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 23);
		gc.set(Calendar.MINUTE, 59);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));
		

		//case update , D
		Analyzer.analyze("update 1 , 1/1/2100");

		UpdateAnalyzer.analyze();
		assertEquals(2, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		assertEquals(null, time.get(0));
		gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, 2100);
		gc.set(Calendar.MONTH, 0);
		gc.set(Calendar.DATE, 1);
		//as milliseconds difference is minor for our software, we compare with toString() for current time
		//might have difference in milliseconds that causes fail
		assertEquals(gc.getTime().toString(), time.get(1).getTime().toString());
		

		//case update , D T
		Analyzer.analyze("update 1 , 1/1/2100 1159pm");

		UpdateAnalyzer.analyze();
		assertEquals(3, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		assertEquals(null, time.get(0));
		gc = new GregorianCalendar(2100, 0, 1, 23, 59);
		assertEquals(gc, time.get(1));

		//case update T ,
		Analyzer.analyze("update 1 1159pm ,");

		UpdateAnalyzer.analyze();
		assertEquals(4, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 23);
		gc.set(Calendar.MINUTE, 59);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		assertEquals(null, time.get(1));

		//case update T , T
		Analyzer.analyze("update 1 1158pm , 1159pm");

		UpdateAnalyzer.analyze();
		assertEquals(5, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 23);
		gc.set(Calendar.MINUTE, 58);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc.set(Calendar.MINUTE, 59);
		assertEquals(gc, time.get(1));

		//case update T , D
		Analyzer.analyze("update 1 1159pm , 1/1/2100");

		UpdateAnalyzer.analyze();
		assertEquals(6, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 23);
		gc.set(Calendar.MINUTE, 59);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, 2100);
		gc.set(Calendar.MONTH, 0);
		gc.set(Calendar.DATE, 1);
		//might have difference in milliseconds that causes fail
		assertEquals(gc.getTime().toString(), time.get(1).getTime().toString());

		//case update T , D T
		Analyzer.analyze("update 1 1159pm , 1/1/2100 1159pm");

		UpdateAnalyzer.analyze();
		assertEquals(7, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 23);
		gc.set(Calendar.MINUTE, 59);
		gc.set(Calendar.SECOND, 0);
		//might have difference in milliseconds that causes fail
		assertEquals(gc.getTime().toString(), time.get(0).getTime().toString());
		gc = new GregorianCalendar(2100, 0, 1, 23, 59);
		assertEquals(gc, time.get(1));

		//case update D ,
		Analyzer.analyze("update 1 1/1/2100 ,");

		UpdateAnalyzer.analyze();
		assertEquals(8, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, 2100);
		gc.set(Calendar.MONTH, 0);
		gc.set(Calendar.DATE, 1);
		//might have difference in milliseconds that causes fail
		assertEquals(gc.getTime().toString(), time.get(0).getTime().toString());
		assertEquals(null, time.get(1));

		//case update D , T
		Analyzer.analyze("update 1 1/1/2100 , 1159pm");

		UpdateAnalyzer.analyze();
		assertEquals(9, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, 2100);
		gc.set(Calendar.MONTH, 0);
		gc.set(Calendar.DATE, 1);
		//might have difference in milliseconds that causes fail
		assertEquals(gc.getTime().toString(), time.get(0).getTime().toString());
		gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 23);
		gc.set(Calendar.MINUTE, 59);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));

		//case update D , D
		Analyzer.analyze("update 1 1/1/2100 , 2/1/2100");

		UpdateAnalyzer.analyze();
		assertEquals(10, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, 2100);
		gc.set(Calendar.MONTH, 0);
		gc.set(Calendar.DATE, 1);
		//might have difference in milliseconds that causes fail
		long millisecondsDifference = gc.getTimeInMillis() - time.get(0).getTimeInMillis();
		assertTrue(millisecondsDifference < 500);
		gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, 2100);
		gc.set(Calendar.MONTH, 0);
		gc.set(Calendar.DATE, 2);
		//might have difference in milliseconds that causes fail
		assertEquals(gc.getTime().toString(), time.get(1).getTime().toString());

		//case update D , D T
		Analyzer.analyze("update 1 1/1/2100 , 2/1/2100 1159pm");

		UpdateAnalyzer.analyze();
		assertEquals(11, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, 2100);
		gc.set(Calendar.MONTH, 0);
		gc.set(Calendar.DATE, 1);
		//might have difference in milliseconds that causes fail
		assertEquals(gc.getTime().toString(), time.get(0).getTime().toString());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar(2100, 0, 2, 23, 59);
		assertEquals(gc, time.get(1));

		//case update D T ,
		Analyzer.analyze("update 1 1/1/2100 1159pm ,");

		UpdateAnalyzer.analyze();
		assertEquals(12, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar(2100, 0, 1, 23, 59);
		assertEquals(gc, time.get(0));
		assertEquals(null, time.get(1));

		//case update D T , T
		Analyzer.analyze("update 1 1/1/2100 1159pm , 1159pm");

		UpdateAnalyzer.analyze();
		assertEquals(13, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar(2100, 0, 1, 23, 59);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 23);
		gc.set(Calendar.MINUTE, 59);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));

		//case update D T , D
		Analyzer.analyze("update 1 1/1/2100 1159pm , 2/1/2100");

		UpdateAnalyzer.analyze();
		assertEquals(14, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar(2100, 0, 1, 23, 59);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, 2100);
		gc.set(Calendar.MONTH, 0);
		gc.set(Calendar.DATE, 2);
		//might have difference in milliseconds that causes fail
		assertEquals(gc.getTime().toString(), time.get(1).getTime().toString());

		//case update D T , D T
		Analyzer.analyze("update 1 1/1/2100 1159pm , 2/1/2100 1159pm");

		UpdateAnalyzer.analyze();
		assertEquals(15, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar(2100, 0, 1, 23, 59);
		assertEquals(gc, time.get(0));
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar(2100, 0, 2, 23, 59);
		assertEquals(gc, time.get(1));

		/* 
		 * without comma, user CANNOT
		 * update start date only 
		 * OR update start time only 
		 * OR update start date and start time only
		 * OR update start time and end date only
		 * OR update start date and end time only
		 * OR update start date, start time and end date only
		 */

		//case update , T
		Analyzer.analyze("update 1 1159pm");

		UpdateAnalyzer.analyze();
		assertEquals(1, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		assertEquals(null, time.get(0));
		gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 23);
		gc.set(Calendar.MINUTE, 59);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(1));

		//case update , D
		Analyzer.analyze("update 1 1/1/2100");

		UpdateAnalyzer.analyze();
		assertEquals(2, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		assertEquals(null, time.get(0));
		gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, 2100);
		gc.set(Calendar.MONTH, 0);
		gc.set(Calendar.DATE, 1);
		//might have difference in milliseconds that causes fail
		assertEquals(gc.getTime().toString(), time.get(1).getTime().toString());

		//case update , D T
		Analyzer.analyze("update 1 1/1/2100 1159pm");

		UpdateAnalyzer.analyze();
		assertEquals(3, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		assertEquals(null, time.get(0));
		gc = new GregorianCalendar(2100, 0, 1, 23, 59);
		assertEquals(gc, time.get(1));

		//case update T , T
		Analyzer.analyze("update 1 1158pm 1159pm");

		UpdateAnalyzer.analyze();
		assertEquals(5, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 23);
		gc.set(Calendar.MINUTE, 58);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		assertEquals(gc, time.get(0));
		gc.set(Calendar.MINUTE, 59);
		assertEquals(gc, time.get(1));

		//case update T , D T
		Analyzer.analyze("update 1 1159pm 1/1/2100 1159pm");

		UpdateAnalyzer.analyze();
		assertEquals(7, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 23);
		gc.set(Calendar.MINUTE, 59);
		gc.set(Calendar.SECOND, 0);
		assertEquals(gc.getTime().toString(), time.get(0).getTime().toString());
		gc = new GregorianCalendar(2100, 0, 1, 23, 59);
		assertEquals(gc, time.get(1));


		//case update D , D
		Analyzer.analyze("update 1 1/1/2100 2/1/2100");

		UpdateAnalyzer.analyze();
		assertEquals(10, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, 2100);
		gc.set(Calendar.MONTH, 0);
		gc.set(Calendar.DATE, 1);
		//might have difference in milliseconds that causes fail
		assertEquals(gc.getTime().toString(), time.get(0).getTime().toString());
		gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, 2100);
		gc.set(Calendar.MONTH, 0);
		gc.set(Calendar.DATE, 2);
		//might have difference in milliseconds that causes fail
		assertEquals(gc.getTime().toString(), time.get(1).getTime().toString());

		//case update D , D T
		Analyzer.analyze("update 1 1/1/2100 2/1/2100 1159pm");

		UpdateAnalyzer.analyze();
		assertEquals(11, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, 2100);
		gc.set(Calendar.MONTH, 0);
		gc.set(Calendar.DATE, 1);
		//might have difference in milliseconds that causes fail
		assertEquals(gc.getTime().toString(), time.get(0).getTime().toString());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar(2100, 0, 2, 23, 59);
		assertEquals(gc, time.get(1));

		//case update D T , T
		Analyzer.analyze("update 1 1/1/2100 1159pm 1159pm");

		UpdateAnalyzer.analyze();
		assertEquals(13, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar(2100, 0, 1, 23, 59);
		assertEquals(gc, time.get(0));
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59);
		assertEquals(gc, time.get(1));

		//case update D T , D T
		Analyzer.analyze("update 1 1/1/2100 1159pm 2/1/2100 1159pm");

		UpdateAnalyzer.analyze();
		assertEquals(15, UpdateAnalyzer.getCalendarProvidedCase());
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar(2100, 0, 1, 23, 59);
		assertEquals(gc, time.get(0));
		time = UpdateAnalyzer.getCalendar();
		gc = new GregorianCalendar(2100, 0, 2, 23, 59);
		assertEquals(gc, time.get(1));
	}

	@Test
	public void test() throws InvalidInputException {
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
	}
}
