package clc.logic;

import static clc.common.Constants.*;

import java.util.ArrayList;

import clc.storage.Storage;
import clc.storage.History;

public class Redo implements Command {
	private ArrayList<ArrayList<Task>> historyMem;
	private ArrayList<Task> internalMem;
	private int currentVersion;
	
	public Redo() {
		internalMem = Storage.getInternalMem();
		historyMem = History.getHistoryMem();
	}
	
	@Override
	public String execute() {
		try {
			internalMem = historyMem.get(currentVersion + 1);
			currentVersion++;
		} catch (Exception exception) {
			return MESSAGE_REDONE_FAILED;
		}
		
		return MESSAGE_REDONE;
	}

}
