package ie.gmit.sw.ai;

import javax.swing.*;

public class GameRunner {
	public static int MAZE_DIM;
	public static int TILE_DIM;
	private int screenDim;;
	private int titleHeight;
	
	public static void main(String[] args) {
		new GameRunner();
	}
	
	public GameRunner() {
		MAZE_DIM = 5;
		TILE_DIM = 64;
		screenDim = MAZE_DIM * TILE_DIM;
		titleHeight = 22;
		
		JFrame f = new JFrame();
		f.setResizable(false);
		f.setTitle("Maze Escape");
		f.add(new Board());
		f.setSize(screenDim, screenDim + titleHeight);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
