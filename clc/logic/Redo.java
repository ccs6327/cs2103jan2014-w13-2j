//@author A0105712U
/**
 * Command Line Calendar (CLC)
 * Redo.java
 * 
 * This Redo class implements Command. Its instance executes Redo command.
 */
package clc.logic;

import static clc.common.Constants.*;

import java.util.ArrayList;

import clc.common.LogHelper;
import clc.storage.Storage;
import clc.storage.History;

public class Redo implements Command {
	private ArrayList<ArrayList<Task>> historyMem;
	private int currentVersion;
	
	@Override
	public String execute() {
		
		historyMem = History.getHistoryMem();
		currentVersion = History.getCurrentVersion();
		
		try {
			Storage.setInternalMem(historyMem.get(currentVersion + 1));
			Storage.writeContentIntoFile();
			currentVersion = History.increaseCurrentVersion();
		} catch (Exception exception) {
			LogHelper.info(MESSAGE_REDONE_FAILED);
			return MESSAGE_REDONE_FAILED;
		}
		
		LogHelper.info(MESSAGE_REDONE);
		return MESSAGE_REDONE;
	}

}
