/**
 * Command Line Calendar (CLC)
 * StorageTest.java
 * 
 * This is JUnit test cases for Storage.
 */
//@author A0105712U
package clc.storage;

import static org.junit.Assert.*;
import static clc.common.Constants.*;

import org.junit.Test;

import clc.common.LogHelper;

public class StorageTest {
	
	private static final String TESTING_IMPORT_DATA_FILE = "Testing importDataFile";
	private static final String TEST_CASE_ALL_PASSED = "All test cases passed\r\n";
	private static final String TEST_CASE_SIX_PASSED = "Test case 6 passed";
	private static final String TEST_CASE_FIVE_PASSED = "Test case 5 passed";
	private static final String TEST_CASE_FOUR_PASSED = "Test case 4 passed";
	private static final String TEST_CASE_THREE_PASSED = "Test case 3 passed";
	private static final String TEST_CASE_TWO_PASSED = "Test case 2 passed";
	private static final String TEST_CASE_ONE_PASSED = "Test case 1 passed";
	private static final String TESTING_EXPORT_DATA_FILE = "Testing exportDataFile";
	private static final String PATH_INVALID = "This is an invalid path ?%!~";
	private static final String PATH_ROOT = "\\" + CLC_EXPORT_DIRECTORY;
	private static final String PATH_ROOT_C = "C:\\" + CLC_EXPORT_DIRECTORY;
	private static final String PATH_WORKSPACE = "..\\" + CLC_EXPORT_DIRECTORY;
	private static final String PATH_NESTED_DIRECTORIES = "C:\\CLC\\Unit Testing\\ + CLC_EXPORT_DIRECTORY";
			
	@Test
	public void testExportDataFile() {
		
		Storage.initializeDataFile();
		
		LogHelper.info(TESTING_EXPORT_DATA_FILE);
		
		// Test case 1: path is null
		assertEquals(MESSAGE_EXPORT_FAILED, Storage.exportDataFile(null));
		LogHelper.info(TEST_CASE_ONE_PASSED);
		
		// Test case 2: path is invalid
		assertEquals(MESSAGE_EXPORT_FAILED_INVALID_PATH, Storage.exportDataFile(PATH_INVALID));
		LogHelper.info(TEST_CASE_TWO_PASSED);
		
		// Test case 3: path is the root of workspace
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_ROOT));
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_ROOT));
		LogHelper.info(TEST_CASE_THREE_PASSED);
		
		// Test case 4: path is other root
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_ROOT_C));
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_ROOT_C));
		LogHelper.info(TEST_CASE_FOUR_PASSED);
		
		// Test case 5: path is the workspace
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_WORKSPACE));
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_WORKSPACE));
		LogHelper.info(TEST_CASE_FIVE_PASSED);
		
		// Test case 6: path is in general
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_NESTED_DIRECTORIES));
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_NESTED_DIRECTORIES));
		LogHelper.info(TEST_CASE_SIX_PASSED);
		
		LogHelper.info(TEST_CASE_ALL_PASSED);
	}
	
	@Test
	public void testImportDataFile() {
		
		LogHelper.info(TESTING_IMPORT_DATA_FILE);
		
		// Test case 1: path is null
		assertEquals(MESSAGE_IMPORT_NO_ACCESS, Storage.importDataFile(null));
		LogHelper.info(TEST_CASE_ONE_PASSED);
		
		// Test case 2: path is invalid
		assertEquals(MESSAGE_IMPORT_NO_ACCESS, Storage.importDataFile(PATH_INVALID));
		LogHelper.info(TEST_CASE_TWO_PASSED);
		
		// Test case 3: path is the root of workspace
		assertEquals(MESSAGE_IMPORT, Storage.importDataFile(PATH_ROOT));
		LogHelper.info(TEST_CASE_THREE_PASSED);
		
		// Test case 4: path is other root
		assertEquals(MESSAGE_IMPORT, Storage.importDataFile(PATH_ROOT_C));
		LogHelper.info(TEST_CASE_FOUR_PASSED);
		
		// Test case 5: path is the workspace
		assertEquals(MESSAGE_IMPORT, Storage.importDataFile(PATH_WORKSPACE));
		LogHelper.info(TEST_CASE_FIVE_PASSED);
		
		// Test case 6: path is in general
		assertEquals(MESSAGE_IMPORT, Storage.importDataFile(PATH_NESTED_DIRECTORIES));
		LogHelper.info(TEST_CASE_SIX_PASSED);
		
		LogHelper.info(TEST_CASE_ALL_PASSED);
	}
}
