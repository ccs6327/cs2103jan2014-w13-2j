package clc.logic;

import static clc.common.Constants.*;

import java.util.ArrayList;

public class Undo implements Command {

	@Override
	public String execute() {
		
		try {
			internalMem = historyMem.get(currentVersion - 1);
			currentVersion--;
		} catch (Exception exception) {
			return MESSAGE_UNDONE_FAILED;
		}
		
		return MESSAGE_UNDONE;
	}

}
