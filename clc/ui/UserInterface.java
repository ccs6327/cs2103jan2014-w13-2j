package clc.ui;

import java.util.ArrayList;
import java.util.GregorianCalendar;

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
		
		switch (commandType) {
		case TYPE_ADD:
			AddAnalyzer.analyze();
			Task task = AddAnalyzer.getToBeAddedTask();
			command = new Add(task);
			break;
		case TYPE_DISPLAY:
			DisplayAnalyzer.analyze();
			boolean isCaseDisplayCalendar = DisplayAnalyzer.getDisplayCase();
			if (isCaseDisplayCalendar) {
				ArrayList<GregorianCalendar> time = DisplayAnalyzer.getCalendar();
				command = new Display(time);
			} else {
				String query = DisplayAnalyzer.getDisplayQuery();
				command = new Display(query);
			}
			break;
		case TYPE_DELETE:
			command = new Delete(SequenceNumberExtracter.getSequenceNum());
			break;
		case TYPE_MARK:
			command = new Mark(SequenceNumberExtracter.getSequenceNum());
			break;
		case TYPE_UNMARK:
			command = new Unmark(SequenceNumberExtracter.getSequenceNum());
			break;
		case TYPE_UPDATE:
			UpdateAnalyzer.analyze();
			boolean isCaseUpdateCalendar = UpdateAnalyzer.getUpdateCase();
			int seqNo = UpdateAnalyzer.getSeqNumForUpdate();
			if (isCaseUpdateCalendar) {
				ArrayList<GregorianCalendar> time = UpdateAnalyzer.getCalendar();
				command = new Update(seqNo, time);
			} else {
				command = new Update(seqNo, UpdateAnalyzer.getNewTaskName());
			}
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
	}

	protected static String getWelcomeMessage() {
		return MESSAGE_WELCOME;
	}
}
