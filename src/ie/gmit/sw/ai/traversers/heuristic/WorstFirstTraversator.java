package ie.gmit.sw.ai.traversers.heuristic;

import ie.gmit.sw.ai.*;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

import java.awt.Component;
import java.util.*;

public class WorstFirstTraversator extends Traversator {
	/*
	 * adds all children to queue then sorts queue by closest heuristic It
	 * chooses the best node first every time.
	 * 
	 * Uses a queue to reverse back up the path
	 * 
	 * When all children are checked, we check all the siblings, and so forth.
	 * 
	 * Space complexity of a DFS. We keep popping off the queue, sorting and
	 * checking. Very costly. We revisit many nodes.
	 */

	private int[] worstPos;
	private Node worstNode;
	private Node oldGoal;
	private Node startPos;
	private Node newGoal;

	public WorstFirstTraversator(Node[][] maze, int row, int col, int[] goalPos, Node oldGoal) {
		super(maze, row, col, null);
		this.oldGoal = oldGoal;
		worstPos = new int[] { row, col };
		setComplete(false);
		
		setGoalNode(goalPos[0], goalPos[1]);
		this.newGoal = getGoalNode();
		worstNode = currentNode;
		traverse(); // fill up allPositions queue.
		// we want the worst one, that means more nodes expanded and we have a better chance of finding a worse heuristic
//		System.out.println("visit count: " + visitCount);

		worstPos = new int[]{worstNode.getRow(), worstNode.getCol()};
		
		// we will now try the other diagonal
//		resetGraph();
//		setCurrentNode(oldGoal);
//		setGoal(startPos);
//		traverse();
	}
	
	

	public Node getWorstNode() {
		return worstNode;
	}



	public int[] getWorstPos() {
		return worstPos;
	}



	@Override
	public int[] findNextMove() { // let the helper select one move at a time
									// from the list

		if (!allPositions.isEmpty()) {
			// System.out.print("pop\t");
			newPos = allPositions.pop();
			 System.out.println(newPos[0] + ", " + newPos[1]);
		} else {
			// System.out.println("not ready");
			resetNewPos();
		}

		try { // Simulate processing each expanded node
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return newPos;
	}

	public void traverse() {
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.addFirst(currentNode); // queue

		long time = System.currentTimeMillis();
		
		while (!queue.isEmpty()) { // while not empty
			currentNode = queue.poll(); // take from front
			if (currentNode.getHeuristic(oldGoal) > worstNode.getHeuristic(oldGoal)) {
				System.out.println("curr: " + currentNode.getHeuristic(oldGoal) + "\tworst: " + worstNode.getHeuristic(oldGoal));
				worstNode = currentNode;
				System.out.println("Starting pos, row: " + worstNode.getRow() + ", col: " + worstNode.getCol());
				System.out.println();
				
			}
			
			allPositions.add(new int[] { currentNode.getRow(), currentNode.getCol() });
			currentNode.setVisited(true);
			visitCount++;



			if (currentNode.isGoalNode()) {
				time = System.currentTimeMillis() - time; // Stop the clock
				TraversatorStats.printStats(currentNode, time, visitCount);
				break;
			}

			// try { // Simulate processing each expanded node
			// Thread.sleep(10);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }

			Node[] children = currentNode.children(mazeArray); // get children
			for (int i = 0; i < children.length; i++) { // iterate through
														// children
				if (children[i] != null && !children[i].isVisited()) { // if not
																		// visited
					children[i].setParent(currentNode); // set parent to current
														// node
					queue.addFirst(children[i]); // add child to front of list
//					queue.addLast(children[i]); // add child to back of list	(this is actually twice as fast, but that depends where you are in the maze)
				}
			}

			// Sort the whole queue. Effectively a priority queue, first in,
			// best out

			// Swapped
			Collections.sort(queue, (Node node1, Node node2) -> node1.getHeuristic(goal) - node2.getHeuristic(goal)); 	// normal: 153
//			Collections.sort(queue, (Node node1, Node node2) -> node2.getHeuristic(goal) - node1.getHeuristic(goal)); 	// swapped:	17596	// Worst First

			// +
//			Collections.sort(queue, (Node node1, Node node2) -> node1.getHeuristic(goal) + node2.getHeuristic(goal)); 	// +: 3916
//			Collections.sort(queue, (Node node1, Node node2) -> node2.getHeuristic(goal) + node1.getHeuristic(goal)); 	// + swapped:	1267
			
		
//			 swapped these two values
			
			
			
			// System.out.println("queue: " + queue.toString());
			// sort the whole queue by the closest heuristic (from the current
			// and chosen child/next node), this allows us to backtrack.

			// current and next are just placeholders for two nodes taken from
			// the queue.
			// it doens't matter where we are in the maze, the closest heuristic
			// is where we go to
		}
	}
}
