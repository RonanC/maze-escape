package ie.gmit.sw.ai;

import javax.swing.*;

public class GameRunner {
	
	private int mazeDim;
	private int tileDim;
	private int screenDim;;
	private int titleHeight;
	
	public static void main(String[] args) {
		new GameRunner();
	}
	
	public GameRunner() {
		mazeDim = 14;
		tileDim = 64;
		screenDim = mazeDim * tileDim;
		titleHeight = 22;
		
		JFrame f = new JFrame();
		f.setResizable(false);
		f.setTitle("Maze Escape");
		f.add(new Board(mazeDim, tileDim));
		f.setSize(screenDim, screenDim + titleHeight);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
