package ie.gmit.sw.ai.traversers.uninformed;

import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

public class RandomWalk implements Traversator {
	/*
	 * Sets the current node to visited, then chooses a random child node and
	 * moves to it
	 * 
	 * low memory, high time
	 */
	private long time;
	private int visitCount;
	private int steps;
	private boolean complete;
	private Node node;
	private Node[][] maze;

	public int findNextMove() { // traverse one step
		if (visitCount <= steps && node != null) {
			node.setVisited(true); // this is what changes the color of the node
			visitCount++;

			if (node.isGoalNode()) {
				time = System.currentTimeMillis() - time; // Stop the clock
				TraversatorStats.printStats(node, time, visitCount);
				complete = true;
				return 4;	// found goal
			} else {
//				try { // Simulate processing each expanded node
//					Thread.sleep(1);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}

				// Pick a random adjacent node
				Node[] children = node.children(maze); // get all children
				// choose a random child
				int choice = (int) (children.length * Math.random());
//				System.out.println("children choice: " + choice);
				try {
					node = children[choice];
				} catch (Exception e) {
					System.out.println("error:" + e.getMessage());
				}
				
				
				return choice;	// 0, 1, 2, 3
			}
		}
		
		return 5;	// out of steps

	}

	public void init(Node[][] maze, Node node) {
		this.maze = maze;
		this.node = node;
		time = System.currentTimeMillis();
		visitCount = 0;

		steps = (int) Math.pow(maze.length, 2) * 2;
		System.out.println("Number of steps allowed: " + steps);

		complete = false;
	}
}
