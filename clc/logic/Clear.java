package clc.logic;

import static clc.common.Constants.*;

public class Clear implements Command {
	@Override
	public String execute() {
		internalMem.clear();
		addNewVersion();
		return MESSAGE_CLEARED;
	}
}
