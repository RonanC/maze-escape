package ie.gmit.sw.ai.traversers;

import java.awt.*;

import ie.gmit.sw.ai.maze.Node;
public interface Traversator {
	public void traverse(Node[][] maze, Node start, Component viewer);

}
