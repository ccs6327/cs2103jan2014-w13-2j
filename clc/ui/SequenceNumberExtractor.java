package clc.ui;

import java.util.ArrayList;
import java.util.Scanner;

import clc.common.InvalidInputException;
import static clc.common.Constants.ERROR_CONTAIN_NON_NUMERIC_INFO;
import static clc.common.Constants.ERROR_EMPTY_COMMAND_DETAILS;
import static clc.common.Constants.SPACE;

public class SequenceNumberExtractor extends Analyzer{
	

	protected SequenceNumberExtractor(String input) {
		super(input);
	}
	
	protected static ArrayList<Integer> getSequenceNum() throws InvalidInputException {
		return parseDetailsToSequenceNum();
	}

	private static ArrayList<Integer> parseDetailsToSequenceNum() throws InvalidInputException {
		ArrayList<Integer> taskSeqNo = new ArrayList<Integer>();
		
		determineIfCommandDetailsExist();
		
		Scanner sc = new Scanner(commandDetails);
		while (sc.hasNext()) {
			String currWord = sc.next();
			if (isNumeric(currWord)) {
				if (!taskSeqNo.contains(Integer.parseInt(currWord))) { //avoid duplicate
					taskSeqNo.add(Integer.parseInt(currWord));
				}
			} else {
				sc.close();
				throw new InvalidInputException(ERROR_CONTAIN_NON_NUMERIC_INFO);
			}
		}
		sc.close();
		return new ArrayList<Integer>(taskSeqNo);
	}

	private static void determineIfCommandDetailsExist()
			throws InvalidInputException {
		if (!doesCommandDetailsExist()) {
			throw new InvalidInputException(ERROR_EMPTY_COMMAND_DETAILS);
		}
	}

	private static boolean doesCommandDetailsExist() {
		return !commandDetails.equals(SPACE);
	}
}
