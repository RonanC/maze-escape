package ie.gmit.sw.ai.traversers.uninformed;

import java.util.Random;

import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

public class RandomWalk implements Traversator {
	/*
	 * Sets the current node to visited, then chooses a random child node and
	 * moves to it
	 * 
	 * low memory, high time
	 */
	private long time;
	private int visitCount;
	private int steps;
	private boolean complete;
	private Node currentNode;
	private Node[][] mazeArray;
	
	private Node goal;


	public RandomWalk(Node[][] maze, int row, int col) {
		init(maze, maze[row][col]);
	}
	
	public void init(Node[][] maze, Node node) {
		this.mazeArray = maze;
		this.currentNode = node;
		time = System.currentTimeMillis();
		visitCount = 0;

		steps = (int) Math.pow(maze.length, 2) * 2;
		System.out.println("Number of steps allowed: " + steps);

		complete = false;
		
		setGoalNodeRand();
	}

	public int findNextMove() { // traverse one step
		if (visitCount <= steps && currentNode != null) {
			currentNode.setVisited(true); // this is what changes the color of the node
			visitCount++;

			if (currentNode.isGoalNode()) {
				time = System.currentTimeMillis() - time; // Stop the clock
				TraversatorStats.printStats(currentNode, time, visitCount);
				complete = true;
				return 4; // found goal
			} else {
				try { // Simulate processing each expanded node
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// Pick a random adjacent node
				Node[] children = currentNode.children(mazeArray); // get all children
				// choose a random child
				int choice = (int) (children.length * Math.random());
				// System.out.println("children choice: " + choice);
				try {
					currentNode = children[choice];
				} catch (Exception e) {
					System.out.println("error:" + e.getMessage());
				}

				return choice; // 0, 1, 2, 3
			}
		}

		return 5; // out of steps

	}
	
	// Pick a goal node
	public void setGoalNodeRand() {
		int randRow;
		int randCol;
		do {
			Random generator = new Random();
			randRow = generator.nextInt(mazeArray.length);
			randCol = generator.nextInt(mazeArray[0].length);
		} while (mazeArray[randRow][randCol].getElement() != 'w');

		mazeArray[randRow][randCol].setGoalNode(true);
		goal = mazeArray[randRow][randCol];
	}

	public void setGoalNode(int row, int col) {
		mazeArray[row][col].setGoalNode(true);
		goal = mazeArray[row][col];
	}

	public Node getGoalNode() {
		return goal;
	}


}
