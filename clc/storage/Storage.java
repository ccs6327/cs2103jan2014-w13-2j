package clc.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import clc.logic.Task;
import static clc.common.Constants.*;

public class Storage {
	
	private static File dataFile;
	
	/* If the data file does not exist, create the data file. 
	 *  Otherwise read in the content from the data file.
	 */
	public static void initializeDataFile() {
		dataFile = new File(OUTFILE);
		if (!dataFile.exists()) {
			// Create the file if it does not exist
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		} else {
			try {
				readContentFromFile();
			} catch (Exception e) {
					
			}
		}
	}
	
	// Read from the data file and store them into the internal memory
	public static void readContentFromFile() throws Exception {
		Task taskToRead = null;
		
		FileInputStream fileIn = new FileInputStream(dataFile);
        ObjectInputStream in = new ObjectInputStream(fileIn);

        while ((taskToRead = (clc.logic.Task) in.readObject()) != null) {
       	 internalMem.add(taskToRead);
        }

        in.close();
        fileIn.close();
	         
	}

	// Write contents in the list into the data file
	public static void writeContentIntoFile() throws IOException {
		FileOutputStream fileOut = new FileOutputStream(dataFile);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        for (Task task : internalMem) {
       	 	out.writeObject(task);
        }
        out.reset();
        out.close();
        fileOut.close();
	}
	     
	
}
