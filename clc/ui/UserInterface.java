package clc.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

import clc.common.InvalidInputException;
import clc.logic.Add;
import clc.logic.Clear;
import clc.logic.Command;
import clc.logic.Delete;
import clc.logic.Display;
import clc.logic.Exit;
import clc.logic.Export;
import clc.logic.Help;
import clc.logic.Import;
import clc.logic.Mark;
import clc.logic.Redo;
import clc.logic.Remind;
import clc.logic.Task;
import clc.logic.Undo;
import clc.logic.Update;
import clc.logic.Unmark;
import clc.storage.History;

import static clc.common.Constants.MESSAGE_INVALID_FORMAT;
import static clc.common.Constants.MESSAGE_WELCOME;
import static clc.common.Constants.TYPE_ADD;
import static clc.common.Constants.TYPE_CREATE;
import static clc.common.Constants.TYPE_DELETE;
import static clc.common.Constants.TYPE_DELETE_SHORT;
import static clc.common.Constants.TYPE_ERASE;
import static clc.common.Constants.TYPE_REMOVE;
import static clc.common.Constants.TYPE_DISPLAY;
import static clc.common.Constants.TYPE_DISPLAY_SHORT;
import static clc.common.Constants.TYPE_SHOW;
import static clc.common.Constants.TYPE_LIST;
import static clc.common.Constants.TYPE_MARK;
import static clc.common.Constants.TYPE_MARK_SHORT;
import static clc.common.Constants.TYPE_UNMARK;
import static clc.common.Constants.TYPE_UNMARK_SHORT;
import static clc.common.Constants.TYPE_UPDATE;
import static clc.common.Constants.TYPE_UPDATE_SHORT;
import static clc.common.Constants.TYPE_EDIT;
import static clc.common.Constants.TYPE_CLEAR;
import static clc.common.Constants.TYPE_UNDO;
import static clc.common.Constants.TYPE_REDO;
import static clc.common.Constants.TYPE_HELP;
import static clc.common.Constants.TYPE_EXIT;
import static clc.common.Constants.TYPE_IMPORT;
import static clc.common.Constants.TYPE_IMPORT_SHORT;
import static clc.common.Constants.TYPE_EXPORT;
import static clc.common.Constants.TYPE_EXPORT_SHORT;


public class UserInterface {
	private GUI gui = new GUI();
	private static String input;
	private static Timer timer;
	public UserInterface() {}

	public void executeCommandsUntilExit() {
		gui.launchAndGetInputAndExecute();
		History.addNewVersion();
		setTimerForReminder();
	}

