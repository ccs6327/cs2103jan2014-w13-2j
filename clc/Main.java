package clc;


import clc.ui.UserInterface;

public class Main {
	public static void main(String[] args) {
		UserInterface userInterface = new UserInterface();
		userInterface.printWelcomeMessage();
		userInterface.executeCommandsUntilExit();	
	}
}
