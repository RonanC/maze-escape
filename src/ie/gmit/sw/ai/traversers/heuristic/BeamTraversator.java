package ie.gmit.sw.ai.traversers.heuristic;

import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

import java.awt.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class BeamTraversator implements Traversator {
	/*
	 * We sort all the children and only search the maximum chosen number, aka
	 * the BEAM. When popping off the queue we will get to the second chosen
	 * children (which enables second choices, backtracking). When we back track
	 * we always check the second child.
	 * 
	 * Having beam at number 4 turns this into a regular sorted children
	 * algorithm. (steepest ascent hill climber).
	 * 
	 * 
	 * Best first is a sorted queue. Sorted children is steepest ascent hill
	 * climber.
	 * 
	 * Needs pruning in order to be efficient, best to prune half each time.
	 */
	private Node goal;
	private int beamWidth = 1;

	public BeamTraversator(Node goal, int beamWidth) {
		this.goal = goal;
		this.beamWidth = beamWidth;
	}

	public void traverse(Node[][] maze, Node node, Component viewer) {
		LinkedList<Node> queue = new LinkedList<Node>(); // queue
		queue.addFirst(node);

		long time = System.currentTimeMillis();
		int visitCount = 0;

		while (!queue.isEmpty()) {
			node = queue.poll(); // take first node
			node.setVisited(true);
			visitCount++;
			viewer.repaint();

			if (node.isGoalNode()) {
				time = System.currentTimeMillis() - time; // Stop the clock
				TraversatorStats.printStats(node, time, visitCount);
				viewer.repaint();
				break;
			}

			try { // Simulate processing each expanded node
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Node[] children = node.children(maze); // get children of node
			Collections.sort(Arrays.asList(children),
					(Node current, Node next) -> current.getHeuristic(goal) - next.getHeuristic(goal));
					// sort children

			// take the next node away from the first one

			int bound = 0;
			if (children.length < beamWidth) { // if we have less children then
												// the beam width then the bound
												// is = to the children length
												// (which will be less)
				bound = children.length;
			} else { // if we have more children then the beamwidth then the
						// bound is equal to the beam with
				bound = beamWidth;
			}
			System.out.print("bound: " + bound + "\t");
			// therefore the beam width will always be 4 or below

			for (int i = 0; i < bound; i++) {
				System.out.print("i" + children[i] + "\t");
				// We only check either all children or less(bound)
				// (children are sorted so we only check the best ones)
				// however if one of the worse ones was 'pruned' then we may
				// have lost our chance, this makes it non-complete.
				if (children[i] != null && !children[i].isVisited()) {
					// check first child and second child only
					// if not visited then
					children[i].setParent(node); // set the parent to the
													// current node
					queue.addFirst(children[i]); // add to queue
				}
			}
			System.out.println();
			// next loop, make bound beam width or less
		}
	}
}
