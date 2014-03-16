package clc.logic;

import static clc.common.Constants.*;
import static clc.storage.History.historyMem;
import static clc.storage.History.currentVersion;
import static clc.storage.Storage.internalMem;

import java.util.ArrayList;

import clc.storage.Storage;
import clc.storage.History;

public class Clear implements Command {
	@Override
	public String execute() {
		internalMem.clear();
		displayMem.clear();
		History.addNewVersion();
		Storage.writeContentIntoFile();
		return MESSAGE_CLEARED;
	}
}
