//@author A0112089J

package clc.ui;

import static clc.common.Constants.ERROR_CONTAIN_NON_NUMERIC_INFO;
import static clc.common.Constants.ERROR_EMPTY_COMMAND_DETAILS;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import clc.common.InvalidInputException;

public class SequenceNumberExtractorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSequenceNum() throws InvalidInputException {
		//normal sequence
		Analyzer.analyze("del 1 2 3 4 5");
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.add(1); 
		temp.add(2); 
		temp.add(3); 
		temp.add(4); 
		temp.add(5);
		try {
			assertEquals(temp, SequenceNumberExtractor.getSequenceNum());
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}

		//repeating sequence
		Analyzer.analyze("del 1 2 2 2 4 5");
		temp = new ArrayList<Integer>();
		temp.add(1); 
		temp.add(2);
		temp.add(4); 
		temp.add(5);
		try {
			assertEquals(temp, SequenceNumberExtractor.getSequenceNum());
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}

		//contains non-numeric
		Analyzer.analyze("del 1 2 b");
		try {
			SequenceNumberExtractor.getSequenceNum();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_CONTAIN_NON_NUMERIC_INFO, e.getMessage());
		}

		//empty command details
		Analyzer.analyze("del");
		try {
			SequenceNumberExtractor.getSequenceNum();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_EMPTY_COMMAND_DETAILS, e.getMessage());
		}

		//blank spaces command details
		Analyzer.analyze("del          ");
		try {
			SequenceNumberExtractor.getSequenceNum();
		} catch (InvalidInputException e) {
			assertEquals(ERROR_EMPTY_COMMAND_DETAILS, e.getMessage());
		}
	}

}
