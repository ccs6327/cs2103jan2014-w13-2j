//@author A0105712U
package clc.storage;

import static org.junit.Assert.*;
import static clc.common.Constants.*;

import org.junit.Test;

import clc.common.LogHelper;

public class StorageTest {
	
	private static final String PATH_INVALID = "This is an invalid path ?%!~";
	private static final String PATH_ROOT = "\\" + CLC_EXPORT_DIRECTORY;
	private static final String PATH_ROOT_C = "C:\\" + CLC_EXPORT_DIRECTORY;
	private static final String PATH_WORKSPACE = "..\\" + CLC_EXPORT_DIRECTORY;
	private static final String PATH_NESTED_DIRECTORIES = "C:\\CLC\\Unit Testing\\ + CLC_EXPORT_DIRECTORY";
			
	@Test
	public void testExportDataFile() {
		
		Storage.initializeDataFile();
		
		LogHelper.info("Testing exportDataFile");
		
		// Test case 1: path is null
		assertEquals(MESSAGE_EXPORT_FAILED, Storage.exportDataFile(null));
		LogHelper.info("Test case 1 passed");
		
		// Test case 2: path is invalid
		assertEquals(MESSAGE_EXPORT_FAILED_INVALID_PATH, Storage.exportDataFile(PATH_INVALID));
		LogHelper.info("Test case 2 passed");
		
		// Test case 3: path is the root of workspace
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_ROOT));
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_ROOT));
		LogHelper.info("Test case 3 passed");
		
		// Test case 4: path is other root
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_ROOT_C));
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_ROOT_C));
		LogHelper.info("Test case 4 passed");
		
		// Test case 5: path is the workspace
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_WORKSPACE));
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_WORKSPACE));
		LogHelper.info("Test case 5 passed");
		
		// Test case 6: path is in general
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_NESTED_DIRECTORIES));
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(PATH_NESTED_DIRECTORIES));
		LogHelper.info("Test case 6 passed");
		
		LogHelper.info("All test cases passed\r\n");
	}
	
	@Test
	public void testImportDataFile() {
		
		LogHelper.info("Testing importDataFile");
		
		// Test case 1: path is null
		assertEquals(MESSAGE_IMPORT_NO_ACCESS, Storage.importDataFile(null));
		LogHelper.info("Test case 1 passed");
		
		// Test case 2: path is invalid
		assertEquals(MESSAGE_IMPORT_NO_ACCESS, Storage.importDataFile(PATH_INVALID));
		LogHelper.info("Test case 2 passed");
		
		// Test case 3: path is the root of workspace
		assertEquals(MESSAGE_IMPORT, Storage.importDataFile(PATH_ROOT));
		LogHelper.info("Test case 3 passed");
		
		// Test case 4: path is other root
		assertEquals(MESSAGE_IMPORT, Storage.importDataFile(PATH_ROOT_C));
		LogHelper.info("Test case 4 passed");
		
		// Test case 5: path is the workspace
		assertEquals(MESSAGE_IMPORT, Storage.importDataFile(PATH_WORKSPACE));
		LogHelper.info("Test case 5 passed");
		
		// Test case 6: path is in general
		assertEquals(MESSAGE_IMPORT, Storage.importDataFile(PATH_NESTED_DIRECTORIES));
		LogHelper.info("Test case 6 passed");
		
		LogHelper.info("All test cases passed\r\n");
	}
}
