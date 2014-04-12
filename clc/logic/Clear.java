//@author A0105712U
package clc.logic;

import static clc.common.Constants.*;

import java.util.ArrayList;

import clc.common.LogHelper;
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
		LogHelper.info(MESSAGE_CLEARED);
		return MESSAGE_CLEARED;
	}
}
