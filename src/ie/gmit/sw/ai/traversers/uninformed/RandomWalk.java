package ie.gmit.sw.ai.traversers.uninformed;

import java.awt.Component;

import ie.gmit.sw.ai.*;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.TraversatorStats;

public class RandomWalk implements Traversator{
	/*
	 * Sets the current node to visited, then chooses a random child node and moves to it
	 * 
	 * low memory, high time
	 */
	public void traverse(Node[][] maze, Node node, Component viewer) {
        long time = System.currentTimeMillis();
    	int visitCount = 0;
    	   	
		int steps = (int) Math.pow(maze.length, 2) * 2;
		System.out.println("Number of steps allowed: " + steps);
		
		boolean complete = false;
		while(visitCount <= steps && node != null){		
			node.setVisited(true);	// this is what changes the color of the node
			visitCount++;
			if (visitCount % 10 == 0) viewer.repaint();
			
			if (node.isGoalNode()){
		        time = System.currentTimeMillis() - time; //Stop the clock
		        TraversatorStats.printStats(node, time, visitCount);
		        viewer.repaint();
		        complete = true;
				break;
			}
			
			try { //Simulate processing each expanded node
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Pick a random adjacent node
        	Node[] children = node.children(maze);	// get all children
        	node = children[(int)(children.length * Math.random())];		// choose a random child	
		}
		
		if (!complete) System.out.println("*** Out of steps....");
	}
}
