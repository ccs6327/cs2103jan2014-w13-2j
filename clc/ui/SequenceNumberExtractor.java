package clc.ui;

import java.util.ArrayList;
import java.util.Scanner;

import clc.common.InvalidInputException;
import static clc.common.Constants.*;

public class SequenceNumberExtractor extends Analyzer{
	
	protected SequenceNumberExtractor(String input) {
		super(input);
	}
	
	protected static ArrayList<Integer> getSequenceNum() throws InvalidInputException {
		return parseDetailsToSequenceNum();
	}

	private static ArrayList<Integer> parseDetailsToSequenceNum() throws InvalidInputException {
		ArrayList<Integer> taskSeqNo = new ArrayList<Integer>();
		Scanner sc = new Scanner(commandDetails);
		while (sc.hasNext()) {
			String currWord = sc.next();
			if (isNumeric(currWord)) {
				if (!taskSeqNo.contains(Integer.parseInt(currWord))) { //avoid duplicate
					taskSeqNo.add(Integer.parseInt(currWord));
				}
			} else {
				sc.close();
				throw new InvalidInputException(MESSAGE_INVALID_FORMAT);
			}
		}
		sc.close();
		return new ArrayList<Integer>(taskSeqNo);
	}
}
