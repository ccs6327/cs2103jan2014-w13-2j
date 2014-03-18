package clc.ui;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import clc.common.InvalidInputException;
import clc.logic.Add;
import clc.logic.Clear;
import clc.logic.Command;
import clc.logic.Delete;
import clc.logic.Display;
import clc.logic.Exit;
import clc.logic.Help;
import clc.logic.Mark;
import clc.logic.Redo;
import clc.logic.Task;
import clc.logic.Undo;
import clc.logic.Update;
import clc.logic.Unmark;
import static clc.common.Constants.*;

public class UserInterface {
	private GUI gui = new GUI();
	private static String input;

	public UserInterface() {}

	public void executeCommandsUntilExit() {
		gui.launchAndGetInputAndExecute();
	}

	protected static String setInputAndExecute(String line) {
		input = line;
		return executeCommand();
	}

	private static String executeCommand() {
		Command command = null;
		Analyzer.analyze(input);
		String commandType = Analyzer.getCommandType(); 

		try{
			switch (commandType) {
			case TYPE_ADD:
				command = analyzeAdd();
				break;
			case TYPE_DISPLAY:
				command = analyzeDisplay();
				break;
			case TYPE_DELETE:
				command = analyzeDelete();
				break;
			case TYPE_MARK:
				command = analyzeMark();
				break;
			case TYPE_UNMARK:
				command = analyzeUnmark();
				break;
			case TYPE_UPDATE:
				command = analyzeUpdate();
				break;
			case TYPE_CLEAR:
				command = new Clear();
				break;
			case TYPE_UNDO:
				command = new Undo();
				break;
			case TYPE_REDO:
				command = new Redo();
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
		} catch (InvalidInputException iie) {
			String invalidFormatMessage = String.format(MESSAGE_INVALID_FORMAT, input);
			return invalidFormatMessage;
		}
	}

	private static Command analyzeUpdate() throws InvalidInputException {
		Command command;
		UpdateAnalyzer.analyze();
		boolean isCaseUpdateCalendar = UpdateAnalyzer.getUpdateCase();
		int seqNo = UpdateAnalyzer.getSeqNumForUpdate();
		if (isCaseUpdateCalendar) {
			int caseCalendarProvided = UpdateAnalyzer.getCalendarProvidedCase();
			ArrayList<GregorianCalendar> time = UpdateAnalyzer.getCalendar();
			command = new Update(seqNo, caseCalendarProvided, time);
		} else {
			String newTaskName = UpdateAnalyzer.getNewTaskName();
			command = new Update(seqNo, newTaskName);
		}
		return command;
	}

	private static Command analyzeAdd() throws InvalidInputException {
		Command command;
		AddAnalyzer.analyze();
		Task task = AddAnalyzer.getToBeAddedTask();
		command = new Add(task);
		return command;
	}

	private static Command analyzeDelete() throws InvalidInputException {
		Command command;
		command = new Delete(SequenceNumberExtractor.getSequenceNum());
		return command;
	}

	private static Command analyzeDisplay() throws InvalidInputException{
		Command command;
		DisplayAnalyzer.analyze();
		boolean isCaseDisplayCalendar = DisplayAnalyzer.getDisplayCase();
		if (isCaseDisplayCalendar) {
			ArrayList<GregorianCalendar> time = DisplayAnalyzer.getCalendar();
			command = new Display(time);
		} else {
			String query = DisplayAnalyzer.getDisplayQuery();
			command = new Display(query);
		}
		return command;
	}

	private static Command analyzeMark() throws InvalidInputException {
		Command command;
		command = new Mark(SequenceNumberExtractor.getSequenceNum());
		return command;
	}

	private static Command analyzeUnmark() throws InvalidInputException {
		Command command;
		command = new Unmark(SequenceNumberExtractor.getSequenceNum());
		return command;
	}

	protected static String getWelcomeMessage() {
		return MESSAGE_WELCOME;
	}
}
