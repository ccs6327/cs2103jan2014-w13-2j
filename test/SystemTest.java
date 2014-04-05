package test;

/**
 * This is the overall test for the whole system
 * 
 */

import static org.junit.Assert.*;
import clc.ui.UserInterface;
import clc.storage.Storage;
import clc.logic.Task;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class SystemTest {
	ArrayList < String > inputList = new ArrayList < String >();
	ArrayList < String > feedbackList = new ArrayList < String >();
	String actualFeedback = null;
	UserInterface start = new UserInterface();
	Calendar date = Calendar.getInstance();
	ArrayList<Integer> displayMem = Storage.getDisplayMem();
	ArrayList<Task> internalMem = Storage.getInternalMem();
	@Before
	public void setUp() throws Exception {
		// Test case 1: Add
		inputList.add("add buy ticket 02/04 2pm");
		feedbackList.add("buy ticket(ending at Wed, 2 Apr 2014 2.00 pm) is added.");
        
		// Test case 2: Update
		date.set(2014, 3, 2, 14, 0, 0);
		//System.out.println(date.getTime());
		displayMem.clear();
		internalMem.add(new Task("buy ticket", date));
		displayMem.add(internalMem.size());
		inputList.add("update 1 , 03/04");
		feedbackList.add("Task No.1 [buy ticket] end  time is updated to [Thu, 3 Apr 2014 2.00 PM] successful.\n\n");
		
		// Test case 3: Delete 
		inputList.add("delete 1");
		feedbackList.add("[buy ticket] is deleted.\n");
		
		// Test case 4: Undo
		inputList.add("undo");
		feedbackList.add("Last command has been undone.");
			
		//Test case 6: Mark
		inputList.add("mark 1");
		feedbackList.add("mark [buy ticket] as done sucessfuly.");
		
		//Test case 7: Invalid Command 
		inputList.add("unso");
		feedbackList.add("Invalid command format");
	}
	
	@Test
	public void test() {	
		for(int i = 0; i < inputList.size(); i++) {
			//actualFeedback = start.setInputAndExecute(inputList.get(i));
			//System.out.println(actualFeedback);
			//System.out.println(feedbackList.get(i));
			assertEquals(feedbackList.get(i), actualFeedback);
			System.out.println("Test case " + (i+1) + " passed!");	
		}
	}
}
