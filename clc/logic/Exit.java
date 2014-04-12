//@author A0105712U
/**
 * Command Line Calendar (CLC)
 * Exit.java
 * 
 * This Exit class implements Command. Its instance executes exit command.
 */
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
