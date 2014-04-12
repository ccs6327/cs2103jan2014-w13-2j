//@author A0112089J
package clc;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

import clc.common.LogHelper;
import clc.storage.Storage;
import clc.ui.UserInterface;

import static clc.common.Constants.*;

public class Main {
	
	public static void main(String[] args) { 
		LogHelper.info(CLC_LAUNCHED);
		checkIfRunning();
		UserInterface userInterface = new UserInterface();
		Storage.initializeDataFile();
		userInterface.executeCommandsUntilExit();	
	}
	
	private static void checkIfRunning() {
		try { 
			//if the socket is used by other application, software cannot be launched
			new ServerSocket(PORT);
		}
		catch (BindException e) {
			LogHelper.warning(ALREADY_RUNNING);
			System.exit(1);
		}
		catch (IOException e) {
			LogHelper.warning(UNEXPECTED_ERROR);
			e.printStackTrace();
			System.exit(2);
		} 
	} 
}
