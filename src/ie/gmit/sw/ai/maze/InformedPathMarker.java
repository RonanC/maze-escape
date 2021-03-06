package ie.gmit.sw.ai.maze;

import java.util.Deque;

import ie.gmit.sw.ai.GameRunner;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.heuristic.*;

/**
 * Used to generate various informed (heuristic) search algorithms.
 * Generates all moves from the player to the goal node.
 * This can then be used to mark the path on the map up to N spaces.
 * This class is used by the helper rats.
 * 
 * Algorithms: basicHillClimber, steepestAscentHillClimber, bestFirstTraversator and beamTraversator.
 * 
 * @author Ronan
 */
public class InformedPathMarker {
	private Traversator traversator;
	private Maze maze;
	int beamWidth = 2;
	boolean helper;
	
	protected Deque<int[]> allPositions;
	Node[][] mazeArray;
	
	public InformedPathMarker(Maze map, int helperPosRow, int helperPosCol, Node goalNode, int algoNum, boolean helper) {
		// init
		int pathLen = GameRunner.MAZE_DIM / 4;
		this.helper = helper;
		this.maze = map;
		int[] goalPos= {goalNode.getCol(), goalNode.getRow()};
		mazeArray = maze.getMazeArray();
		
		if (allPositions != null) {
			unmarkPath();
		}
		
		switch (algoNum) {
		case 0:
			basicHillClimber(map, helperPosRow, helperPosCol, goalPos);
			break;
			
		case 1:
			steepestAscentHillClimber(map, helperPosRow, helperPosCol, goalPos);
			break;
			
		case 2:
			bestFirstTraversator(map, helperPosRow, helperPosCol, goalPos);
			break;
			
		case 3:
			beamTraversator(map, helperPosRow, helperPosCol, goalPos, beamWidth);
			break;

		default:
			basicHillClimber(map, helperPosRow, helperPosCol, goalPos);
			break;
		}
		traversator.run(); // starts the thread
		allPositions = traversator.getAllPositions();	// get positions
		markPath(pathLen);
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
	
	/**
	 * Marks the coordinates on the map to be lit up.
	 * 
	 * @param pathLen
	 */
	public void markPath(int pathLen){
		System.out.println("Marking path");
		int counter = 0;
		for (int[] pos : allPositions) {
			if (counter > pathLen) {
				break;
			}
			counter++;
			if (helper) {
				mazeArray[pos[0]][pos[1]].setHelperPath(true);
			} else{
				mazeArray[pos[0]][pos[1]].setExplosion(true);
				mazeArray[pos[0]][pos[1]].setBurnt(true);
			}
		}
	}
	
	/**
	 * Prints the path coordinates.
	 */
	public void printPath(){
		System.out.println("Printing path");
		for (int[] pos : allPositions) {
			System.out.println("row: " + pos[0] + ", col: " + pos[1]);
		}
	}
	
	/**
	 * removes the marks on the coordinates on the map to be lit up.
	 */
	public void unmarkPath(){
		System.out.println("Unmarking path");
		for (int[] pos : allPositions) {
			if (helper) {
				mazeArray[pos[0]][pos[1]].setHelperPath(false);	
			} else{
				mazeArray[pos[0]][pos[1]].setExplosion(false);	
			}
		}
	}
}
