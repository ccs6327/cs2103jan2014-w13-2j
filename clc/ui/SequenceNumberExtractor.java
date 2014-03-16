package clc.ui;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

public class SequenceNumberExtractor extends Analyzer{
	
	protected SequenceNumberExtractor(String input) {
		super(input);
	}
	
	protected static ArrayList<Integer> getSequenceNum() {
		return parseDetailsToSequenceNum();
	}

	private static ArrayList<Integer> parseDetailsToSequenceNum() {
		TreeSet<Integer> taskSeqNo = new TreeSet<Integer>();
		Scanner sc = new Scanner(commandDetails);
		while (sc.hasNext()) {
			String currWord = sc.next();
			if (isNumeric(currWord)) {
				taskSeqNo.add(Integer.parseInt(currWord));
			} else {
				System.out.println("invalid format");
			}
		}
		sc.close();
		return new ArrayList<Integer>(taskSeqNo);
	}
}
