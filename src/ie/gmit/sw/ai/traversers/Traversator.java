package ie.gmit.sw.ai.traversers;

import ie.gmit.sw.ai.maze.Node;

public interface Traversator {
	public void init(Node[][] maze, Node start);

	public int findNextMove();
}
