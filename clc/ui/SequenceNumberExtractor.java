//@author A0112089J

package clc.ui;

import java.util.ArrayList;
import java.util.Scanner;

import clc.common.InvalidInputException;
import clc.common.LogHelper;
import static clc.common.Constants.ERROR_CONTAIN_NON_NUMERIC_INFO;

public class SequenceNumberExtractor extends Analyzer{
	

	protected SequenceNumberExtractor(String input) {
		super(input);
	}
	
	protected static ArrayList<Integer> getSequenceNum() throws InvalidInputException {
		return parseDetailsToSequenceNum();
	}

	private static ArrayList<Integer> parseDetailsToSequenceNum() throws InvalidInputException {
		ArrayList<Integer> taskSeqNo = new ArrayList<Integer>();
		
		throwExceptionIfEmptyCommandDetails();
		
		Scanner sc = new Scanner(commandDetails);
		parseSeqNoIntoArrayList(taskSeqNo, sc);
		sc.close();
		
		return new ArrayList<Integer>(taskSeqNo);
	}

	private static void parseSeqNoIntoArrayList(ArrayList<Integer> taskSeqNo,
			Scanner sc) throws InvalidInputException {
		while (sc.hasNext()) {
			String currWord = sc.next();
			if (isNumeric(currWord)) {
				int seqNo = Integer.parseInt(currWord);
				if (!taskSeqNo.contains(seqNo)) { //avoid duplication
					taskSeqNo.add(seqNo);
				}
				LogHelper.info("'" + currWord + "'" + " is numeric");
			} else {
				sc.close();
				LogHelper.info("'" + currWord + "'" + " is non-numeric");
				throw new InvalidInputException(ERROR_CONTAIN_NON_NUMERIC_INFO);
			}
		}
	}
}
