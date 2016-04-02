package ie.gmit.sw.ai.traversers.uninformed;

import ie.gmit.sw.ai.characters.Player;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

/**
 * Implementation of the Iterative Deepening DFS search algorithm.
 * 
 * @author Ronan
 */
public class IDDFSTraversator extends Traversator {
	// iterative deepening DFS
	// frontier moves after each depth has being checked.
	// this saves on memory.

	public IDDFSTraversator(Node[][] maze, int row, int col, Player player) {
		super(maze, row, col, player);
		setComplete(true);
		
	}

//	@Override
//	public void run() {
//		if (!shouldStop) {
//			initCustom();
//			shouldStop= true;
//		}
//	}

	@Override
	protected void initCustom() {
		int limit = 1;

		while (keepRunning) {
			dfs(currentNode, 0, limit); // recursive call with depth of 0
										// initially

			if (keepRunning) { // return increase limit and unvisit
				limit++; // increase frontier
				unvisit(); // remove all parents and visited nodes (aka RESET)
			}
		}

		setComplete(false);
	}

	@Override
	public int[] findNextMove() { // let the enemy select one move at a time
									// from the list

		if (!allPositions.isEmpty()) {
			// System.out.print("pop\t");
			newPos = allPositions.pop();
			// System.out.println(newPos[0] + ", " + newPos[1]);
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

	private void dfs(Node node, int depth, int limit) {
		allPositions.add(new int[] { node.getRow(), node.getCol() });
		if (!keepRunning || depth > limit)
			return; // return if depth met (instead of ending it like the
					// Limited DFS, we will call the method with an increased
					// limit)
					// DFS has rat holes, limited one means we might miss the
					// goal, iterative deepening DFS allows us to have the space
					// complexity of BFS and also a complete algorithm
					// time complexity is quite low though

		node.setVisited(true);
		visitCount++;

		if (node.isGoalNode()) {
			time = System.currentTimeMillis() - time; // Stop the clock
			TraversatorStats.printStats(node, time, visitCount);
			keepRunning = false;
			return;
		}

		Node[] children = node.children(mazeArray);
		for (int i = 0; i < children.length; i++) {
			if (children[i] != null && !children[i].isVisited()) {
				children[i].setParent(node);
				dfs(children[i], depth + 1, limit);
			}
		}
	}

	private void unvisit() {
		for (int i = 0; i < mazeArray.length; i++) {
			for (int j = 0; j < mazeArray[i].length; j++) {
				mazeArray[i][j].setVisited(false);
				mazeArray[i][j].setParent(null);
			}
		}
	}

}