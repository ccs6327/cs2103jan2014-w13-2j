package clc.logic;

import clc.storage.Storage;

public class Help implements Command {
	
	Storage storage = new Storage();
	
	//constructor
	public Help() {}
	
	public String execute() {
		String helpMessage = readUserManual();
		return helpMessage;
	}

	private String readUserManual() {
		return Storage.readManualFromHelpFile();
	}
}
