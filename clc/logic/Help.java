package clc.logic;
import clc.storage.Storage;

public class Help implements Command {
	
	Storage storage = new Storage();
	
	//constructor
	public Help() {}
	
	public void execute() {
		String[] helpMessage = readUserManual();
		//printUserManual(helpMessage);
	}

	private String[] readUserManual() {
		//return storage.
		return null;
	}
}
