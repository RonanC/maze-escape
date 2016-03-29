package ie.gmit.sw.ai.traversers.uninformed;

import ie.gmit.sw.ai.characters.Player;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

import java.awt.Component;
import java.util.*;

public class RecursiveDFSTraversator extends Traversator {
	/*
	 * keeps traversing through first child no queue
	 */
	private boolean keepRunning = true;
	private Deque<int[]> allPositions;

	public RecursiveDFSTraversator(Node[][] maze, int row, int col, Player player) {
		super(maze, row, col, player);
		initCustom();
		setComplete(true); // we want to wait until this is complete before
							// letting the enemy use it. It is reversed.
	}

	public void initCustom() {
		// dfs(currentNode);
		allPositions = new LinkedList<int[]>();
		dfs(currentNode);
	}

	@Override
	public int[] findNextMove() { // then let the enemy select one at a time

		if (!keepRunning) {
			newPos = allPositions.pop();
		} else {
			System.out.println("not ready");
			resetNewPos();
		}
		return newPos;
	}

	public void dfs(Node nodeTemp) { // get all positions
		allPositions.add(new int[] { nodeTemp.getRow(), nodeTemp.getCol() });

		resetNewPos();
		if (!keepRunning)
			return;

		nodeTemp.setVisited(true);
		visitCount++;

		if (nodeTemp.isGoalNode()) {
			time = System.currentTimeMillis() - time; // Stop the clock
			TraversatorStats.printStats(nodeTemp, time, visitCount);
			keepRunning = false;
			setComplete(false);
			return;
		}

		// try { // Simulate processing each expanded node
		// Thread.sleep(1);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }

		Node[] children = nodeTemp.children(mazeArray);
		for (int i = 0; i < children.length; i++) { // go through each child
			if (children[i] != null && !children[i].isVisited()) { // if not
																	// null and
																	// not
																	// visited
				children[i].setParent(nodeTemp); // set our current node as
													// parent to child node
				dfs(children[i]); // call this method on the child.
				// this is like the brute first dequeue method without the
				// queue.
				// with the brute force one we added each child then expanded
				// it, this is the same, no queue so we save memory
			}
		}
	}
}
