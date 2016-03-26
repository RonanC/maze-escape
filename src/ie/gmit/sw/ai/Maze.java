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

	public Maze() {

		maze = new char[GameRunner.MAZE_DIM][GameRunner.MAZE_DIM];
		initMaze();
		
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

	private void initMaze() {
		System.out.println(maze.length);
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {
				maze[row][col] = 'w';
			}
		}
		
		printMap();
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
		openFile();
		readFile();
		closeFile();
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
}
