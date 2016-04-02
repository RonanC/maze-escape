package ie.gmit.sw.ai.traversers.uninformed;

import ie.gmit.sw.ai.characters.Player;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

/**
 * Implementation of the Recursive DFS search algorithm.
 * 
 * @author Ronan
 */
public class RecursiveDFSTraversator extends Traversator {
	/*
	 * keeps traversing through first child no queue
	 */

	public RecursiveDFSTraversator(Node[][] maze, int row, int col, Player player) {
		super(maze, row, col, player);

		setComplete(true); // we want to wait until this is complete before
							// letting the enemy use it. It is reversed.
		initCustom();
	}

	public void initCustom() {
		// dfs(currentNode);
//		allPositions = new LinkedList<int[]>();
//		keepRunning = true;
		dfs(currentNode);
		setComplete(false);
	}

	@Override
	public int[] findNextMove() { // let the enemy select one move at a time
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

	public void dfs(Node nodeTemp) { // get all positions and save to list
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
