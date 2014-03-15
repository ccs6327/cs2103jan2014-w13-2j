package clc.logic;

import static clc.common.Constants.*;

import java.util.ArrayList;

import clc.storage.Storage;

public class Clear implements Command {
	@Override
	public String execute() {
		internalMem.clear();
		displayMem.clear();
		addNewVersion();
		Storage.writeContentIntoFile();
		return MESSAGE_CLEARED;
	}
}
