package ie.gmit.sw.ai.traversers.uninformed;

import ie.gmit.sw.ai.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

import java.awt.Component;

public class RecursiveDFSTraversator implements Traversator {
	/*
	 * keeps traversing through first child
	 * no queue
	 */
	private Node[][] maze;
	private Component viewer;
	private boolean keepRunning = true;
	private long time = System.currentTimeMillis();
	private int visitCount = 0;

	public void traverse(Node[][] maze, Node node, Component viewer) {
		this.maze = maze;
		this.viewer = viewer;
		dfs(node);
	}

	private void dfs(Node node) {
		if (!keepRunning)
			return;

		node.setVisited(true);
		visitCount++;
		viewer.repaint();

		if (node.isGoalNode()) {
			time = System.currentTimeMillis() - time; // Stop the clock
			TraversatorStats.printStats(node, time, visitCount);
			viewer.repaint();
			keepRunning = false;
			return;
		}

		try { // Simulate processing each expanded node
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Node[] children = node.children(maze);
		for (int i = 0; i < children.length; i++) {	// go through each child
			if (children[i] != null && !children[i].isVisited()) {	// if not null and not visited
				children[i].setParent(node);	// set our current node as parent to child node
				dfs(children[i]);	// call this method on the child.
				// this is like the brute first dequeue method without the queue.
				// with the brute force one we added each child then expanded it, this is the same, no queue so we save memory
			}
		}
	}
}
