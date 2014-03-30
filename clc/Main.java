package clc;


import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

import clc.storage.Storage;
import clc.ui.UserInterface;

public class Main {

	public static void main(String[] args) { 
		checkIfRunning();
		UserInterface userInterface = new UserInterface();
		Storage.initializeDataFile();
		userInterface.executeCommandsUntilExit();	
	}
	
	private static void checkIfRunning() {
		try { 
			new ServerSocket(6327); 
			//cannot construct if it is previously construct by the application
			//not the best way **
		}
		catch (BindException e) {
			System.err.println("Already running.");
			System.exit(1);
		}
		catch (IOException e) {
			System.err.println("Unexpected error.");
			e.printStackTrace();
			System.exit(2);
		} 
	} 
}
