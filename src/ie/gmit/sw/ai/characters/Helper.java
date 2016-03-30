package ie.gmit.sw.ai.characters;

import java.util.Deque;

import ie.gmit.sw.ai.maze.Maze;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.heuristic.*;

public class Helper {
	private Traversator traversator;
	private Maze maze;
	int beamWidth = 2;
	
	protected Deque<int[]> allPositions;
	Node[][] mazeArray;
	
	public Helper(Maze map, int helperPosRow, int helperPosCol, Node goalNode) {
		// init
		this.maze = map;
		int[] goalPos= {goalNode.getCol(), goalNode.getRow()};
		mazeArray = maze.getMazeArray();
		
//		basicHillClimber(map, helperPosRow, helperPosCol, goalPos);
//		steepestAscentHillClimber(map, helperPosRow, helperPosCol, goalPos);
//		bestFirstTraversator(map, helperPosRow, helperPosCol, goalPos);
		beamTraversator(map, helperPosRow, helperPosCol, goalPos, beamWidth);
		
		allPositions = traversator.getAllPositions();	// get positions
	}
	
	private void basicHillClimber(Maze map, int helperPosRow, int helperPosCol, int[] goalPos) {
		System.out.println("\nBasic Hill Climbing Traversal Helper.");
		traversator = new BasicHillClimbingTraversator(map.getMazeArrayClone(), helperPosRow, helperPosCol, goalPos);
	}

	private void steepestAscentHillClimber(Maze map, int helperPosRow, int helperPosCol, int[] goalPos) {
		System.out.println("\nSteepest Ascent Hill Climbing Traversal Helper.");
		traversator = new SteepestAscentHillClimbingTraversator(map.getMazeArrayClone(), helperPosRow, helperPosCol, goalPos);
	}
	
	private void bestFirstTraversator(Maze map, int helperPosRow, int helperPosCol, int[] goalPos) {
		System.out.println("\nBest First Traversator Helper.");
		traversator = new BestFirstTraversator(map.getMazeArrayClone(), helperPosRow, helperPosCol, goalPos);
	}
	
	private void beamTraversator(Maze map, int helperPosRow, int helperPosCol, int[] goalPos, int beamWidth) {
		System.out.println("\nBest First Traversator Helper.");
		traversator = new BeamTraversator(map.getMazeArrayClone(), helperPosRow, helperPosCol, goalPos, beamWidth);
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
