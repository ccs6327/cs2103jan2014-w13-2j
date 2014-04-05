package clc.logic;

import clc.storage.Storage;
import static clc.common.Constants.BACKSLASH;

public class Export implements Command {
	
	private String path;

	public Export(String commandDetails) {
		path = formatPath(commandDetails);
	}

	private String formatPath(String commandDetails) {
		if (commandDetails.endsWith(BACKSLASH)) {
			return commandDetails;
		} else {
			return commandDetails + BACKSLASH;
		}
	}

	@Override
	public String execute() {
		return String.format(Storage.exportDataFile(path), path);
	}

}
