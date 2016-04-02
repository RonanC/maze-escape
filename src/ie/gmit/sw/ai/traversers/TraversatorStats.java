package ie.gmit.sw.ai.traversers;

import java.awt.Color;
import ie.gmit.sw.ai.maze.Node;


/**
 * Prints the statistics for the search once complete.
 * 
 * @author Ronan
 */
public class TraversatorStats {
	
	/**
	 * Prints the statistics for the search once complete.
	 * 
	 * @param node
	 * @param time
	 * @param visitCount
	 */
	public static void printStats(Node node, long time, int visitCount){
		// uncomment to turn on
//		print(node, time, visitCount);    
	}

	@SuppressWarnings("unused")
	private static void print(Node node, long time, int visitCount) {
		double depth = 0;
		
		while (node != null){			
			node = node.getParent();
			if (node != null) node.setColor(Color.YELLOW);
			depth++;
		}
		
        System.out.println("\nVisited " + visitCount + " nodes in " + time + "ms.");
        System.out.println("Found goal at a depth of " + String.format("%.0f", depth));
        System.out.println("EBF = B* = k^(1/d) = " + String.format("%.2f", Math.pow((double) visitCount, (1.00d / depth))));
	}
}
