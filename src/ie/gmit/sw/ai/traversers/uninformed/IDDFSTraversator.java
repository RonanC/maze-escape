package ie.gmit.sw.ai.traversers.uninformed;

import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

import java.awt.Component;

public class IDDFSTraversator implements Traversator {
	// iterative deepening DFS
	// frontier moves after each depth has being checked.
	// this saves on memory.

	private Node[][] maze;
	private Component viewer;
	private boolean keepRunning = true;
	private long time = System.currentTimeMillis();
	private int visitCount = 0;

	public void traverse(Node[][] maze, Node start, Component viewer) {
		this.maze = maze;
		this.viewer = viewer;
		int limit = 1;

		while (keepRunning) {
			dfs(start, 0, limit); // resursive call with depth of 0 initially

			if (keepRunning) { // return increase limit and unvisit
				limit++; // increase frontier
				unvisit(); // remove all parents and visited nodes (aka RESET)
			}
		}

	}

	private void dfs(Node node, int depth, int limit) {
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
		for (int i = 0; i < children.length; i++) {
			if (children[i] != null && !children[i].isVisited()) {
				children[i].setParent(node);
				dfs(children[i], depth + 1, limit);
			}
		}
	}

	private void unvisit() {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				maze[i][j].setVisited(false);
				maze[i][j].setParent(null);
			}
		}
	}
}