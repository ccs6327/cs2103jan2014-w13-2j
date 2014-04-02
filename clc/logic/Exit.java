package clc.logic;

import clc.storage.Storage;

public class Exit implements Command {

	@Override
	public String execute() {
		Storage.writeContentIntoFile();
		System.exit(0);
		return null;
	}

}
