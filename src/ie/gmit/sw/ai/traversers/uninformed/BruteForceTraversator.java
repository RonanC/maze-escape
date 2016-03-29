package ie.gmit.sw.ai.traversers.uninformed;

import ie.gmit.sw.ai.*;
import ie.gmit.sw.ai.characters.Player;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

import java.awt.*;
import java.util.*;

public class BruteForceTraversator extends Traversator {
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
	Deque<Node> queue;
	
	public BruteForceTraversator(Node[][] maze, int row, int col, boolean depthFirst, Player player) {
		super(maze, row, col, player);
		complete = false;
		initCustom(depthFirst);
	}
	
	private void initCustom(boolean depthFirst) {
		this.dfs = depthFirst;
		
		// create a double ended queue of Nodes
		queue = new LinkedList<Node>();
		queue.offer(currentNode); // adds element to the end of the queue (starting
							// node added)
	}
	
	public int[] findNextMove() { // traverse one step through the algorithm at a time
		resetNewPos();
		if (!queue.isEmpty()) { // while the queue is not empty
//			System.out.println("queue: " + queue.toString());
			
			currentNode = queue.poll(); // take from the head

			// same as other algorithms
			currentNode.setVisited(true);
			visitCount++;

			// found node, end
			if (currentNode.isGoalNode()) {
				time = System.currentTimeMillis() - time; // Stop the clock
				TraversatorStats.printStats(currentNode, time, visitCount);
//				setGoalNodeRand();// reset goal node
				newPos[0] = 4;
				setComplete(true);
				queue.clear();
				resetGraph();
				return newPos;
			}

			try { // let painter paint screen
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// actual algorithm
			Node[] children = currentNode.children(mazeArray); // get child nodes available
			for (int i = 0; i < children.length; i++) { // go through each node
														// (brute force)
				if (children[i] != null && !children[i].isVisited()) {
					// if child is not empty and it has not being visited (we
					// never visit a node twice)
					children[i].setParent(currentNode); // set this child nodes parent
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
			newPos[0] = currentNode.getRow(); // return node co-ordinates	// Y
			newPos[1] = currentNode.getCol();								// X
			return newPos;
		} else{
			// queue empty, did not find node
			setComplete(true);
			newPos[0] = 5;
			queue.clear();
			resetGraph();
			return newPos;
		}
	}
}
