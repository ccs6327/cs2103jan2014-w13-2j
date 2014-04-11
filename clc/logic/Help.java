//author A0112089J

package clc.logic;

import static clc.common.Constants.TYPE_ADD;
import static clc.common.Constants.TYPE_CREATE;
import static clc.common.Constants.TYPE_DELETE;
import static clc.common.Constants.TYPE_DELETE_SHORT;
import static clc.common.Constants.TYPE_DISPLAY;
import static clc.common.Constants.TYPE_DISPLAY_SHORT;
import static clc.common.Constants.TYPE_EDIT;
import static clc.common.Constants.TYPE_ERASE;
import static clc.common.Constants.TYPE_EXPORT;
import static clc.common.Constants.TYPE_EXPORT_SHORT;
import static clc.common.Constants.TYPE_IMPORT;
import static clc.common.Constants.TYPE_IMPORT_SHORT;
import static clc.common.Constants.TYPE_LIST;
import static clc.common.Constants.TYPE_MARK;
import static clc.common.Constants.TYPE_MARK_SHORT;
import static clc.common.Constants.TYPE_REMOVE;
import static clc.common.Constants.TYPE_SHOW;
import static clc.common.Constants.TYPE_UNMARK;
import static clc.common.Constants.TYPE_UNMARK_SHORT;
import static clc.common.Constants.TYPE_UPDATE;
import static clc.common.Constants.TYPE_UPDATE_SHORT;
import static clc.common.Constants.TYPE_SEARCH;
import static clc.common.Constants.SEARCH_HELP_FILE;
import static clc.common.Constants.IMPORT_EXPORT_HELP_FILE;
import static clc.common.Constants.UPDATE_HELP_FILE;
import static clc.common.Constants.MARK_UNMARK_HELP_FILE;
import static clc.common.Constants.DISPLAY_HELP_FILE;
import static clc.common.Constants.DELETE_HELP_FILE;
import static clc.common.Constants.ADD_HELP_FILE;
import static clc.common.Constants.GENERAL_HELP_FILE;
import static clc.common.Constants.DATE_HELP_FILE;
import static clc.common.Constants.HOTKEY_HELP_FILE;
import static clc.common.Constants.HOTKEY;
import static clc.common.Constants.DATE;
import clc.storage.Storage;

public class Help implements Command {
	private String query;
	
	//constructor
	public Help(String _query) {
		query = _query;
	}
	
	public String execute() {
		String fileName;
		if (isCaseAdd(query)) {
			fileName = ADD_HELP_FILE;
		} else if (isCaseDelete(query)) {
			fileName = DELETE_HELP_FILE;
		} else if (isCaseDisplay(query)) {
			fileName = DISPLAY_HELP_FILE;
		} else if (isCaseMark(query) || isCaseUnmark(query)) {
			fileName = MARK_UNMARK_HELP_FILE;
		} else if (isCaseUpdate(query)) {
			fileName = UPDATE_HELP_FILE;
		} else if (isCaseImport(query) || isCaseExport(query)) {
			fileName = IMPORT_EXPORT_HELP_FILE;
		} else if (isCaseSearch(query)) {
			fileName = SEARCH_HELP_FILE;
		} else if (isCaseHotkey(query)) {
			fileName = HOTKEY_HELP_FILE;
		} else if (isCaseDate(query)) {
			fileName = DATE_HELP_FILE;
		} else {
			fileName = GENERAL_HELP_FILE;
		}
		
		String helpMessage = readUserManual(fileName);
		return helpMessage;
	}

	private String readUserManual(String fileName) {
		return Storage.readManualFromHelpFile(fileName);
	}
	
	private static boolean isCaseAdd(String query) {
		return query.equalsIgnoreCase(TYPE_ADD) 
				|| query.equalsIgnoreCase(TYPE_CREATE);
	}

	private static boolean isCaseDelete(String query) {
		return query.equalsIgnoreCase(TYPE_DELETE) 
				|| query.equalsIgnoreCase(TYPE_DELETE_SHORT)
				|| query.equalsIgnoreCase(TYPE_ERASE)
				|| query.equalsIgnoreCase(TYPE_REMOVE);
	}

	private static boolean isCaseDisplay(String query) {
		return query.equalsIgnoreCase(TYPE_DISPLAY) 
				|| query.equalsIgnoreCase(TYPE_DISPLAY_SHORT) 
				|| query.equalsIgnoreCase(TYPE_SHOW) 
				|| query.equalsIgnoreCase(TYPE_LIST);
	}

	private static boolean isCaseMark(String query) {
		return query.equalsIgnoreCase(TYPE_MARK) 
				|| query.equalsIgnoreCase(TYPE_MARK_SHORT);
	}

	private static boolean isCaseUnmark(String query) {
		return query.equalsIgnoreCase(TYPE_UNMARK) 
				|| query.equalsIgnoreCase(TYPE_UNMARK_SHORT);
	}

	private static boolean isCaseUpdate(String query) {
		return query.equalsIgnoreCase(TYPE_UPDATE) 
				|| query.equalsIgnoreCase(TYPE_UPDATE_SHORT)
				|| query.equalsIgnoreCase(TYPE_EDIT);
	}

	private static boolean isCaseImport(String query) {
		return query.equalsIgnoreCase(TYPE_IMPORT)
				|| query.equalsIgnoreCase(TYPE_IMPORT_SHORT);
	}

	private static boolean isCaseExport(String query) {
		return query.equalsIgnoreCase(TYPE_EXPORT)
				|| query.equalsIgnoreCase(TYPE_EXPORT_SHORT);
	}

	private static boolean isCaseSearch(String query) {
		return query.equalsIgnoreCase(TYPE_SEARCH);
	}

	private static boolean isCaseHotkey(String query) {
		return query.equalsIgnoreCase(HOTKEY);
	}

	private static boolean isCaseDate(String query) {
		return query.equalsIgnoreCase(DATE);
	}
}
