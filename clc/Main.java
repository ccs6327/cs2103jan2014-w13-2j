package clc;


import clc.storage.Storage;
import clc.ui.UserInterface;

public class Main {
	public static void main(String[] args) {
		UserInterface userInterface = new UserInterface();
		Storage.initializeDataFile();
		userInterface.executeCommandsUntilExit();	
	}
}
