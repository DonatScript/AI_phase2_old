// KING SAUD UNIVERSITY
// CCIS
// CSC 361

// NAME:  ...................
// ID: ...................

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class Search {

	// CONSTANTS:
	private static final int CLOSED_MAX_SIZE = 100000;
	private static final int FRINGE_MAX_SIZE = 100000;

	// ATTRIBUTES:
	private Node root; // the root node
	private Queue<Node> fringe; // the BFS frings: change it // for the other
								// searches.
	private Node goal; // the goal node
	private int numNodesExpanded; // number of nodes expanded

	// ALTHOUGH YOU ARE NOT REQUIRED TO, BUT IT IS
	// HIGHLY RECOMMENDED THAT YOU IMPLEMENT THE
	// CLOSED LIST MECHANISM (THE GRAPH SEARCH).
	// THE NEED FOR IT IS DUE TO THE VERY LARGE NUMBER OF
	// REDUNDANT STATES THAT YOU WILL GENERATE BEFORE
	// REACHING THE GOAL.
	// THE FOLLOWING IS A SIMPLE IMPLEMENTATION OF THE CLOSED
	// LIST, REPRESENTED AS A SIMPLE ARRAY OF NODES:

	private Node closed[]; // the closed list containing // visited nodes
	private int n_closed; // size of closed list

	// CONSTRUCTOR 1:
	// THIS CONSTRUCTOR WILL CREATE A SEARCH OBJECT.
	Search(State init_state) {
		root = new Node(init_state, null, 0, -1, 0); // make the root node
		fringe = new ArrayBlockingQueue<>(FRINGE_MAX_SIZE); // initialize Queue
		closed = null;
		n_closed = 0;
 		goal = null;
		numNodesExpanded = 0;

		// ...

	}

	// THIS METHOD INITIALIZES THE CLOSED LIST
	private void initialize_closed() {
		if (closed == null)
			closed = new Node[CLOSED_MAX_SIZE];
		n_closed = 0;
	}

	// THIS METHOD TESTS WHETHER THE NODE WAS
	// VISITED OR NOT USING A SIMPLE FOR LOOP.
	// YOU CAN CHANGE IT.
	private boolean visited(Node n) {
		for (int i = 0; i < n_closed; i++) {
			if(closed[i].equals(n))
				return true;
		}
		return false;
	}

	// THIS METHOD ADDS A NODE TO THE CLOSED LIST.
	// IT SIMPLY ADDS IT TO THE ND OF THE LIST. YOU
	// CAN CHANGE IT TO A MORE SOPHISTICATED METHOD.
	private void mark_as_visited(Node n) {
		// if the list is full do a left shift:
		if (n_closed == CLOSED_MAX_SIZE) {
			for (int i = 0; i < CLOSED_MAX_SIZE - 1; i++)
				closed[i] = closed[i + 1];
		} else
			n_closed++;
		closed[n_closed - 1] = n;
	}

	// THIS METHOD WILL DO THE SEARCH AND CAN
	// RETURN THE GOAL NODE. YOU CAN EXTRACT
	// THE SOLUTION BY FOLLOWING THE PARENT NODES
	public Node doSearch() {

		numNodesExpanded = 0;
		Node nodesList[];
		Node current = root;

		fringe.offer(current);
		while (!fringe.isEmpty()) {
			current = fringe.poll();
			
			if (current.isGoal()) {
				return current;
			}
			nodesList = current.expand();
			numNodesExpanded++;
			if(!visited(current))
				mark_as_visited(current);
			for (int i = 0; i < 5; i++) { // we have 5 actions
				if (nodesList[i] != null)
					fringe.offer(nodesList[i]);
			}
		}
		return null; // goal not found

	}

	//recursive method
	public Node doSearchRecursive() {
		initialize_closed();
		numNodesExpanded = 0;
		Node current = root;
		Node nodesList[] = current.expand();
		numNodesExpanded++;
		return recDoSerch(current, nodesList);
	}

	private Node recDoSerch(Node current, Node nodesList[]) {
		if(!visited(current))
			mark_as_visited(current);
		if (current.isGoal())
			return current;
		if (!fringe.isEmpty()) {
			nodesList = current.expand();
			numNodesExpanded++;
			for (int i = 0; i < 5; i++) { // we have 5 actions
				if (nodesList[i] != null)
					fringe.offer(nodesList[i]);
				current = fringe.poll();
				i++;
				return recDoSerch(current, nodesList);
			}
		}
		return null;
	}

	// GIVEN THE GOAL NODE, THIS METHOD WILL EXTRACT
	// THE SOLUTION, WHICH IS A SEQUENCE OF ACTIONS.
	public String[] extractSolution(Node goalNode) {

		// first find solution length;
		int len = 0;
		Node n = goalNode;
		while (n != null) {
			n = n.getParent();
			len++;
		}

		// declare an array of strings
		String sol[] = new String[len -1];//[len]

		n = goalNode;
		for (int i = len - 2; i >= 0; i--) {
			switch (n.getAction()) {
			case 0:
				sol[i] = new String("move-N");
				break;
			case 1:
				sol[i] = new String("move-S");
				break;
			case 2:
				sol[i] = new String("move-W");
				break;
			case 3:
				sol[i] = new String("move-E");
				break;
			case 4:
				sol[i] = new String("recharge");
			}
			n = n.getParent();
		}
		return sol;
	}

	// THIS METHOD WILL DISPLAY THE SOLUTION
	public void displaySolution(Node goalNode) {
		String sol[] = extractSolution(goalNode);
		for (int i = 0; i < sol.length; i++)
			System.out.println(sol[i]);
	}
}
