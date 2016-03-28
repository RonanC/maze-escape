package ie.gmit.sw.ai.traversers.uninformed;

import ie.gmit.sw.ai.*;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

import java.awt.*;
import java.util.*;

public class BruteForceTraversator implements Traversator {
	/*
	 * Option: BFS or DFS gets all children, either adds them to the front (BFS)
	 * or end (DFS) of the dequeue. then polls the queue no intelligence here,
	 * just brute force.
	 * 
	 * BFS takes up a lot of memory as all pointers to children are stored on
	 * the queue, (parents of each node are also stored) Parents are also
	 * stored.
	 * 
	 * DFS has very low memory because it's not necessary to store all child
	 * then go into the child
	 * when pulling back up a rat hole we pop all the children off the queue.
	 * 
	 * in BFS we store all the children and then go into each child and add all
	 * children and so forth, this quickly grows the list like crazy
	 * 
	 * DFS - Stack
	 * BFS Queue
	 */
	private boolean dfs = false;

	public BruteForceTraversator(boolean depthFirst) {
		this.dfs = depthFirst;
	}

	public void traverse(Node[][] maze, Node node, Component viewer) {
		// Start the clock
		long time = System.currentTimeMillis();
		int visitCount = 0;

		// create a double ended queue of Nodes
		Deque<Node> queue = new LinkedList<Node>();
		queue.offer(node); // adds element to the end of the queue (starting
							// node added)

		while (!queue.isEmpty()) { // while the queue is not empty
			System.out.println("queue: " + queue.toString());
			node = queue.poll(); // take from the head

			// same as other algorithms
			node.setVisited(true);
			visitCount++;
			viewer.repaint();

			// found node, end
			if (node.isGoalNode()) {
				time = System.currentTimeMillis() - time; // Stop the clock
				TraversatorStats.printStats(node, time, visitCount);
				viewer.repaint();
				break;
			}

			try { // Simulate processing each expanded node
				Thread.sleep(4);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// actual algorithm
			Node[] children = node.children(maze); // get child nodes available
			for (int i = 0; i < children.length; i++) { // go through each node
														// (brute force)
				if (children[i] != null && !children[i].isVisited()) {
					// if child is not empty and it has not being visited (we
					// never visit a node twice)
					children[i].setParent(node); // set this child nodes parent
													// as the our current node
													// (which is the parent).
					if (dfs) { // .children if depth first then we add this to
								// the front
						// which means we will add all the children to the front
						// this means we will expand these children next
						// iteration.

						// exands until the end (rat holes).
						queue.addFirst(children[i]);
					} else { // add child to the end of the queue
						// add all children to the end of the queue
						// this means the next iteration we won't exand these
						// children, this will be more like a frontier push
						// approach.
						queue.addLast(children[i]);
					}
				}
			}
		}
	}
}
