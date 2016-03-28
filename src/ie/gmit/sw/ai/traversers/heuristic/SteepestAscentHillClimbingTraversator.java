package ie.gmit.sw.ai.traversers.heuristic;

import java.awt.*;
import java.util.*;

import ie.gmit.sw.ai.*;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

public class SteepestAscentHillClimbingTraversator implements Traversator {
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
	private Node goal;

	public SteepestAscentHillClimbingTraversator(Node goal) {
		this.goal = goal;
	}

	public void traverse(Node[][] maze, Node node, Component viewer) {
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.addFirst(node);

		long time = System.currentTimeMillis();
		int visitCount = 0;

		while (!queue.isEmpty()) {
			node = queue.poll();
			visitCount++;
			node.setVisited(true);
			viewer.repaint();

			if (node.isGoalNode()) {
				time = System.currentTimeMillis() - time; // Stop the clock
				TraversatorStats.printStats(node, time, visitCount);
				viewer.repaint();
				break;
			}

			try { // Simulate processing each expanded node
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Sort the children of the current node in order of increasing h(n)
			Node[] children = node.children(maze); // get 4 children
			// It sorts all the children of the current node.....?
			for (int i = 0; i < children.length; i++) {
				System.out.print(children[i] + ",");
			}
			System.out.print("\t");

			Collections.sort(Arrays.asList(children),
					(Node current, Node next) -> next.getHeuristic(goal) - current.getHeuristic(goal)); // lambda
			for (int i = 0; i < children.length; i++) {
				System.out.print(children[i] + ",");
			}
			System.out.println();
			// Collections.sort takes in a list and a comparator
			// we use the lambda as the comparator.
			// the lambda is an anonymous throw away function

			// We sort all current children and choose the best

			for (int i = 0; i < children.length; i++) {
				if (children[i] != null && !children[i].isVisited()) {
					children[i].setParent(node); // sets parent node
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