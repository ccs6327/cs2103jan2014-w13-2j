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
import clc.logic.Mark;
import clc.logic.Task;
import clc.logic.Unmark;
import static clc.common.Constants.*;

public class UserInterface {
	//private Scanner scanner;
	GUI gui = new GUI();
	private static String input, feedback;
	
	public UserInterface() {}
	
	public void executeCommandsUntilExit() {
		gui.launchAndGetInputAndExecute();
	}
	
	public static void setInputAndExecute(String line) {
		input = line;
		feedback = executeCommand();
	}

	private static String executeCommand() {
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
			boolean isCaseDisplayCalendar = analyzer.analyzeDisplay();
			if (isCaseDisplayCalendar) {
				ArrayList<GregorianCalendar> time = analyzer.getCalendar();
				command = new Display(time);
			} else {
				String query = analyzer.getDisplayQuery();
				command = new Display(query);
			}
			break;
		case TYPE_DELETE:
			command = new Delete(analyzer.analyzeDelete());
			break;
		case TYPE_MARK:
			command = new Mark(analyzer.analyzeMarkDone());
			break;
		case TYPE_UNMARK:
			command = new Unmark(analyzer.analyzeMarkNotDone());
			break;
		case TYPE_UPDATE:/*
			boolean isCaseUpdateTaskName = analyzer.analyzeUpdate();
			int seqNo = analyzer.getSeqNumForUpdate();
			if (isCaseUpdateTaskName) {
				command = new Update(seqNo, analyzer.getNewTaskName());
			} else {
				ArrayList<GregorianCalendar> time = analyzer.getNewCalendar();
				command = new Update(seqNo, time);
			}*/
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

	protected static String getWelcomeMessage() {
		return MESSAGE_WELCOME;
	}
	
	protected static String getFeedback() {
		return feedback;
	}
}
