package clc.storage;

import static org.junit.Assert.*;
import static clc.common.Constants.*;

import org.junit.Test;

public class StorageTest {

	private static final String FILE_E = "E:\\";

	@Test
	public void testExportDataFile() {
		
		Storage.initializeDataFile();
		
		assertEquals(MESSAGE_EXPORT_FAILED, Storage.exportDataFile(null + "\\"));
		assertEquals(MESSAGE_EXPORT, Storage.exportDataFile(FILE_E));
	}

}
