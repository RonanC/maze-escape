package ie.gmit.sw.ai.traversers.uninformed;


import ie.gmit.sw.ai.characters.Player;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

import java.awt.Component;
import java.util.*;

/**
 * Implementation of the Brute Force DFS/BFS search algorithm.
 * 
 * @author Ronan
 */
public class tempBrute extends Traversator {
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
	
	public tempBrute(Node[][] maze, int row, int col, boolean depthFirst, Player player) {
		super(maze, row, col, player);
		complete = false;
		this.dfs = depthFirst;
//		initCustom(depthFirst);
	}
	
	@Override
	protected void initCustom() {
		traverse();
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
	
	public void traverse() {
		// Start the clock
		long time = System.currentTimeMillis();
		int visitCount = 0;

		// create a double ended queue of Nodes
		Deque<Node> queue = new LinkedList<Node>();
		queue.offer(currentNode); // adds element to the end of the queue (starting
							// node added)

		while (!queue.isEmpty()) { // while the queue is not empty
			allPositions.add(new int[] { currentNode.getRow(), currentNode.getCol() });
//			System.out.println("queue: " + queue.toString());
			currentNode = queue.poll(); // take from the head

			// same as other algorithms
			currentNode.setVisited(true);
			visitCount++;

			// found node, end
			if (currentNode.isGoalNode()) {
				time = System.currentTimeMillis() - time; // Stop the clock
				TraversatorStats.printStats(currentNode, time, visitCount);
				break;
			}

//			try { // Simulate processing each expanded node
//				if (dfs) {
//					Thread.sleep(4);
//				}else{
//					Thread.sleep(10);
//				}
//				
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

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
		}
	}
}