	private static void setTimerForReminder() {
		Remind rmd = new Remind();
		ArrayList<String> reminderInfo = rmd.getToBeRemindedInfo();
		ArrayList<Date> reminderTime = rmd.getToBeRemindedTime();
		ArrayList<Long> reminderTaskId = rmd.getToBeRemindedTaskId();
		timer = new Timer(true);
		
		for (int i = 0; i < reminderInfo.size(); i ++) {
			timer.schedule(new Reminder(reminderInfo.get(i), reminderTaskId.get(i)), reminderTime.get(i));
		}
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
			if (isCaseAdd(commandType)) {
				command = analyzeAdd();
			} else if (isCaseDelete(commandType)) {
				command = analyzeDelete();
			} else if (isCaseDisplay(commandType)) {
				command = analyzeDisplay();
			} else if (isCaseMark(commandType)) {
				command = analyzeMark();
			} else if (isCaseUnmark(commandType)) {
				command = analyzeUnmark();
			} else if (isCaseUpdate(commandType)) {
				command = analyzeUpdate();
			} else if (isCaseImport(commandType)) {
				command = analyzeImport();
			} else if (isCaseExport(commandType)) {
				command = analyzeExport();
			} else if (isCaseSearch(commandType)) {
				command = analyzeSearch();
			} else if (isCaseClear(commandType)) {
				command = new Clear();
			} else if (isCaseUndo(commandType)) {
				command = new Undo();
			} else if (isCaseRedo(commandType)) {
				command = new Redo();
			} else if (isCaseHelp(commandType)) {
				command = new Help();
			} else if (isCaseExit(commandType)) {
				command = new Exit();
			} else {
				throw new InvalidInputException(String.format(MESSAGE_INVALID_FORMAT, input));
			}
			
			String feedback = command.execute();
			
			clearAndResetTimer();
			
			return feedback;
		} catch (InvalidInputException iie) {
			return iie.getMessage();
		}
	}

	private static boolean isCaseAdd(String commandType) {
		return commandType.equalsIgnoreCase(TYPE_ADD) 
				|| commandType.equalsIgnoreCase(TYPE_CREATE);
	}

	private static boolean isCaseDelete(String commandType) {
		return commandType.equalsIgnoreCase(TYPE_DELETE) 
				|| commandType.equalsIgnoreCase(TYPE_DELETE_SHORT)
				|| commandType.equalsIgnoreCase(TYPE_ERASE)
				|| commandType.equalsIgnoreCase(TYPE_REMOVE);
	}

	private static boolean isCaseDisplay(String commandType) {
		return commandType.equalsIgnoreCase(TYPE_DISPLAY) 
				|| commandType.equalsIgnoreCase(TYPE_DISPLAY_SHORT) 
				|| commandType.equalsIgnoreCase(TYPE_SHOW) 
				|| commandType.equalsIgnoreCase(TYPE_LIST);
	}

	private static boolean isCaseMark(String commandType) {
		return commandType.equalsIgnoreCase(TYPE_MARK) 
				|| commandType.equalsIgnoreCase(TYPE_MARK_SHORT);
	}

	private static boolean isCaseUnmark(String commandType) {
		return commandType.equalsIgnoreCase(TYPE_UNMARK) 
				|| commandType.equalsIgnoreCase(TYPE_UNMARK_SHORT);
	}

	private static boolean isCaseUpdate(String commandType) {
		return commandType.equalsIgnoreCase(TYPE_UPDATE) 
				|| commandType.equalsIgnoreCase(TYPE_UPDATE_SHORT)
				|| commandType.equalsIgnoreCase(TYPE_EDIT);
	}

	private static boolean isCaseImport(String commandType) {
		return commandType.equalsIgnoreCase(TYPE_IMPORT)
				|| commandType.equalsIgnoreCase(TYPE_IMPORT_SHORT);
	}

	private static boolean isCaseExport(String commandType) {
		return commandType.equalsIgnoreCase(TYPE_EXPORT)
				|| commandType.equalsIgnoreCase(TYPE_EXPORT_SHORT);
	}

	private static boolean isCaseSearch(String commandType) {
		return commandType.equalsIgnoreCase("search");
	}

	private static boolean isCaseClear(String commandType) {
		return commandType.equalsIgnoreCase(TYPE_CLEAR);
	}

	private static boolean isCaseUndo(String commandType) {
		return commandType.equalsIgnoreCase(TYPE_UNDO);
	}

	private static boolean isCaseRedo(String commandType) {
		return commandType.equalsIgnoreCase(TYPE_REDO);
	}

	private static boolean isCaseHelp(String commandType) {
		return commandType.equalsIgnoreCase(TYPE_HELP);
	}

	private static boolean isCaseExit(String commandType) {
		return commandType.equalsIgnoreCase(TYPE_EXIT);
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
			boolean isCaseKeywordCalendar = DisplayAnalyzer.getDisplayCalendarCase();
			ArrayList<GregorianCalendar> time = DisplayAnalyzer.getCalendar();
			if (isCaseKeywordCalendar) {
				String query = DisplayAnalyzer.getDisplayQuery();
				command = new Display(time, query);
			} else {
				command = new Display(time);
			}
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
	
	private static Command analyzeImport() {
		Command command;
		command = new Import(Analyzer.getCommandDetails());
		return command;
	}

	private static Command analyzeExport() {
		Command command;
		command = new Export(Analyzer.getCommandDetails());
		return command;
	}

	private static Command analyzeSearch() {
		Command command;
		command = new Search(Analyzer.getCommandDetails());
		return command;
	}

	private static void clearAndResetTimer() {
		timer.cancel();
		timer.purge();
		setTimerForReminder();
	}

	protected static String getWelcomeMessage() {
		return MESSAGE_WELCOME;
	}

	public static void markReminderTask(long taskId) {
		Command command;
		command = new Mark(taskId);
		command.execute();
	}
}
