package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Maze {

	private Scanner input;
	private char[][] maze;
	private Random random;
	private int insideNum;

	public Maze() {
		random = new Random();
		insideNum = GameRunner.MAZE_DIM - 4;
		maze = new char[GameRunner.MAZE_DIM][GameRunner.MAZE_DIM];

		// readInMap();
		genRandomMaze();
		placeOuterWalls();
	}

	private void initMaze(char element) {
		System.out.println(maze.length);
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {
				maze[row][col] = element;
			}
		}

		System.out.println(toString());
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
			if (row == 0 || row == 1 || row == maze.length - 1 || row == maze.length-2) {
				for (int col = 0; col < maze.length - 1; col++) {
					maze[row][col] = 'w';
				}
			}
			for (int col = 0; col < 2; col++) {
				maze[row][col] = 'w';
			}
			
			// left wall
			for (int col = 0; col < 2; col++) {
				maze[row][col] = 'w';
			}
			
			// right wall
			for (int col = maze.length - 1; col > maze.length - 3; col--) {
				maze[row][col] = 'w';
			}
		}
	}

	private void genRandomMaze() {
		initMaze('x');

		// changes some to space
		buildMaze();

		int itemNum = random.nextInt(GameRunner.MAZE_DIM / 5); // 10
		int helperNum = random.nextInt(GameRunner.MAZE_DIM / 10); // 5
		int goalNum = random.nextInt(GameRunner.MAZE_DIM / 50); // 2
		
		if (goalNum < 1) {
			goalNum = 1;
		}

		// items
		int swordNumber = itemNum; // 10
		int bombNumber = itemNum; // 10

		// quest items
		int goalNumber = goalNum; // 4
		int helperNumber = helperNum; // 12

		// items
		addFeature('s', 'x', itemNum);
		addFeature('b', 'x', itemNum);

		// quest items
		addFeature('h', 'x', itemNum);
		addFeature('g', 'x', 1);

		// rest of x's should be walls
		addWalls();

		System.out.println(toString());
	}

	private void addWalls() {
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length - 1; col++) {
				if(maze[row][col] == 'x'){
					maze[row][col] = 'w';
				}
			}
		}
	}

	private void buildMaze() {
		// spaces work as well as f (floor)
		// floor is cleaner IMO
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length - 1; col++) {
				// chooses a random number less then 10
				int num = (int) (Math.random() * 10);
				if (num >= 5 && col + 1 < maze[row].length - 1) {
					// changes X to blank
					maze[row][col + 1] = 'f';
					continue;
				}
				if (row + 1 < maze.length) { // Check south
					// changes x to space
					maze[row + 1][col] = 'f';
				}
			}
		}
	}

//	private void addFeature(char feature, char replace, int number) {
	private void addFeature(char feature, char replace, int number) {
		// randomely adds features
		int counter = 0;
		while (counter < number) {
			// leave 2 wall thickness around edges

			// int row = random.nextInt(GameRunner.MAZE_DIM - 4) + 2;
			// int col = random.nextInt(GameRunner.MAZE_DIM - 4) + 2;

			int row = (int) ((insideNum) * Math.random()) + 2;
			int col = (int) ((insideNum) * Math.random()) + 2;

			// System.out.print("row: " + row + ", col: " + col);
			// System.out.println("\tmaze[row][col]: " + maze[row][col]);

			if (maze[row][col] == replace) {
				maze[row][col] = feature;
				counter++;
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
				maze[row][col] = element;
			}
		}
	}

	private void closeFile() {
		input.close();
	}

	public void reset() {
		readInMap();
	}

	public void setTileItem(int x, int y, char item) {
		// row by col
		maze[y][x] = item;
	}

	public String getPosElement(int x, int y) {
		char index = 0;
		try {
			index = maze[y][x];
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("x: " + x + ", y:" + y);
			// printMap();
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
