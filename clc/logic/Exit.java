//@author A0105712U
package clc.logic;

import clc.common.LogHelper;
import clc.storage.Storage;

import static clc.common.Constants.LOG_EXIT;

public class Exit implements Command {

	@Override
	public String execute() {
		Storage.writeContentIntoFile();
		LogHelper.info(LOG_EXIT);
		System.exit(0);
		return null;
	}

}
