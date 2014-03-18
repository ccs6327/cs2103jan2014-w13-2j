package clc.ui;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

import clc.common.InvalidInputException;

public class SequenceNumberExtractor extends Analyzer{
	
	protected SequenceNumberExtractor(String input) {
		super(input);
	}
	
	protected static ArrayList<Integer> getSequenceNum() throws InvalidInputException {
		return parseDetailsToSequenceNum();
	}

	private static ArrayList<Integer> parseDetailsToSequenceNum() throws InvalidInputException {
		TreeSet<Integer> taskSeqNo = new TreeSet<Integer>();
		Scanner sc = new Scanner(commandDetails);
		while (sc.hasNext()) {
			String currWord = sc.next();
			if (isNumeric(currWord)) {
				taskSeqNo.add(Integer.parseInt(currWord));
			} else {
				sc.close();
				throw new InvalidInputException();
			}
		}
		sc.close();
		return new ArrayList<Integer>(taskSeqNo);
	}
}
