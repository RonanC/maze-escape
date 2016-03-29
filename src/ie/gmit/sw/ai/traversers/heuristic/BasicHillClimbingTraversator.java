package ie.gmit.sw.ai.traversers.heuristic;

import ie.gmit.sw.ai.characters.Player;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

import java.awt.Component;

public class BasicHillClimbingTraversator extends Traversator {
	/*
	 * Checks which child has the shortest distance (heuristic) to the goal.
	 * We take the lowest option, if no option is better then the current node then we stay put
	 * 
	 * Cannot go backwards!
	 * 
	 * Chooses the best node and if there is no better it stops.
	 */
//	private Node goal;

	public BasicHillClimbingTraversator(Node[][] maze, int row, int col, int[] goalPos) {
		super(maze, row, col, null);
		setComplete(false);
		setGoalNode(goalPos[1], goalPos[0]);
		traverse();	// fill up allPositions queue.
	}
	
	@Override
	public int[] findNextMove() { // let the helper select one move at a time
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
		long time = System.currentTimeMillis();
		int visitCount = 0;

		Node next = null;
		while (true) {	// safe: node != null (but we always break so it's okay)
			allPositions.add(new int[] { currentNode.getRow(), currentNode.getCol() });
			currentNode.setVisited(true);
			visitCount++;

			if (currentNode.isGoalNode()) {
				time = System.currentTimeMillis() - time; // Stop the clock
				TraversatorStats.printStats(currentNode, time, visitCount);
				break;
			}

			try { // Simulate processing each expanded node
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Node[] children = currentNode.children(mazeArray);	// get all children
			// we are getting the children of that specific node. (passing the maze each time seems over the top, might be better to have a that globally available)
			int fnext = Integer.MAX_VALUE;	// max int, our number will always be lower
			for (int i = 0; i < children.length; i++) {	// 2, 3 or 4
				if (children[i].getHeuristic(goal) < fnext) {	// if distance to goal is less then previous distance
					next = children[i];	// get child node
					fnext = next.getHeuristic(goal);	// set the heuristic to be the distance from the child node to the goal
					System.out.println(fnext);
				} 
			}
			if(fnext == Integer.MAX_VALUE){
				System.out.println("end");
			}

			if (fnext >= currentNode.getHeuristic(goal)) {	// if the next node is closed to the goal then our current node
				System.out.println("Cannot improve on current node " + currentNode.toString() + " \nh(n)="
						+ currentNode.getHeuristic(goal) + " = Local Optimum...");
				break;	// closest possible
			}
			else{
				currentNode = next;	// else next node is the current node, and next = null
				next = null;	// eventually the current node will be null and we will break
			}
			
		}
	}
}