//@author A0105712U
/**
 * Command Line Calendar (CLC)
 * AddTest.java
 * 
 * This is JUnit test cases for Add.
 */
package clc.logic;

import static clc.common.Constants.*;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import clc.common.LogHelper;

public class AddTest {

	private static final String TEST_CASE_ALL_PASSED = "All test cases passed\r\n";
	private static final String TEST_CASE_THREE_PASSED = "Test case 3 passed";
	private static final String TEST_CASE_TWO_PASSED = "Test case 2 passed";
	private static final String TEST_CASE_ONE_PASSED = "Test case 1 passed";
	private static final long OFFSET = 600000;

	@Test
	public void test() {
		
		Calendar startTime = new GregorianCalendar();
		startTime.setTimeInMillis(System.currentTimeMillis() + OFFSET);
		Calendar endTime = new GregorianCalendar();
		endTime.setTimeInMillis(System.currentTimeMillis() + OFFSET);
		
		// Test case 1: add a timed task	
		Task timedTask = new Task(TIMED_TASK, startTime, endTime);
		Add add1 = new Add(timedTask);
		assertEquals(String.format(MESSAGE_TIMED_TASK_ADDED, timedTask.getTaskName(), formatDate(timedTask.getStartTime()), formatDate(timedTask.getEndTime())), add1.execute());
		LogHelper.info(TEST_CASE_ONE_PASSED);
		
		// Test case 2: add a deadline task	
		Task deadlineTask = new Task(DEADLINE_TASK, endTime);
		Add add2 = new Add(deadlineTask);
		assertEquals(String.format(MESSAGE_DEADLINE_TASK_ADDED, deadlineTask.getTaskName(), formatDate(deadlineTask.getEndTime())), add2.execute());
		LogHelper.info(TEST_CASE_TWO_PASSED);
		
		// Test case 3: add a floating task	
		Task floatingTask = new Task(FLOATING_TASK);
		Add add3 = new Add(floatingTask);
		assertEquals(String.format(MESSAGE_FLOATING_TASK_ADDED, floatingTask.getTaskName()), add3.execute());
		LogHelper.info(TEST_CASE_THREE_PASSED);
		
		LogHelper.info(TEST_CASE_ALL_PASSED);
		
	}

	private String formatDate(Calendar calendar) {
		String date;
		SimpleDateFormat dateFormat = new SimpleDateFormat(ADD_DATE_FORMAT);
		date = dateFormat.format(calendar.getTime());
		return date;
	}
}
