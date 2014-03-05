package clc.ui;

import clc.logic.Task;

public class Analyzer {
	private String input;
	private String commandType;
	
	public Analyzer(String input) {
		this.input = input;
		commandType = getFirstWord(input);
	}

	public String getCommandType() {
		return commandType;
	}
	
	private String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}
	
	private String removeFirstWord(String userCommand) {
		return userCommand.replaceFirst(getFirstWord(userCommand), "").trim();
	}

	

	public Task analyzeAdd() {
		// TODO Auto-generated method stub
		return null;
	}



	
}
