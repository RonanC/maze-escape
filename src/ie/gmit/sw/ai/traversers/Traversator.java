package ie.gmit.sw.ai.traversers;

import java.util.Random;

import ie.gmit.sw.ai.characters.Player;
import ie.gmit.sw.ai.maze.Node;

public abstract class Traversator {
	protected long time;
	protected int visitCount;
	protected int steps;
	protected boolean complete;
	protected Node currentNode;
	protected Node[][] mazeArray;
	protected Node goal;
	protected int[] newPos;
	protected Player player;

	public int[] getPos(){
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

	public Traversator(Node[][] maze, int row, int col, Player player) {
		this.player = player;
		
		// create deep copy of nodes so that different enemies do not disturb each others nodes.
		Node[][] newMaze = new Node[maze.length][maze[0].length];
		for (int i = 0; i < newMaze.length; i++) {
			for (int j = 0; j < newMaze.length; j++) {
				newMaze[i][j] = new Node(i, j);
				newMaze[i][j].setElement(maze[i][j].getElement());;
			}
		}
		
		init(newMaze, row, col);
	}
	

	public void resetAndSetGoal(){
		resetGraph();
		setComplete(false);
		setPlayerAsGoal();
	}
	
	public void resetGraph(){
		for (Node[] nodes : mazeArray) {
			for (Node node : nodes) {
				node.setParent(null);
				node.setVisited(false);
			}
		}
	}
	
	public void setPlayerAsGoal(){
		setGoalNode(player.getTileY(), player.getTileX());
	}
	

	public abstract int[] findNextMove();

	public void init(Node[][] maze, int row, int col) {
		this.mazeArray = maze;
		this.currentNode = mazeArray[row][col];
		time = System.currentTimeMillis();
		visitCount = 0;
		complete = false;
		setGoalNodeRand();
		newPos = new int[2];
	}
	
	public void resetNewPos(){
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
}
