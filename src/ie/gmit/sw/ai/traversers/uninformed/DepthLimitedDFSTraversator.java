package ie.gmit.sw.ai.traversers.uninformed;

import ie.gmit.sw.ai.characters.Player;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

/**
 * Implementation of the Depth Limited DFS search algorithm.
 * 
 * @author Ronan
 */
public class DepthLimitedDFSTraversator extends Traversator {
	/*
	 * same as DFS but limited.
	 */
	private int limit;

	public DepthLimitedDFSTraversator(Node[][] maze, int row, int col, Player player, int limit) { // new
		super(maze, row, col, player);
		this.limit = limit;
		setComplete(true);
		initCustom();
	}

	public void initCustom() {
		// dfs(currentNode);
//		keepRunning = true;
		dfs(currentNode, 1);
		System.out.println("size: " + allPositions.size());
		if (isComplete()) {
//			System.out.println("Failed to find goal node within a depth of " + limit);
//			setComplete(false);
//			keepRunning = false;
			
		} else {
//			System.out.println("Found Goal node");
		}
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

	private void dfs(Node node, int depth) {
		allPositions.add(new int[] { node.getRow(), node.getCol() });
		resetNewPos();

		if (!keepRunning || depth > limit){
			keepRunning = false; // stop it (all returns will trigger)
//			setComplete(false);
			return; // if depth reached then return (goal could be passed that
			// depth though)
		}


		node.setVisited(true);
		visitCount++;

		if (node.isGoalNode()) {
			time = System.currentTimeMillis() - time; // Stop the clock
			TraversatorStats.printStats(node, time, visitCount);
			keepRunning = false; // stop it (all returns will trigger)
			setComplete(false);
			return;
		}

		Node[] children = node.children(mazeArray);
		for (int i = 0; i < children.length; i++) {
			if (!children[i].isVisited()) { // it will not be null as only child
											// nodes are passed back
				children[i].setParent(node);
				dfs(children[i], depth + 1); // pass depth in with recursive
												// call
			}
		}
	}
}