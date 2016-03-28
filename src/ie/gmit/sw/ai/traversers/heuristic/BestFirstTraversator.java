package ie.gmit.sw.ai.traversers.heuristic;

import ie.gmit.sw.ai.*;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

import java.awt.Component;
import java.util.*;

public class BestFirstTraversator implements Traversator {
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
	private Node goal;

	public BestFirstTraversator(Node goal) {
		this.goal = goal;
	}

	public void traverse(Node[][] maze, Node node, Component viewer) {
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.addFirst(node); // queue

		long time = System.currentTimeMillis();
		int visitCount = 0;

		while (!queue.isEmpty()) { // while not empty
			node = queue.poll(); // take from front
			node.setVisited(true);
			visitCount++;
			viewer.repaint();

			if (node.isGoalNode()) {
				time = System.currentTimeMillis() - time; // Stop the clock
				TraversatorStats.printStats(node, time, visitCount);
				// viewer.repaint();
				break;
			}

			try { // Simulate processing each expanded node
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Node[] children = node.children(maze); // get children
			for (int i = 0; i < children.length; i++) { // iterate through
														// children
				if (children[i] != null && !children[i].isVisited()) { // if not
																		// visited
					children[i].setParent(node); // set parent to current node
					queue.addFirst(children[i]); // add child to front of list
				}
			}

			// Sort the whole queue. Effectively a priority queue, first in,
			// best out
			Collections.sort(queue, (Node node1, Node node2) -> node1.getHeuristic(goal) - node2.getHeuristic(goal));
			System.out.println("queue: " + queue.toString());
			// sort the whole queue by the closest heuristic (from the current
			// and chosen child/next node), this allows us to backtrack.
			
			// current and next are just placeholders for two nodes taken from the queue.
			// it doens't matter where we are in the maze, the closest heuristic is where we go to
		}
	}
}
