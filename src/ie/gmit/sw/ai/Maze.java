package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Maze {

	private Scanner input;
	private String maze[];

	public Maze() {
		this.maze = new String[GameRunner.MAZE_DIM];

		// read in map
		openFile();
		readFile();
		closeFile();
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

		for (int y = 0; y < GameRunner.MAZE_DIM; y++) { // goes through each (14
														// times)
			StringBuilder temp = new StringBuilder();
			try {
				temp.append(input.next());
			} catch (Exception e) {
				// no input
				for (int j = 0; j < GameRunner.MAZE_DIM; j++) {
					temp.append('w');
				}
				maze[y] = temp.toString();
			}

			// add walls where there are missing characters
			if (temp.length() == GameRunner.MAZE_DIM) {
				maze[y] = temp.toString();
			} else {
				int extras = GameRunner.MAZE_DIM - (temp.length());
//				System.out.println("extras: " + extras);
				for (int j = 0; j < extras; j++) {
					temp.append('w');
				}
				maze[y] = temp.toString();
//				printMap();
			}
		}
	}

	private void closeFile() {
		input.close();
	}

	public void reset() {
		openFile();
		readFile();
		closeFile();
	}

	// set tile item
	public void setTileItem(int x, int y, char item) {
		StringBuilder tile = new StringBuilder(maze[y]);
		tile.setCharAt(x, item);
		maze[y] = tile.toString();
	}

	// getters
	public String getPosElement(int x, int y) {
		String index = null;
		try {
			index = maze[y].substring(x, x + 1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("x: " + x + ", y:" + y);
			printMap();
		}

		return index;
	}

	public void printMap() {
		StringBuilder strMap = new StringBuilder();
		for (int i = 0; i < maze.length; i++) {
			strMap.append(maze[i] + "\n");
		}
		System.out.println(strMap.toString());
	}
}
