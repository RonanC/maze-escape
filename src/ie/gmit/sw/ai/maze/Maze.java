package ie.gmit.sw.ai.maze;

import java.io.*;
import java.util.*;

import ie.gmit.sw.ai.GameRunner;

public class Maze {

	private Scanner input;
	private Node[][] maze;
	private Random random;
	private int insideNum;
	private Node goal;

	public Maze() {
		random = new Random();
		insideNum = GameRunner.MAZE_DIM - 4;
		maze = new Node[GameRunner.MAZE_DIM][GameRunner.MAZE_DIM];

		reset();
	}


	// Pick a goal node
	public void setGoalNodeRand() {
		Random generator = new Random();
		int randRow = generator.nextInt(maze.length);
		int randCol = generator.nextInt(maze[0].length);
		maze[randRow][randCol].setGoalNode(true);
		goal = maze[randRow][randCol];
	}
	
	public void setMazeArray(Node[][] mazeArray){
		this.maze = mazeArray;
	}

	public void setGoalNode(int row, int col) {
//		maze[row][col].setGoalNode(true);	// we only set this true in the actual search algorithm
		goal = maze[row][col];
	}

	public Node getGoalNode() {
		return goal;
	}

	public Node[][] getMazeArray() {
		return maze;
	}

	public Node[][] getMazeArrayClone() {
		return maze.clone(); // unique for each enemy
	}

	public void reset() {
		// readInMap();
		genRandomMaze();
		placeOuterWalls();

		// System.out.println(toString());
		// printMap();
	}

	private void initMaze(char element) {
		System.out.println(maze.length);
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {
				maze[row][col] = new Node(row, col, element);
			}
		}

