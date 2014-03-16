package clc.logic;

import static clc.common.Constants.*;
import static clc.storage.History.historyMem;
import static clc.storage.History.currentVersion;
import static clc.storage.Storage.internalMem;

import java.util.ArrayList;

public class Redo implements Command {

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
