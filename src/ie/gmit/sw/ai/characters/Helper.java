package ie.gmit.sw.ai.characters;

import java.util.Deque;

import ie.gmit.sw.ai.maze.Maze;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.heuristic.BasicHillClimbingTraversator;

public class Helper {
	private Traversator traversator;
	private Maze maze;
	
	protected Deque<int[]> allPositions;
	Node[][] mazeArray;
	
	public Helper(Maze map, int helperPosRow, int helperPosCol, Node goalNode) {
		this.maze = map;
		int[] goalPos= {goalNode.getRow(), goalNode.getCol()};
		traversator = new BasicHillClimbingTraversator(map.getMazeArrayClone(), helperPosRow, helperPosCol, goalPos);
		allPositions = traversator.getAllPositions();	// get positions
		mazeArray = maze.getMazeArray();
		System.out.println("Basic Hill Climbing Traversal Helper.");
	}
	
	public void markPath(){
		System.out.println("Marking path");
//		Node[][] mazeArray = maze.getMazeArray();
		for (int[] pos : allPositions) {
			mazeArray[pos[0]][pos[1]].setHelperPath(true);	// y for yellow brick road
		}
	}
	
	public void printPath(){
		System.out.println("Printing path");
//		Node[][] mazeArray = maze.getMazeArray();
		for (int[] pos : allPositions) {
			System.out.println("row: " + pos[0] + ", col: " + pos[1]);
		}
	}
	
	public void unmarkPath(){
		System.out.println("Unmarking path");
//		Node[][] mazeArray = maze.getMazeArray();
		for (int[] pos : allPositions) {
			mazeArray[pos[0]][pos[1]].setHelperPath(false);	// y for yellow brick road
		}
	}
}
