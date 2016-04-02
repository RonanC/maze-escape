package ie.gmit.sw.ai.traversers;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

import ie.gmit.sw.ai.characters.Player;
import ie.gmit.sw.ai.maze.Node;

/**
 * Abstract super class for the search algorithms.
 * 
 * @author Ronan
 */
public abstract class Traversator implements Runnable {
	protected long time;
	protected int visitCount;
	protected int steps;
	protected boolean complete;
	protected Node currentNode;
	protected Node[][] mazeArray;
	protected Node goal;
	protected int[] newPos;
	protected Player player;

	protected boolean keepRunning;
	protected Deque<int[]> allPositions;

	// run search algorithm in different thread
	protected final Thread t;
	protected volatile boolean shouldStop = false;

	public Traversator(Node[][] maze, int row, int col, Player player) {
		this.player = player;
		t = new Thread(this, "IDDFS Thread");

		// create deep copy of nodes so that different enemies do not disturb
		// each others nodes.
		Node[][] newMaze = new Node[maze.length][maze[0].length];
		for (int i = 0; i < newMaze.length; i++) {
			for (int j = 0; j < newMaze.length; j++) {
				newMaze[i][j] = new Node(i, j);
				newMaze[i][j].setElement(maze[i][j].getElement());
			}
		}

		init(newMaze, row, col);
	}
	
	public void init(Node[][] maze, int row, int col) {
		this.mazeArray = maze;
		this.currentNode = mazeArray[row][col];
		allPositions = new LinkedList<int[]>();
		keepRunning = true;
		time = System.currentTimeMillis();
		visitCount = 0;
		// complete = false;
		// setGoalNodeRand();
		if (player != null) {
			setPlayerAsGoal(); // only for enemies
		}

		newPos = new int[2];
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public Node getGoal() {
		return goal;
	}

	public void setGoal(Node goal) {
		this.goal = goal;
	}

	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}



	public Deque<int[]> getAllPositions() {
		return allPositions;
	}

	public int[] getPos() {
		int[] position = new int[2];
		position[0] = currentNode.getRow();
		position[1] = currentNode.getCol();
		return newPos;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public void resetAndSetGoal() {
		resetGraph();
		setComplete(false);
		setPlayerAsGoal();
	}

	public void resetGraph() {
		for (Node[] nodes : mazeArray) {
			for (Node node : nodes) {
				node.setParent(null);
				node.setVisited(false);
			}
		}
	}

	public void setPlayerAsGoal() {
		setGoalNode(player.getTileY(), player.getTileX());
	}

	public abstract int[] findNextMove();

	public void resetNewPos() {
		newPos[0] = -1;
		newPos[1] = -1;
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

	@Override
	public void run() {
		if (!shouldStop) {
			initCustom();
			shouldStop = true;
		}
	}

	protected abstract void initCustom();
}
