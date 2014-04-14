//@author A0105712U
/**
 * Command Line Calendar (CLC)
 * Undo.java
 * 
 * This Undo class implements Command. Its instance executes undo command.
 */
package clc.logic;

import static clc.common.Constants.*;

import java.util.ArrayList;

import clc.common.LogHelper;
import clc.storage.History;
import clc.storage.Storage;



public class Undo implements Command {
	private ArrayList<ArrayList<Task>> historyMem;
	private ArrayList<Integer> displayMem;
	private int currentVersion;
	
	@Override
	public String execute() {
		
		historyMem = History.getHistoryMem();
		displayMem = History.getDisplayMem();
		currentVersion = History.getCurrentVersion();
		
		try {
			Storage.setInternalMem(historyMem.get(currentVersion - 1));
			Storage.writeContentIntoFile();
			Storage.setDisplayMem(displayMem);
			currentVersion = History.decreaseCurrentVersion();
		} catch (Exception exception) {
			LogHelper.info(MESSAGE_UNDONE_FAILED);
			return MESSAGE_UNDONE_FAILED;
		}

		LogHelper.info(MESSAGE_UNDONE);
		return MESSAGE_UNDONE;
	}

}
