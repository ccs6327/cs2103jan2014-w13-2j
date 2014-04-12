//@author A0105712U
package clc.common;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static clc.common.Constants.*;

public class LogHelper {
	
	private static boolean isPrintedInConsole = true;
	
	public static void enablePrintInConsole() {
		isPrintedInConsole = true;
	}
	
	public static void disablePrintInConsole() {
		isPrintedInConsole = false;
	}
	
	public static void log(Level level, String message) {
		try {
			FileHandler fileHandler = new FileHandler(LOG_FILE, true);
			Logger logger = Logger.getLogger("log");
			logger.setUseParentHandlers(isPrintedInConsole);
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
		    fileHandler.setFormatter(formatter);  
		    StackTraceElement caller = new Throwable().getStackTrace()[1];
			logger.logp(level, caller.getClassName(), caller.getMethodName(), message);
			fileHandler.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void info(String message) {
		try {
			FileHandler fileHandler = new FileHandler(LOG_FILE, true);
			Logger logger = Logger.getLogger("log");
			logger.setUseParentHandlers(isPrintedInConsole);
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
		    fileHandler.setFormatter(formatter);  
		    StackTraceElement caller = new Throwable().getStackTrace()[1];
			logger.logp(Level.INFO, caller.getClassName(), caller.getMethodName(), message);
			fileHandler.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void all(String message) {
		try {
			FileHandler fileHandler = new FileHandler(LOG_FILE, true);
			Logger logger = Logger.getLogger("log");
			logger.setUseParentHandlers(isPrintedInConsole);
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
		    fileHandler.setFormatter(formatter);  
		    StackTraceElement caller = new Throwable().getStackTrace()[1];
			logger.logp(Level.ALL, caller.getClassName(), caller.getMethodName(), message);
			fileHandler.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void config(String message) {
		try {
			FileHandler fileHandler = new FileHandler(LOG_FILE, true);
			Logger logger = Logger.getLogger("log");
			logger.setUseParentHandlers(isPrintedInConsole);
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
		    fileHandler.setFormatter(formatter);  
		    StackTraceElement caller = new Throwable().getStackTrace()[1];
			logger.logp(Level.CONFIG, caller.getClassName(), caller.getMethodName(), message);
			fileHandler.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void fine(String message) {
		try {
			FileHandler fileHandler = new FileHandler(LOG_FILE, true);
			Logger logger = Logger.getLogger("log");
			logger.setUseParentHandlers(isPrintedInConsole);
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
		    fileHandler.setFormatter(formatter);  
		    StackTraceElement caller = new Throwable().getStackTrace()[1];
			logger.logp(Level.FINE, caller.getClassName(), caller.getMethodName(), message);
			fileHandler.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void finer(String message) {
		try {
			FileHandler fileHandler = new FileHandler(LOG_FILE, true);
			Logger logger = Logger.getLogger("log");
			logger.setUseParentHandlers(isPrintedInConsole);
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
		    fileHandler.setFormatter(formatter);  
		    StackTraceElement caller = new Throwable().getStackTrace()[1];
			logger.logp(Level.FINER, caller.getClassName(), caller.getMethodName(), message);
			fileHandler.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void finest(String message) {
		try {
			FileHandler fileHandler = new FileHandler(LOG_FILE, true);
			Logger logger = Logger.getLogger("log");
			logger.setUseParentHandlers(isPrintedInConsole);
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
		    fileHandler.setFormatter(formatter);  
		    StackTraceElement caller = new Throwable().getStackTrace()[1];
			logger.logp(Level.FINEST, caller.getClassName(), caller.getMethodName(), message);
			fileHandler.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void off(String message) {
		try {
			FileHandler fileHandler = new FileHandler(LOG_FILE, true);
			Logger logger = Logger.getLogger("log");
			logger.setUseParentHandlers(isPrintedInConsole);
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
		    fileHandler.setFormatter(formatter);  
		    StackTraceElement caller = new Throwable().getStackTrace()[1];
			logger.logp(Level.OFF, caller.getClassName(), caller.getMethodName(), message);
			fileHandler.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void severe(String message) {
		try {
			FileHandler fileHandler = new FileHandler(LOG_FILE, true);
			Logger logger = Logger.getLogger("log");
			logger.setUseParentHandlers(isPrintedInConsole);
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
		    fileHandler.setFormatter(formatter);  
		    StackTraceElement caller = new Throwable().getStackTrace()[1];
			logger.logp(Level.SEVERE, caller.getClassName(), caller.getMethodName(), message);
			fileHandler.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void warning(String message) {
		try {
			FileHandler fileHandler = new FileHandler(LOG_FILE, true);
			Logger logger = Logger.getLogger("log");
			logger.setUseParentHandlers(isPrintedInConsole);
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
		    fileHandler.setFormatter(formatter);  
		    StackTraceElement caller = new Throwable().getStackTrace()[1];
			logger.logp(Level.WARNING, caller.getClassName(), caller.getMethodName(), message);
			fileHandler.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
