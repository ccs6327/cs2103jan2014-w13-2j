package clc.logic;

import static clc.common.Constants.*;

import java.util.ArrayList;

import clc.storage.Storage;
import clc.storage.History;

public class Clear implements Command {
	private ArrayList<Task> internalMem;
	private ArrayList<Integer> displayMem;
	
	public Clear() {
		internalMem = Storage.getInternalMem();
		displayMem = Storage.getDisplayMem();
	}
	
	@Override
	public String execute() {
		internalMem.clear();
		displayMem.clear();
		History.addNewVersion();
		Storage.writeContentIntoFile();
		return MESSAGE_CLEARED;
	}
}
