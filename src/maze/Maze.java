package maze;

import java.awt.Color;

import javax.swing.*;

public class Maze {
	
	private int mazeDim;
	private int tileDim;
	private int screenDim;;
	private int titleHeight;
	
	public static void main(String[] args) {
		new Maze();
	}
	
	public Maze() {
		mazeDim = 14;
		tileDim = 64;
		screenDim = mazeDim * tileDim;
		titleHeight = 22;
		
		JFrame f = new JFrame();
		f.setResizable(false);
		f.setTitle("Maze Game");
		f.add(new Board(mazeDim, tileDim));
		f.setSize(screenDim, screenDim + titleHeight);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBackground(Color.DARK_GRAY);
	}
}
