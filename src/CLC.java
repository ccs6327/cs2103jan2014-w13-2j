import java.util.Scanner;

public class CLC {
	private static Storage storage = new Storage("clcStorage.txt");
	private static CommandAnalyzer ca;
	private static CommandExecuter ce;

	public static void main(String[] args) {
		printWelcomeMessage();

		//openFileAndRetreiveTasksList();

		executeCommandsUntilExit();
	}

	private static void printWelcomeMessage() {
		//printWelcomeMessage
	}

	private static void openFileAndRetreiveTasksList() {
		// retrieve tasks list from the file
		storage.openFileAndRetrieveTaskList();
	}

	private static void executeCommandsUntilExit() {
		// read the command and execute the command
		Scanner sc = new Scanner(System.in);
		String command;

		//printInputCommand
		while (sc.hasNextLine()) {
			command = sc.nextLine();
			determineCommand(command);
			//printInputCommand
		}
	}

	private static void determineCommand(String command) {
		// determine which function should be called
		ca = new CommandAnalyzer(command);
		Command analyzedCommand = ca.analyzeCommand();
		ce = new CommandExecuter(analyzedCommand);
		ce.executeCommand();
	}
}


