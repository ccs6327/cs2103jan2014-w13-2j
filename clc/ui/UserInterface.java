package clc.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;

import clc.logic.Add;
import clc.logic.Clear;
import clc.logic.Command;
import clc.logic.Delete;
import clc.logic.Display;
import clc.logic.Exit;
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
			Task task = analyzer.analyzeAdd();
			command = new Add(task);
			break;
		case TYPE_DISPLAY:
			ArrayList<GregorianCalendar> time = analyzer.analyzeDisplay();
			//command = new Display(time.get(0), time.get(1));
			break;
		case TYPE_DELETE:
			command = new Delete(analyzer.analyzeDelete());
			break;
		case TYPE_CLEAR:
			command = new Clear();
			break;
		case TYPE_HELP:
			command = new Help();
			break;
		case TYPE_EXIT:
			command = new Exit();
			break;
		default:
			return String.format(MESSAGE_INVALID_FORMAT, input);
		}
		
		return command.execute();
	}
		
	private void print(String content) {
		System.out.print(content);
	}
	
	private void println(String content) {
		System.out.println(content);
	}
}
