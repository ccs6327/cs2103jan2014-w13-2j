package clc.logic;

import static clc.common.Constants.*;

import java.util.ArrayList;

public class Undo implements Command {

	@Override
	public String execute() {
		
		/* //debug
		System.out.println(internalMem.size());
		System.out.println(historyMem.size());
		// */
		
		try {
			internalMem = historyMem.get(currentVersion - 1);
			currentVersion--;
		} catch (Exception exception) {
			return MESSAGE_UNDONE_FAILED;
		}
		
		/* //debug
		System.out.println(internalMem.size());
		System.out.println(historyMem.size());
		// */
		return MESSAGE_UNDONE;
	}

}
