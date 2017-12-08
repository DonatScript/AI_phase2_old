// KING SAUD UNIVERSITY
// CCIS
// CSC 361

// NAME:  ...................
// ID: ...................

import java.io.*;
import java.util.*;

public class Agent {

	public static void main(String[] args) throws FileNotFoundException {

		State s1 = new State("simple2.map.txt");
		s1.run("simple.map.command.txt", "finalMap.txt", "logsFile.txt");
		Search s = new Search(s1);
		Node n = s.doSearchRecursive();
		s.displaySolution(n);
		
		int n_args = args.length;
		if (n_args!=5) {
			System.out.println("ERROR: ILLEGAL NUMBER OF ARGUMENTS:");
			System.out.println("Number of arguments must be 5.");
			return;
		}
		String mode = args[0];
		if (!mode.equals("s") && !mode.equals("c")) {
			System.out.println("ERROR: ILLEGAL MODE:");
			System.out.println("Mode must be 's' or 'c'.");
			return;
		}
		if (mode.equals("c")) {
			String MapFile = args[1];
			String CommandFile = args[2];
			String finalMapFile = args[3];
			String logFile = args[4];
			
			// ... phase 1 code here ...
			
		}
		else
		if (mode.equals("s")) {
			int na = Integer.valueOf(args[1]);
			String MapFile = args[2];
			String actionsFile = args[3];
			String finalMapFile = args[4];
			
			// ... phase 2 code here ...
			
		}

		// WRITE YOUR EXTRA CODE DOWN HERE:
		
		// ...
		
		
		
	}


	
}

