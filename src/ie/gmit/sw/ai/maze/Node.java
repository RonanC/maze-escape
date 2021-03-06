package ie.gmit.sw.ai.maze;

import java.awt.Color;

/**
 * There is a node for every space on the game board. Keeps track of what item
 * is on a this coordinate.
 * 
 * Also keeps track of various other things like if it's on bomb fire or was on
 * bomb fire, or is lit up via a helper.
 * 
 * Used for search algorithms to set parent and set visited.
 * 
 * @author Ronan
 */
public class Node {
	private static final int MAX_EXITS = 4; // NS EW

	public enum NodeType {
		Wall, Passage
	};

	private Node parent;
	private Color color = Color.BLACK; // black color
	private NodeType type = NodeType.Wall; // default is wall
	public boolean visited = false;
	public boolean goal;
	private int row = -1;
	private int col = -1;
	private int distance;

	private boolean helperPath = false;
	private boolean explosion = false;
	private boolean burnt = false;
	private int helperNum = -1;

	private char element; // feature

	public int getHelperNum() {
		return helperNum;
	}

	public void setHelperNum(int helperNum) {
		this.helperNum = helperNum;
	}

	public void setGoal(boolean goal) {
		this.goal = goal;
	}

	public boolean isBurnt() {
		return burnt;
	}

	public void setBurnt(boolean burnt) {
		this.burnt = burnt;
	}

	public char getElement() {
		return element;
	}

	public void setElement(char element) {
		this.element = element;
	}

	public void setHelperPath(boolean onPath) {
		this.helperPath = onPath;
	}

	public boolean getHelperPath() {
		return helperPath;
	}

	public boolean isExplosion() {
		return explosion;
	}

	public void setExplosion(boolean explosion) {
		this.explosion = explosion;
	}

	public Node(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public Node(int row, int col, char element) {
		this.row = row;
		this.col = col;
		this.element = element;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Pass the maze in, get a Node array back.
	 * 
	 * @param maze
	 * @return
	 */
	public Node[] children(Node[][] maze) {
		// Gets all four children available.
		Node[] children = new Node[MAX_EXITS];
		if (col - 1 >= 0 && maze[row][col - 1].getElement() != 'w')
			children[0] = maze[row][col - 1]; // A West edge
		if (col + 1 < maze[row].length && maze[row][col + 1].getElement() != 'w')
			children[1] = maze[row][col + 1]; // An East Edge
		if (row - 1 >= 0 && maze[row - 1][col].getElement() != 'w')
			children[2] = maze[row - 1][col]; // A North edge
		if (row + 1 < maze.length && maze[row + 1][col].getElement() != 'w')
			children[3] = maze[row + 1][col]; // An South Edge

		int counter = 0;
		for (int i = 0; i < children.length; i++) { // goes through each child
			if (children[i] != null) // if child is not empty
				counter++; // increment counter
		}

		// this makes sure the array length is correct (2, 3 or 4)
		Node[] tmp = new Node[counter]; // array with length of 2, 3 or 4
		int index = 0;
		for (int i = 0; i < children.length; i++) {// goes through each child
			if (children[i] != null) { // copy all children into tmp
				tmp[index] = children[i];
				index++;
			}
		}

		return tmp;
	}

	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.color = Color.BLUE;
		this.visited = visited;
	}

	public boolean isGoalNode() {
		return goal;
	}

	public void setGoalNode(boolean goal) {
		this.goal = goal;
	}

	/**
	 * Gets the distance between where we are and where the goal is.
	 * 
	 * @param goal
	 * @return
	 */
	public int getHeuristic(Node goal) {
		double x1 = this.col;
		double y1 = this.row;
		double x2 = goal.getCol();
		double y2 = goal.getRow();
		return (int) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}

	public int getPathCost() {
		return distance;
	}

	public void setPathCost(int distance) {
		this.distance = distance;
	}
}
