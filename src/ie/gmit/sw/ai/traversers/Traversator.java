package ie.gmit.sw.ai.traversers;

import ie.gmit.sw.ai.Node;

import java.awt.*;
public interface Traversator {
	public void traverse(Node[][] maze, Node start, Component viewer);

}
