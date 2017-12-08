// KING SAUD UNIVERSITY
// CCIS
// CSC 361

// NAME:  ...................
// ID: ...................

import java.io.*;
import java.util.*;

public class State {

	// THE FOLLOWING IS AN EXAMPLE OF THE ATTRIBUTES
	// THAT YOU NEED TO PUT IN A STATE.
	// YOU SHOULD CHANGE IT:

	private int x; // THIS IS X (MAYBE NOT NEEDED)
	private int y; // THIS IS Y (MAYBE NOT NEEDED)
	private char[][] map; // THE MAP
	private int N;
	private int M;
	private int battery;

	// -----------------------------

	// THE FOLLOWING ARE THE CONSTRUCTORS
	// YOU CAN CHANGE OR REPALCE THEM.


	// CONSTRUCTOR 1:
	// THIS CONSTRUCTOR WILL CREATE A STATE FROM FILE.
	State(String fileName) throws FileNotFoundException {
		Scanner in = new Scanner(new File(fileName));
		in.useDelimiter("\n| \n");
		N = in.nextInt();
		M = in.nextInt();
		battery = new Random().nextInt(N*M);//select random number for energy battery
		String str = "";
		while (in.hasNext()) {
			str += in.next();
		}
		map = new char[N][M];
		int sizeArray = N * M;
		int s = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (s <= sizeArray - 1)
					map[i][j] = str.charAt(s++);
				if (map[i][j] == 'R') {
					x = i;
					y = j;
				}
			}
		}
		in.close();
	}

	// CONSTRUCTOR 2:
	// THIS CONSTRUCTOR WILL CREATE A RANDOM STATE.
	State(int n, int m, int rseed) {
		Random r = new Random(rseed);
		char c[] = { ' ', 'B', 'H', 'C' };
		N = n;
		M = m;
		battery = new Random().nextInt(N*M);//////////////////////////////////////
		map = new char[N][M];
		for (int i = 0; i < N - 1; i++)
			for (int j = 0; j < M - 1; j++)
				map[i][j] = c[r.nextInt(c.length)];
		map[r.nextInt(N)][r.nextInt(M)] = 'T';
		x = r.nextInt(N);
		y = r.nextInt(M);
		map[x][y] = 'R';
	}

	// CONSTRUCTOR 3:
	// COPY CONSTRUCTOR.
	State(State s) {
		x = s.x;
		y = s.y;
		N = s.getN();
		M = s.getM();
		battery = s.battery;
		map = new char[N][M];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < M; j++)
				map[i][j] = s.map[i][j];

		// ...
	}

	// -----------------------------

	// STATE GETTERS AND SETTERS
	// YOU CAN & SHOULD CHANGE THEM.
	// IF YOU HAVE QUESTIONS ASK THE INSTRUCTOR.
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public char[][] getMap() {
		return map;
	}
	public int getN() {
		return N;
	}

	public int getM() {
		return M;
	}
	public int getBattery() {
		return battery;
	}

	public void setBattery(int battery) {
		this.battery = battery;
	}

	// METHOD THAT TELLS WHETHER THIS STATE IS EQUAL
	// TO ANOTHER STATE.
	public boolean equal(State s) {
		int n = s.getN();
		int m = s.getM();
		if (battery != s.battery)
			return false;
		if (N * M != n * m)
			return false;
		for (int i = 0; i < n; i++)
			for (int j = 0; j < m; j++)
				if (map[i][j] != s.getMap()[i][j])
					return false;
		if ((x == s.x) && (y == s.y))
			return true;
		else
			return false;
	}

	// -----------------------------

	// THE ACTIONS:
	// YOU CAN CHANGE THE ACTIONS CONTENTS,
	// WHAT THE ACTIONS RETURN, ETC.
	public void run(String command, String finalMap, String log) {
		try {
			Scanner in = new Scanner(new File(command));
			while (in.hasNext() && battery > 0) {
				writeLogs(log, doCommandAndLog(in.next()));
				if (fallInHole())
					writeLogs(log, "HOLE");
				if (foundTreasure())
					writeLogs(log, "GOAL");

			}
			in.close();
			FileWriter f = new FileWriter(new File(finalMap));
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					f.write(map[i][j]);
				}
				f.write("\n");
			}
			f.close();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	// ACTION move_N
	// RETURNS BOOLEAN:
	// TRUE MEANS ACTION WAS APPLIED,
	// FALSE MEANS ACTOIN COULD NOT AND WAS NOT APPLIED.
	public boolean move_N() {
		if (battery == 0)
			return false;
		battery--;
		if (x == 0)
			return false;
		else if (map[x][y] == 'X' || map[x][y] == 'Z')
			return false;
		else if (map[x - 1][y] == 'B')
			return false;
		else {
			changCurrentCell();
			x--;
			changeNextCell();
			return true;
		}
	}

	public boolean move_S() {
		if (battery == 0)
			return false;
		battery--;
		if (x == N - 1)
			return false;
		else if (map[x][y] == 'X' || map[x][y] == 'Z')
			return false;
		else if (map[x + 1][y] == 'B')
			return false;
		else {
			changCurrentCell();
			x++;
			changeNextCell();
			return true;
		}
	}

	public boolean move_E() {
		if (battery == 0)
			return false;
		battery--;
		if (y == M - 1)
			return false;
		else if (map[x][y] == 'X' || map[x][y] == 'Z')
			return false;
		else if (map[x][y + 1] == 'B')
			return false;
		else {
			changCurrentCell();
			y++;
			changeNextCell();
			return true;
		}
	}

	public boolean move_W() {
		if (battery == 0)
			return false;
		battery--;
		if (y == 0)
			return false;
		else if (map[x][y] == 'X' || map[x][y] == 'Z')
			return false;
		else if (map[x][y - 1] == 'B')
			return false;
		else {
			changCurrentCell();
			y--;
			changeNextCell();
			return true;
		}
	}

	public boolean recharge() {
		if(map[x][y] == 'C'){
			battery = N*M;
			return true;			
		}
		else
			return false;
	}
	private void changeNextCell() {
		switch (map[x][y]) {
		case ' ':
			map[x][y] = 'R';
			break;
		case 'T':
			map[x][y] = 'U';
			break;
		case 'H':
			map[x][y] = 'X';
			break;
		case 'Y':
			map[x][y] = 'Z';
			break;
		case 'X':
			map[x][y] = 'X';
			break;
		}
	}

	private void changCurrentCell() {
		switch (map[x][y]) {
		case 'R':
			map[x][y] = ' ';
			break;
		case 'U':
			map[x][y] = 'T';
			break;
		case 'D':
			map[x][y] = 'C';
			break;
		case 'F':
			map[x][y] = 'E';
			break;
		}
	}
	private boolean fallInHole() {
		if (map[x][y] == 'X' || map[x][y] == 'Z')
			return true;
		else
			return false;
	}

	// YOU CAN ADD MORE ACTIONS HERE.

	// ...

	// -----------------------------

	// GOAL TEST: THIS WILL
	// TELL WHETHER THE TREASURE WAS FOUND.
	public boolean foundTreasure() {
		return map[x][y] == 'U' || map[x][y] == 'Z' || map[x][y] == 'F';
	}

	// -----------------------------

	// DISPLAY THE STATE
	public void display() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++)
				System.out.print(map[i][j]);
			System.out.println();
		}
	}

	// THIS METHOD WILL DO the GIVEN COMMAND
	// AND WILL RETURN THE LOG MESSAGE
	public String doCommandAndLog(String cmd) {
		String log = "ERROR";
		switch (cmd) {
		case "move-N":
			if (move_N())
				log = "DONE";
			else
				log = "FAIL";
			break;
		case "move-S":
			if (move_S())
				log = "DONE";
			else
				log = "FAIL";
			break;
		case "move-E":
			if (move_E())
				log = "DONE";
			else
				log = "FAIL";
			break;
		case "move-W":
			if (move_W())
				log = "DONE";
			else
				log = "FAIL";
			break;
		}
		return log;
	}

	// THIS METHOD WILL WRITE THE GIVEN LOGS INTO A FILE
	public void writeLogs(String logsFilename, String logs) {
		try {
			FileWriter b = new FileWriter(new File(logsFilename),true);
			b.write(logs + "\n");
			b.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// -----------------------------

	// THIS METHOD WILL RETURN THE SUCCESSOR STATES
	// OF COURSE, YOU CAN & SHOULD CHANGE IT
	public State[] successors() {
		State children[] = new State[5]; // we have 5 actions

		// action 1

		children[0] = new State(this);
		if (!children[0].move_E())
			children[0] = null;
		else
			System.out.println("move_E");

		// action 2

		children[1] = new State(this);
		if (!children[1].move_N())
			children[1] = null;
		else
			System.out.println("move_N");
		
		// action 3

		children[2] = new State(this);
		if (!children[2].move_W())
			children[2] = null;
		else
			System.out.println("move_W");

		// action 4

		children[3] = new State(this);
		if (!children[3].move_S())
			children[3] = null;
		else
			System.out.println("move_S");
		
		// action 5

		children[4] = new State(this);
		if (!children[4].recharge())
			children[4] = null;
		else
			System.out.println("recharge");
		
		return children;
	}
	// -----------------------------

	// ADD EXTRAS HERE ...

}
