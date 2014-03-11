package clc.logic;

import static clc.common.Constants.*;
import clc.storage.Storage;

public class Clear implements Command {
	@Override
	public String execute() {
		internalMem.clear();
		addNewVersion();
		Storage.writeContentIntoFile();
		return MESSAGE_CLEARED;
	}
}
