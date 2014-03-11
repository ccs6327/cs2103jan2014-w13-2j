package clc.logic;

import static clc.common.Constants.*;

import java.util.ArrayList;

public class Redo implements Command {

	@Override
	public String execute() {
		
		/* //debug
		System.out.println(internalMem.size());
		System.out.println(historyMem.size());
		// */
		
		try {
			internalMem = historyMem.get(currentVersion + 1);
			currentVersion++;
		} catch (Exception exception) {
			return MESSAGE_REDONE_FAILED;
		}
		
		/* //debug
		System.out.println(internalMem.size());
		System.out.println(historyMem.size());
		// */
		return MESSAGE_REDONE;
	}

}
