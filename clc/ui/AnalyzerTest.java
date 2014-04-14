//@author A0112089J

package clc.ui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import clc.common.InvalidInputException;

import static clc.common.Constants.MESSAGE_INVALID_FORMAT;
import static clc.common.Constants.ERROR_EMPTY_COMMAND_DETAILS;

public class AnalyzerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAnalyze() {
		//test to handle special syntax like ? \ * ( ) etc
		try {
			Analyzer.analyze("?*\\()");
		} catch (InvalidInputException e) {
			assertEquals(String.format(MESSAGE_INVALID_FORMAT, "?*\\()"), e.getMessage());
		}
	}

	@Test
	public void testGetCommandType() {
		try {
			Analyzer.analyze("add");
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		assertEquals("add", Analyzer.getCommandType());

		try {
			Analyzer.analyze("     add     ");
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		assertEquals("add", Analyzer.getCommandType());

		try {
			Analyzer.analyze("add taskname");
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		assertEquals("add", Analyzer.getCommandType());
		
		try {
			Analyzer.analyze("add *?\\!()");
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		assertEquals("add", Analyzer.getCommandType());
	}
	
	@Test
	public void testGetCommandDetails() {
		try {
			Analyzer.analyze("add");
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		assertEquals("", Analyzer.getCommandDetails());

		try {
			Analyzer.analyze("     add     ");
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		assertEquals("", Analyzer.getCommandDetails());

		try {
			Analyzer.analyze("add taskname");
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		assertEquals("taskname", Analyzer.getCommandDetails());
		
		try {
			Analyzer.analyze("add *?\\!()");
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		assertEquals("*?\\!()", Analyzer.getCommandDetails());
	}

	@Test
	public void testGetFirstWord() {
		assertEquals("add", Analyzer.getFirstWord("add"));

		assertEquals("add", Analyzer.getFirstWord("     add     "));

		assertEquals("add", Analyzer.getFirstWord("add taskname"));
		
		assertEquals("add", Analyzer.getFirstWord("add *?\\!()"));
	}

	@Test
	public void testRemoveFirstWord() {
		//test to handle special syntax like ? \ * ( ) etc
		try {
			Analyzer.removeFirstWord("?*\\() abc");
		} catch (InvalidInputException e) {
			assertEquals(String.format(MESSAGE_INVALID_FORMAT, "?*\\() abc"), e.getMessage());
		}
		
		try {
			assertEquals("", Analyzer.removeFirstWord("add"));
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}

		try {
			assertEquals("", Analyzer.removeFirstWord("     add     "));
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}

		try {
			assertEquals("taskname", Analyzer.removeFirstWord("add taskname"));
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		
		try {
			assertEquals("*?\\!()", Analyzer.removeFirstWord("add *?\\!()"));
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDoesCommandDetailsExist() {
		//empty command details
		assertFalse(Analyzer.doesCommandDetailsExist(""));
		
		//contains normal character
		assertTrue(Analyzer.doesCommandDetailsExist("abcde12345"));
		
		//contains special syntax
		assertTrue(Analyzer.doesCommandDetailsExist("***???\\()"));
	}

	@Test
	public void testThrowExceptionIfEmptyCommandDetails() {
		//no command details
		try {
			Analyzer.analyze("add");
			Analyzer.throwExceptionIfEmptyCommandDetails();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_EMPTY_COMMAND_DETAILS, e.getMessage());
		}
		
		//command details with space in front
		try {
			Analyzer.analyze("       add");
			Analyzer.throwExceptionIfEmptyCommandDetails();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_EMPTY_COMMAND_DETAILS, e.getMessage());
		}
		
		//command details with space at the back
		try {
			Analyzer.analyze("add       ");
			Analyzer.throwExceptionIfEmptyCommandDetails();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_EMPTY_COMMAND_DETAILS, e.getMessage());
		}
		
		//command details with space in front and at the back
		try {
			Analyzer.analyze("        add       ");
			Analyzer.throwExceptionIfEmptyCommandDetails();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_EMPTY_COMMAND_DETAILS, e.getMessage());
		}
		
		//command details with normal characters
		try {
			Analyzer.analyze("add something 12345");
			Analyzer.throwExceptionIfEmptyCommandDetails();
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		
		//command details with special syntax
		try {
			Analyzer.analyze("add \\**??()");
			Analyzer.throwExceptionIfEmptyCommandDetails();
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testIsNumeric() {
		//single alphabet
		assertFalse(Analyzer.isNumeric("a"));
		
		//string
		assertFalse(Analyzer.isNumeric("abc"));
		
		//numbers
		assertTrue(Analyzer.isNumeric("123"));
		
		//mixture of numbers and alphabets
		assertFalse(Analyzer.isNumeric("1a2b3c"));
		
		//largest number of Integer
		assertTrue(Analyzer.isNumeric(Integer.MAX_VALUE + ""));
		
		//larger than Integer max value
		assertFalse(Analyzer.isNumeric(Integer.MAX_VALUE + "1"));
		
		//smallest number of Integer
		assertTrue(Analyzer.isNumeric(Integer.MIN_VALUE + ""));
		
		//smaller than Integer max value
		assertFalse(Analyzer.isNumeric(Integer.MIN_VALUE + "1"));
	}
}
