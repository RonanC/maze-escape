package ie.gmit.sw.ai;

import javax.swing.*;

public class GameRunner {
	public static int MAZE_DIM;
	public static int TILE_DIM;
	public static int VIEW_DIM;
	private int screenDim;
	private int titleHeight;
	private int infoBar;
	
	public static void main(String[] args) {
		new GameRunner();
	}
	
	public GameRunner() {
		MAZE_DIM = 100;
		TILE_DIM = 64;
		VIEW_DIM = 5; // 5 * 5
//		screenDim = MAZE_DIM * TILE_DIM;// full screen
		// plus an info bar
		screenDim = TILE_DIM * VIEW_DIM;
		infoBar = TILE_DIM;
		titleHeight = 22;
		
		JFrame f = new JFrame();
		f.setResizable(false);
		f.setTitle("Maze Escape");
		f.add(new Board());
		f.setSize(screenDim, screenDim + titleHeight);	//  + infoBar
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
