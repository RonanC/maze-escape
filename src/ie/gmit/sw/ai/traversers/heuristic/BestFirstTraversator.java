package ie.gmit.sw.ai.traversers.heuristic;

import ie.gmit.sw.ai.*;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

import java.awt.Component;
import java.util.*;

public class BestFirstTraversator extends Traversator {
	/*
	 * adds all children to queue
	 * then sorts queue by closest heuristic
	 * It chooses the best node first every time.
	 * 
	 * Uses a queue to reverse back up the path
	 * 
	 * When all children are checked, we check all the siblings, and so forth.
	 * 
	 * Space complexity of a DFS. We keep popping off the queue, sorting and checking. Very costly.
	 * We revisit many nodes.
	 */

	public BestFirstTraversator(Node[][] maze, int row, int col, int[] goalPos) {
		super(maze, row, col, null);
		setComplete(false);
		setGoalNode(goalPos[1], goalPos[0]);
		traverse();	// fill up allPositions queue.
	}
	
	@Override
	public int[] findNextMove() { // let the helper select one move at a time
									// from the list

		if (!allPositions.isEmpty()) {
//			System.out.print("pop\t");
			newPos = allPositions.pop();
//			System.out.println(newPos[0] + ", " + newPos[1]);
		} else {
//			System.out.println("not ready");
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
		int visitCount = 0;

		while (!queue.isEmpty()) { // while not empty
			currentNode = queue.poll(); // take from front
			allPositions.add(new int[] { currentNode.getRow(), currentNode.getCol() });
			currentNode.setVisited(true);
			visitCount++;

			if (currentNode.isGoalNode()) {
				time = System.currentTimeMillis() - time; // Stop the clock
				TraversatorStats.printStats(currentNode, time, visitCount);
				break;
			}

//			try { // Simulate processing each expanded node
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

			Node[] children = currentNode.children(mazeArray); // get children
			for (int i = 0; i < children.length; i++) { // iterate through
														// children
				if (children[i] != null && !children[i].isVisited()) { // if not
																		// visited
					children[i].setParent(currentNode); // set parent to current node
					queue.addFirst(children[i]); // add child to front of list
				}
			}

			// Sort the whole queue. Effectively a priority queue, first in,
			// best out
			Collections.sort(queue, (Node node1, Node node2) -> node1.getHeuristic(goal) - node2.getHeuristic(goal));
//			System.out.println("queue: " + queue.toString());
			// sort the whole queue by the closest heuristic (from the current
			// and chosen child/next node), this allows us to backtrack.
			
			// current and next are just placeholders for two nodes taken from the queue.
			// it doens't matter where we are in the maze, the closest heuristic is where we go to
		}
	}
}