		// System.out.println(toString());
	}

	private void readInMap() {
		initMaze('w');

		// read in map
		openFile();
		readFile();
		closeFile();
	}

	private void placeOuterWalls() {
		for (int row = 0; row < maze.length; row++) {
			// rows
			if (row == 0 || row == 1 || row == maze.length - 1 || row == maze.length - 2) {
				for (int col = 0; col < maze.length - 1; col++) {
					maze[row][col].setElement('w');
				}
			}
			for (int col = 0; col < 2; col++) {
				maze[row][col].setElement('w');
			}

			// left wall
			for (int col = 0; col < 2; col++) {
				maze[row][col].setElement('w');
			}

			// right wall
			for (int col = maze.length - 1; col > maze.length - 3; col--) {
				maze[row][col].setElement('w');
			}
		}
	}

	private void genRandomMaze() {
		initMaze('x');

		// changes some to space
		buildMaze();

		// items
		int itemNum = random.nextInt(GameRunner.MAZE_DIM / 5) * 2;

		int bombNum = random.nextInt(GameRunner.MAZE_DIM / 5);

		int helperNum = random.nextInt(GameRunner.MAZE_DIM / 10) * 2;
		
		if (helperNum < 4) {
			helperNum = 4;
		}
		
		if (bombNum < 4) {
			bombNum = 4;
		}
		
		if (itemNum < 4) {
			itemNum = 4;
		}

		// 10'000 spaces in a 100 by 100 maze.

		// int goalNum;
		// try {
		// goalNum = random.nextInt(GameRunner.MAZE_DIM / 50); // 2
		// } catch (Exception e) {
		// goalNum = 1;
		// }
		//
		// if (goalNum < 1) {
		// goalNum = 1;
		// }
		//
		// int goalNumber = goalNum;

		// quest items
		int goalNumber = 1; // only one goal

		// items
		addFeature('s', 'f', itemNum);
		addFeature('b', 'f', bombNum);
		addFeature('m', 'f', itemNum); // med kit

		// quest items
		addFeature('h', 'f', helperNum);
		addFeature('g', 'f', 1);

		// rest of x's should be walls
		addWalls();

		// System.out.println(toString());
	}

	private void addWalls() {
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length - 1; col++) {
				if (maze[row][col].getElement() == 'x') {
					maze[row][col].setElement('w');
				}
			}
		}
	}

	// https://www.youtube.com/watch?v=IjLmUT_is8g
	private void buildMaze() {
		// spaces work as well as f (floor)
		// floor is cleaner IMO

		// goes through each row
		for (int row = 0; row < maze.length; row++) {
			// goes through each column
			for (int col = 0; col < maze[row].length - 1; col++) {
				// with this turned on there is a chance the goal can not be
				// found,
				// NB
				// chooses a random number less then 10
				int num = (int) (Math.random() * 10);

				// half the time this happens
				// changes the next column to a floor
				if (num >= 5 && col + 1 < maze[row].length - 1) { // Change
																	// West
																	// (break
																	// wall)
					// changes X to floor
					maze[row][col + 1].setElement('f');
					// continue; // use else instead of continue
				} else
				// next we check if to the south is
				if (row + 1 < maze.length) { // Change south
					// changes x to floor
					maze[row + 1][col].setElement('f');
				}
			}
		}
	}

	private void buildMazeV1() {
		// spaces work as well as f (floor)
		// floor is cleaner IMO

		// goes through each row
		for (int row = 0; row < maze.length; row++) {
			// goes through each column
			for (int col = 0; col < maze[row].length - 1; col++) {
				// with this turned on there is a chance the goal can not be
				// found,
				// NB
				if (random.nextInt(10) >= 2) {
					// chooses a random number less then 10
					int num = (int) (Math.random() * 10);

					// half the time this happens
					// changes the next column to a floor
					if (num >= 5 && col + 1 < maze[row].length - 1) { // Change
																		// West
						// changes X to floor
						maze[row][col + 1].setElement('f');
						// continue; // use else instead of continue
					} else
					// next we check if to the south is
					if (row + 1 < maze.length) { // Change south
						// changes x to floor
						maze[row + 1][col].setElement('f');
					}
				}
			}
		}
	}

	// private void addFeature(char feature, char replace, int number) {
	private void addFeature(char feature, char replace, int number) {

		// randomely adds features
		int counter = 0;
		while (counter < number) {
			// leave 2 wall thickness around edges

			// int row = random.nextInt(GameRunner.MAZE_DIM - 4) + 2;
			// int col = random.nextInt(GameRunner.MAZE_DIM - 4) + 2;

			int row = (int) ((insideNum) * Math.random()) + 2;
			int col = (int) ((insideNum) * Math.random()) + 2;

			if (maze[row][col].getElement() == replace) {
				maze[row][col].setElement(feature);
				counter++;
				if (feature == 'g') {
					setGoalNode(row, col);
					System.out.println("GOAL: row: " + row + ", col: " + col);
				}
			}
		}
	}

	// io
	private void openFile() {
		try {
			input = new Scanner(new File("resources/map/map.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Error loading map.");
		}
	}

	private void readFile() {

		// get row first
		int row = -1;
		while (input.hasNext()) {
			row++;
			String rowStr = input.next();

			// go through each col
			for (int col = 0; col < rowStr.length(); col++) {
				char element = rowStr.charAt(col);
				maze[row][col].setElement(element);
			}
		}
	}

	private void closeFile() {
		input.close();
	}

	public void setTileItem(int x, int y, char item) {
		// row by col
		maze[y][x].setElement(item);
	}

	public String getPosElement(int x, int y) {
		char index = 0;
		try {
			index = maze[y][x].getElement();
		} catch (Exception e) {
			System.out.print("Error in Maze getPostElement(): " + e.getMessage() + "\t");
			System.out.println("x: " + x + ", y:" + y);
			// index = 'w'; // default to wall
		}

		StringBuilder temp = new StringBuilder();
		temp.append(index);

		return temp.toString();
	}

	public void printMap() {
		StringBuilder strMap = new StringBuilder();
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {
				strMap.append(maze[row][col]);
			}
			strMap.append("\n");
		}

		System.out.println(strMap.toString());
	}

	// returns maze in string form
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {
				sb.append(maze[row][col]);
				if (col < maze[row].length - 1)
					sb.append(",");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}