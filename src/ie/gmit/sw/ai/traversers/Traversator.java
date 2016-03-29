package ie.gmit.sw.ai.traversers;

import java.util.Random;

import ie.gmit.sw.ai.maze.Node;

public abstract class Traversator {
	protected long time;
	protected int visitCount;
	protected int steps;
	protected boolean complete;
	protected Node currentNode;
	protected Node[][] mazeArray;
	protected Node goal;

	public Traversator(Node[][] maze, int row, int col) {
		init(maze, maze[row][col]);
	}

	public abstract int findNextMove();

	public void init(Node[][] maze, Node node) {
		this.mazeArray = maze;
		this.currentNode = node;
		time = System.currentTimeMillis();
		visitCount = 0;
		complete = false;
		setGoalNodeRand();
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
