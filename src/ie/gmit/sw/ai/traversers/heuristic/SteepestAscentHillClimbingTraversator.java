package ie.gmit.sw.ai.traversers.heuristic;

import java.util.*;

import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

/**
 * Implementation of the Steepest Ascent Hill Climbing search algorithm.
 * 
 * @author Ronan
 */
public class SteepestAscentHillClimbingTraversator extends Traversator {
	/*
	 * Similar to Hill Climbing. Has queue. Checks every single child before
	 * moving back up the queue. Similar to DFS in that it can get stuck in a
	 * rat hole. Sorts all children of current node (<= 4)
	 * 
	 * It sorts the children, and when it picks the child path it commits to
	 * check every single child within it You can notice that there are no half
	 * looked at paths.
	 * 
	 * All or nothing. Like the way I play games. check everything!
	 * 
	 * It chooses the best choice out of every single intersection and them
	 * ascends all the way.
	 */

	public SteepestAscentHillClimbingTraversator(Node[][] maze, int row, int col, int[] goalPos) {
		super(maze, row, col, null);
		setComplete(false);
		setGoalNode(goalPos[1], goalPos[0]);
	}
	
	@Override
	protected void initCustom() {
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
		queue.addFirst(currentNode);

		long time = System.currentTimeMillis();
		int visitCount = 0;

		while (!queue.isEmpty()) {
			allPositions.add(new int[] { currentNode.getRow(), currentNode.getCol() });
			currentNode = queue.poll();
			visitCount++;
			currentNode.setVisited(true);

			if (currentNode.isGoalNode()) {
				time = System.currentTimeMillis() - time; // Stop the clock
				TraversatorStats.printStats(currentNode, time, visitCount);
				break;
			}

			// Sort the children of the current node in order of increasing h(n)
			Node[] children = currentNode.children(mazeArray); // get 4 children
			// It sorts all the children of the current node.....?
//			for (int i = 0; i < children.length; i++) {
//				System.out.print(children[i] + ",");
//			}
//			System.out.print("\t");

			Collections.sort(Arrays.asList(children),
					(Node current, Node next) -> next.getHeuristic(goal) - current.getHeuristic(goal)); // lambda
//			for (int i = 0; i < children.length; i++) {
//				System.out.print(children[i].toString() + ",");
//			}
//			System.out.println();
			// Collections.sort takes in a list and a comparator
			// we use the lambda as the comparator.
			// the lambda is an anonymous throw away function

			// We sort all current children and choose the best

			for (int i = 0; i < children.length; i++) {
				if (children[i] != null && !children[i].isVisited()) {
					children[i].setParent(currentNode); // sets parent node
					queue.addFirst(children[i]); // LIFO // adds the child to
													// the queue (we will go
													// into that child first)
					// The children are sorted by heuristic value so we always
					// go into the best child
				}
			}
		}
	}
}