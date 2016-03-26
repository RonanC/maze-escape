package ie.gmit.sw.ai;

import javax.swing.*;

public class GameRunner {
	public static int MAZE_DIM;
	public static int TILE_DIM;
	public static int VIEW_DIM;
	public static int ZOOM_DIM;
	public static int SCREEN_DIM;
	public static boolean BG_ON;
	public static int ZOOM_MULT;
	private int titleHeight;
	private int infoBar;

	public static void main(String[] args) {
		new GameRunner();
	}

	public GameRunner() {
		JFrame f = new JFrame();
		
		int ans = Integer.parseInt((String) JOptionPane.showInputDialog(f, "Maze Size?", null, JOptionPane.INFORMATION_MESSAGE, null,
				null, "100"));
		
		System.out.println("ans: " + ans);
		
		if (ans < 20) {
			ans = 20;
			System.out.println("min size is 20");
		}else if (ans > 100){
			ans = 20;
			System.out.println("max size is 100");
		}
		
		int bg = Integer.parseInt((String) JOptionPane.showInputDialog(f, "Music on?\n0: off\n1: on", null, JOptionPane.INFORMATION_MESSAGE, null,
				null, "1"));
		
		if (bg == 0) {
			BG_ON = false;
		}else{
			BG_ON = true;
			System.out.println("");
		}
		
		// size of maze
		MAZE_DIM = ans;
		// scale of tiles (may throw off font)
		TILE_DIM = 64 * 2;

		// others
		VIEW_DIM = 5; // 5 * 5
		// ZOOM_DIM = 16;
		
		// this will be relative anyway
		ZOOM_DIM = 40; // perfect
		
		// test
		ZOOM_MULT = 2;
		ZOOM_DIM = 32 * ZOOM_MULT;
//		ZOOM_DIM = 20; // 20x20
//		ZOOM_DIM = 6; // 100x100
		
		
		//MAZE_DIM / 16; //6;
		
		
		// screenDim = MAZE_DIM * TILE_DIM;// full screen
		// plus an info bar
		SCREEN_DIM = TILE_DIM * VIEW_DIM;
		infoBar = TILE_DIM;
		titleHeight = 22;

		
		f.setResizable(false);
		f.setTitle("Maze Escape");
		f.add(new GameCtrl());
		f.setSize(SCREEN_DIM, SCREEN_DIM + titleHeight + infoBar); // + infoBar
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
