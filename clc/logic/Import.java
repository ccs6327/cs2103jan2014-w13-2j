//@author A0105712U
package clc.logic;

import clc.storage.Storage;
import static clc.common.Constants.BACKSLASH;
import static clc.common.Constants.CLC_EXPORT_DIRECTORY;

public class Import implements Command {
	
	private String inputPath;
	private String actualPath;

	public Import(String commandDetails) {
		inputPath = formatPath(commandDetails);
		actualPath = inputPath + CLC_EXPORT_DIRECTORY;
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
		return String.format(Storage.importDataFile(actualPath), inputPath);
	}

}
