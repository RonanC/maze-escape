package ie.gmit.sw.ai.traversers.uninformed;

import ie.gmit.sw.ai.characters.Player;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

/**
 * Implementation of the Random Walk search algorithm.
 * 
 * @author Ronan
 */
public class RandomWalk extends Traversator {
	/*
	 * Sets the current node to visited, then chooses a random child node and
	 * moves to it
	 * 
	 * low memory, high time
	 */

	public RandomWalk(Node[][] maze, int row, int col, Player player) {
		super(maze, row, col, player);
		initCustom();
	}
	
	private void initCustom() {
		complete = false;
		steps = (int) Math.pow(mazeArray.length, 2) * 2;
//		System.out.println("Number of steps allowed: " + steps);
	}

	public int[] findNextMove() { // traverse one step through the algorithm at a time
		resetNewPos();
		if (visitCount <= steps && currentNode != null) {
			currentNode.setVisited(true); // this is what changes the color of the node
			visitCount++;

			if (currentNode.isGoalNode()) {
				time = System.currentTimeMillis() - time; // Stop the clock
				TraversatorStats.printStats(currentNode, time, visitCount);
				complete = true;
				newPos[0] = 4;
				return newPos; // found goal
			} else {
				try { // Needed to let the screen paint each move (enemies skip spaces otherwise).
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// Pick a random adjacent node
				Node[] children = currentNode.children(mazeArray); // get all children
				// choose a random child
				int choice = (int) (children.length * Math.random());
				// System.out.println("children choice: " + choice);
				try {
					currentNode = children[choice];
				} catch (Exception e) {
//					System.out.println("error:" + e.getMessage());
				}
				newPos[0] = choice;
				return newPos; // 0, 1, 2, 3
			}
		}
		newPos[0] = 5;
		return newPos; // out of steps

	}

}
