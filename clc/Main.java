//@author A0112089J
package clc;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

import clc.common.LogHelper;
import clc.storage.Storage;
import clc.ui.UserInterface;

public class Main {
	
	public static void main(String[] args) { 
		LogHelper.info("CLC launched.");
		checkIfRunning();
		UserInterface userInterface = new UserInterface();
		Storage.initializeDataFile();
		userInterface.executeCommandsUntilExit();	
	}
	
	private static void checkIfRunning() {
		try { 
			//if the socket is used by other application, software cannot be launched
			new ServerSocket(6327);
		}
		catch (BindException e) {
			LogHelper.warning("Already running");
			System.exit(1);
		}
		catch (IOException e) {
			LogHelper.warning("Unexpected error");
			e.printStackTrace();
			System.exit(2);
		} 
	} 
}
