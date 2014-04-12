//@author A0112089J

package clc.ui;

import static org.junit.Assert.*;
import static clc.common.Constants.ERROR_NO_TASK_NAME;
import static clc.common.Constants.ERROR_REMINDER_FOR_FLOATING_TASK;
import static clc.common.Constants.ERROR_EMPTY_COMMAND_DETAILS;
import static clc.common.Constants.ERROR_START_TIME;
import static clc.common.Constants.ERROR_END_TIME;
import static clc.common.Constants.ERROR_START_TIME_LATER_THAN_OR_EQUAL_TO_END_TIME;
import static clc.common.Constants.ERROR_NO_EXACT_TIME;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import clc.common.InvalidInputException;

public class AddAnalyzerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetToBeAddedTask() throws InvalidInputException {

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
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 58);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		//assertEquals(gc.compareTo(AddAnalyzer.getToBeAddedTask().getStartTime()), 0);
		gc = new GregorianCalendar(2100, 0, 1, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 3: date at the end
		Analyzer.analyze("add taskname6 1158pm 1159pm 1/1/2100");

		AddAnalyzer.analyze();
		assertEquals("taskname6", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 58);
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
		Analyzer.analyze("add taskname 9 1/1/2100 1159pm 1159pm 2/1/2100");

		AddAnalyzer.analyze();
		assertEquals("taskname 9", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(2100, 0, 1, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(2100, 0, 2, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 4: time date date time
		Analyzer.analyze("add task name 10 1159pm 1/1/2100 2/1/2100 1159pm");

		AddAnalyzer.analyze();
		assertEquals("task name 10", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(2100, 0, 1, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(2100, 0, 2, 23, 59); // month is 0 to 11
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());


		//command details with keyword "today" and "tomorrow"
		//case 1 with full word today and tomorrow
		Analyzer.analyze("add task name 11 today 1159pm tomorrow 1159pm"); 

		AddAnalyzer.analyze();
		assertEquals("task name 11", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE) + 1, 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 2 with acronym of today and tomorrow
		Analyzer.analyze("add task name 11 tdy 1159pm tmr 1159pm"); 

		AddAnalyzer.analyze();
		assertEquals("task name 11", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE) + 1, 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//name after calendar information
		//case 1 timed task
		Analyzer.analyze("add from tomorrow 4pm to 5pm task name 12");

		AddAnalyzer.analyze();
		assertEquals("task name 12", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE) + 1, 16, 0);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 17, 0);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 2 deadline task
		Analyzer.analyze("add by tomorrow 4pm task name 12");

		AddAnalyzer.analyze();
		assertEquals("task name 12", AddAnalyzer.getToBeAddedTask().getTaskName());
		assertEquals(null, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE) + 1, 16, 0);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());


		//Monday to Sunday case + next Monday to Sunday case
		//case 1 timed task
		GregorianCalendar currTime = new GregorianCalendar();

		Analyzer.analyze("add task name 13 from monday 1159pm to next tuesday 1159pm");

		AddAnalyzer.analyze();
		assertEquals("task name 13", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.MONDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.TUESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE) + 7, 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());
		
		//case 2 deadline task
		Analyzer.analyze("add task name 14 by wednesday 1159pm");

		AddAnalyzer.analyze();
		assertEquals("task name 14", AddAnalyzer.getToBeAddedTask().getTaskName());
		assertEquals(null, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.WEDNESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), currTime.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 3 timed task with acronym
		Analyzer.analyze("add task name 15 from thu 1159pm to next next fri 1159pm");

		AddAnalyzer.analyze();
		assertEquals("task name 15", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.THURSDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.FRIDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE) + 14, 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 4 deadline with acronym
		Analyzer.analyze("add task name 16 at sat 1159pm");

		AddAnalyzer.analyze();
		assertEquals("task name 16", AddAnalyzer.getToBeAddedTask().getTaskName());
		assertEquals(null, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.SATURDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 5 timed task with full word and acronym
		Analyzer.analyze("add task name 17 from sunday 1159pm to next monday 1159pm");

		AddAnalyzer.analyze();
		assertEquals("task name 17", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.SUNDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar();
		gc.add(Calendar.DATE, (Calendar.MONDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE) + 7, 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());


		//everyday case, postpone to next day if before now
		//case 1 deadline task before now
		Analyzer.analyze("add task name 18 everyday 1201am");

		AddAnalyzer.analyze();
		assertEquals("task name 18", AddAnalyzer.getToBeAddedTask().getTaskName());
		assertEquals(null, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 0, 1);
		if (gc.compareTo(Calendar.getInstance()) == -1) {
			gc.add(Calendar.DATE, 1);
		}
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 2 deadline task after now
		Analyzer.analyze("add task name 19 everyday 1159pm");

		AddAnalyzer.analyze(); 
		assertEquals("task name 19", AddAnalyzer.getToBeAddedTask().getTaskName());
		assertEquals(null, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 3 timed task before now
		Analyzer.analyze("add task name 20 everyday 1201am to 1159pm");

		AddAnalyzer.analyze();
		assertEquals("task name 20", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 0, 1);
		if (gc.compareTo(Calendar.getInstance()) == -1) {
			gc.add(Calendar.DATE, 1);
		}
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc.set(Calendar.HOUR, 23);
		gc.set(Calendar.MINUTE, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 4 timed task after now
		Analyzer.analyze("add task name 21 everyday 1158pm to 1159pm");

		AddAnalyzer.analyze();
		assertEquals("task name 21", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 58);
		if (gc.compareTo(Calendar.getInstance()) == -1) {
			gc.add(Calendar.DATE, 1);
		}
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc.set(Calendar.MINUTE, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());


		//every Monday to Sunday case, first task postpone one week if entered time is before now
		//case 1 timed task at the same day
		Analyzer.analyze("add task name 22 every mon from 1201am to 1202am");

		AddAnalyzer.analyze();
		assertEquals("task name 22", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 0, 1);
		gc.add(Calendar.DATE, (Calendar.MONDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		if (gc.compareTo(Calendar.getInstance()) == -1) {
			gc.add(Calendar.DATE, 7);
		}
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc.set(Calendar.MINUTE, 2);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 2 timed task at different day
		boolean isPostponed = false;
		Analyzer.analyze("add task name 23 every tue from 1201am to next tue 1202am");

		AddAnalyzer.analyze();
		assertEquals("task name 23", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH), currTime.get(Calendar.DATE), 0, 1);
		gc.add(Calendar.DATE, (Calendar.TUESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		if (gc.compareTo(Calendar.getInstance()) == -1) {
			gc.add(Calendar.DATE, 7);
			isPostponed = true;
		}
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH), currTime.get(Calendar.DATE), 0, 2);
		gc.add(Calendar.DATE, (Calendar.TUESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7 + 7);
		if (isPostponed) {
			gc.add(Calendar.DATE, 7);
		}
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 2 deadline task
		Analyzer.analyze("add task name 24 every wed 1201am");

		AddAnalyzer.analyze();
		assertEquals("task name 24", AddAnalyzer.getToBeAddedTask().getTaskName());
		assertEquals(null, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH), currTime.get(Calendar.DATE), 0, 1);
		gc.add(Calendar.DATE, (Calendar.WEDNESDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		if (gc.compareTo(Calendar.getInstance()) == -1) {
			gc.add(Calendar.DATE, 7);
		}
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 0, 1);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//the rest is use to make sure postpone is taken place
		Analyzer.analyze("add task name 25 every thursday 1201am");

		AddAnalyzer.analyze();
		assertEquals("task name 25", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH), currTime.get(Calendar.DATE), 0, 1);
		gc.add(Calendar.DATE, (Calendar.THURSDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		if (gc.compareTo(Calendar.getInstance()) == -1) {
			gc.add(Calendar.DATE, 7);
		}
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 0, 1);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		Analyzer.analyze("add task name 26 every friday 1201am");

		AddAnalyzer.analyze();
		assertEquals("task name 26", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH), currTime.get(Calendar.DATE), 0, 1);
		gc.add(Calendar.DATE, (Calendar.FRIDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		if (gc.compareTo(Calendar.getInstance()) == -1) {
			gc.add(Calendar.DATE, 7);
		}
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 0, 1);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		Analyzer.analyze("add task name 27 every saturday 1201am");

		AddAnalyzer.analyze();
		assertEquals("task name 27", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH), currTime.get(Calendar.DATE), 0, 1);
		gc.add(Calendar.DATE, (Calendar.SATURDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		if (gc.compareTo(Calendar.getInstance()) == -1) {
			gc.add(Calendar.DATE, 7);
		}
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 0, 1);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		Analyzer.analyze("add task name 28 every sunday 1201am");

		AddAnalyzer.analyze();
		assertEquals("task name 28", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH), currTime.get(Calendar.DATE), 0, 1);
		gc.add(Calendar.DATE, (Calendar.SUNDAY - currTime.get(Calendar.DAY_OF_WEEK) + 7) % 7);
		if (gc.compareTo(Calendar.getInstance()) == -1) {
			gc.add(Calendar.DATE, 7);
		}
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 0, 1);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());


		//task name quoted with ' '
		//case 1 without any time information
		Analyzer.analyze("add 'task name 29 with today or sunday or 23/4/12 is okay'");

		AddAnalyzer.analyze();
		assertEquals("task name 29 with today or sunday or 23/4/12 is okay", AddAnalyzer.getToBeAddedTask().getTaskName());
		assertEquals(null, AddAnalyzer.getToBeAddedTask().getStartTime());
		assertEquals(null, AddAnalyzer.getToBeAddedTask().getEndTime());
		//case 2 with time information
		Analyzer.analyze("add 'task name 30 with today or sunday or 23/4/12 is okay' 1159pm");

		AddAnalyzer.analyze();
		assertEquals("task name 30 with today or sunday or 23/4/12 is okay", AddAnalyzer.getToBeAddedTask().getTaskName());
		assertEquals(null, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());


		//task with reminder
		//case 1 deadline task
		Analyzer.analyze("add /r taskname 31 1159pm");

		AddAnalyzer.analyze();
		assertTrue(AddAnalyzer.getToBeAddedTask().getIsReminderNeeded());
		assertEquals("taskname 31", AddAnalyzer.getToBeAddedTask().getTaskName());
		assertEquals(null, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		//case 2 timed task		
		Analyzer.analyze("add /r taskname 32 1158pm 1159pm");

		AddAnalyzer.analyze();
		assertTrue(AddAnalyzer.getToBeAddedTask().getIsReminderNeeded());
		assertEquals("taskname 32", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 58);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());


		//all calendar pattern
		Analyzer.analyze("add taskname 33 1jan2100 1:23pm 2january2100 2.34pm");

		AddAnalyzer.analyze();
		assertEquals("taskname 33", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar(2100, 0, 1, 13, 23);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(2100, 0, 2, 14, 34);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		Analyzer.analyze("add taskname 34 31 dec 23:58 31december 23:59");

		AddAnalyzer.analyze();
		assertEquals("taskname 34", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), 11, 31, 23, 58);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), 11, 31, 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		Analyzer.analyze("add taskname 35 31/12 11.59 pm 2/1/2100 2:34 pm");

		AddAnalyzer.analyze();
		assertEquals("taskname 35", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), 11, 31, 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(2100, 0, 2, 14, 34);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		Analyzer.analyze("add taskname 36 31-12 1159 pm 2-1-2100 2 pm");

		AddAnalyzer.analyze();
		assertEquals("taskname 36", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), 11, 31, 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(2100, 0, 2, 14, 0);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());

		Analyzer.analyze("add taskname 37 31.12 1159 pm 2.1.2100 2pm");

		AddAnalyzer.analyze();
		assertEquals("taskname 37", AddAnalyzer.getToBeAddedTask().getTaskName());
		gc = new GregorianCalendar();
		gc = new GregorianCalendar(gc.get(Calendar.YEAR), 11, 31, 23, 59);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getStartTime());
		gc = new GregorianCalendar(2100, 0, 2, 14, 0);
		assertEquals(gc, AddAnalyzer.getToBeAddedTask().getEndTime());
	}

	@Test
	//throws exception for Analyzer.analyze() as it is tested in other test driver
	public void testInvalidInputException() throws InvalidInputException {
		//empty command details
		Analyzer.analyze("add");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_EMPTY_COMMAND_DETAILS, e.getMessage());
		}

		//spaces command details
		Analyzer.analyze("add            ");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_EMPTY_COMMAND_DETAILS, e.getMessage());
		}

		//command details without task name 
		Analyzer.analyze("add 2/2/2100 1159pm");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_NO_TASK_NAME, e.getMessage());
		}

		//add with reminder for floating task
		Analyzer.analyze("add -r taskname");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_REMINDER_FOR_FLOATING_TASK, e.getMessage());
		}

		//command details end time is earlier than start time
		Analyzer.analyze("add taskname 25/5/2100 4pm 22/5/2100 3pm");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_START_TIME_LATER_THAN_OR_EQUAL_TO_END_TIME, e.getMessage());
		}

		//command details end time is equal to start time
		Analyzer.analyze("add taskname 25/5/2100 4pm 25/5/2100 4pm");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_START_TIME_LATER_THAN_OR_EQUAL_TO_END_TIME, e.getMessage());
		}

		//start time(or both) is earlier than current time
		Analyzer.analyze("add taskname 1/1/2014 4pm 1/1/2100 4pm");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_START_TIME, e.getMessage());
		}	

		//end time for deadline task is earlier than current time
		Analyzer.analyze("add taskname 1/1/2014 4pm");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_END_TIME, e.getMessage());
		}

		//no time(only date) is given
		Analyzer.analyze("add taskname 1/1/2100");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_NO_EXACT_TIME, e.getMessage());
		}

		//add reminder for floating task
		Analyzer.analyze("add /r floating task");
		try {
			AddAnalyzer.analyze();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_REMINDER_FOR_FLOATING_TASK, e.getMessage());
		}
	}
}
