package clc.ui;

import java.io.IOException;
import java.util.Scanner;

import clc.logic.Add;
import clc.logic.Clear;
import clc.logic.Command;
import clc.logic.Delete;
import clc.logic.Display;
import clc.logic.Help;
import clc.logic.Task;
import static clc.common.Constants.*;

public class UserInterface {
	private Scanner scanner;
	
	public UserInterface() {
		scanner = new Scanner(System.in);
	}

	public void printWelcomeMessage() {
		println(MESSAGE_WELCOME);
	}

	public void executeCommandsUntilExit() {
		while (true) {
			print(MESSAGE_ENTER_COMMAND);
			String command = scanner.nextLine();
			String feedback = executeCommand(command);
			println(feedback);
		}
		
	}
	
	private String executeCommand(String input) {
		Command command = null;
		Analyzer analyzer = new Analyzer(input);
		if (input.trim().equals(EMPTY_STRING)) {
			return String.format(MESSAGE_INVALID_FORMAT, input);
		}
		
		String commandType = analyzer.getCommandType();
		
		switch (commandType) {
		case TYPE_ADD:
			command = new Add(analyzer.analyzeAdd());
			//return input.substring(4) + " added";
			break;
		case TYPE_DISPLAY:
			command = new Display();
			break;
		case TYPE_DELETE:
			command = new Delete();
			break;
		case TYPE_CLEAR:
			command = new Clear();
			break;
		case TYPE_HELP:
			command = new Help();
			break;
		case TYPE_EXIT:
			exit();
			break;
		default:
			return String.format(MESSAGE_INVALID_FORMAT, input);
		}
		
		command.execute();
		
		return "";
	}
	
	private void exit() {
		System.exit(0);
	}

	
	private void print(String content) {
		System.out.print(content);
	}
	
	private void println(String content) {
		System.out.println(content);
	}
}
