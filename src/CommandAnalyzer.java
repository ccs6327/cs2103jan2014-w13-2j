import java.util.ArrayList;
import java.util.Calendar;

public class CommandAnalyzer {
	private String operation, commandDetails;
	private CommandExecuter ce;
	private Storage storage = new Storage();

	//constructor
	public CommandAnalyzer() {}

	public CommandAnalyzer(String command) {
		operation = getFirstWord(command);
		commandDetails = trimAwayFirstWord(command).trim();
	}

	//public method
	public Command analyzeCommand() {
		//break down commandDetails
		String taskName = null;
		Calendar startTime = null;
		Calendar endTime = null;
		ArrayList<Integer> taskSeqNo = null;
		
		

		//.... code to determine

		return (new Command(operation, taskName, startTime, endTime, taskSeqNo));
	}

	private String getFirstWord(String command) {
		// extract the first word from the command
		//if (command.contains(" ")) {
		//	return (command.substring(0, command.indexOf(" ")));
		//}
		//return command;
		return command.trim().split("\\s+")[0];
		
	}

	private String trimAwayFirstWord(String s) {
		// return the string after the first word
		//return s.substring(operation.length(), s.length());
		return s.replaceFirst(getFirstWord(s), "").trim();
	}  	
}



