package ie.gmit.sw.ai.characters;

import java.awt.*;
import java.util.Deque;
import java.util.Random;

import ie.gmit.sw.ai.GameRunner;
import ie.gmit.sw.ai.img.ImgCtrl;
import ie.gmit.sw.ai.maze.Maze;
import ie.gmit.sw.ai.traversers.heuristic.WorstFirstTraversator;

/*
 * STATS:
 * Health
 * Steps/Luck
 * Sword
 */

/**
 * Contains player specific methods and variables.
 * Extends the Character super class.
 * 
 * @author Ronan
 */
public class Player extends Character {
	public static int MAX_HEALTH = 100;
	private Image player;
	private Image player_walk;
	private Image player_walk2;
	private Image player_win;
	private Image punch1, punch2;
	private Random random;
	private int stepCount;

	// when enemy dead gain xp
	// not used at the moment
	private int xp;

	// items
	private boolean hasSword;
	private boolean hasBomb;

	private int swordStrength;
	private int swordMaxStrength;

	// set player far away
	private Maze maze;
	int[] goalPos;
	Deque<int[]> allPositions;

	public Player(Maze map, ImgCtrl imgCtrl) {
		super(map, imgCtrl);
		this.tileDim = GameRunner.TILE_DIM;
		this.mazeDim = GameRunner.MAZE_DIM;
		random = new Random();
		this.xp = 0;

		setUpImages();

		// set player far away
		this.maze = map;

		this.goalPos = new int[] { maze.getGoalNode().getRow(), maze.getGoalNode().getCol() };
		// start position
		// resetPos();
		setFarAwayPos();

		// set info
		hasSword = false;
		hasBomb = false;
		health = MAX_HEALTH;

		swordMaxStrength = 2;
		swordStrength = 0;
	}

	public void setFarAwayPos() {
//		char element = 'w';
		int rowSave = 5;
		int colSave = 5;
		// starting node for algo
		for (int row = 0; row < maze.getMazeArray().length; row++) {
			for (int col = 0; col < maze.getMazeArray()[0].length; col++) {
				if (maze.getMazeArray()[row][col].getElement() == 'f') {
					rowSave = row;
					colSave = col;
					break;
				}
			}
		}
		System.out.println("Algo: Start Node, row: " + rowSave + ", col: " +colSave);
		int rowGoal = GameRunner.MAZE_DIM - 2;;
		int colGoal = GameRunner.MAZE_DIM - 2;;
		// goal node will be opposite
		for (int row = maze.getMazeArray().length - 1; row > 0; row--) {
			for (int col = maze.getMazeArray()[0].length - 1; col > 0 ; col--) {
				if (maze.getMazeArray()[row][col].getElement() == 'f') {
					rowGoal = row;
					colGoal = col;
					break;
				}
			}
		}
		System.out.println("Algo: Goal Node, row: " + rowGoal + ", col: " +colGoal);
		goalPos[0] = rowGoal;
		goalPos[1] = colGoal;
		

		WorstFirstTraversator wfs = new WorstFirstTraversator(maze.getMazeArray(), rowSave, colSave, goalPos, maze.getGoalNode());
		allPositions = wfs.getAllPositions();
		// int counter = 0;
		// for (int[] pos : allPositions) {
		// counter++;
		// System.out.println("Pos " + counter + ", row:" + pos[0] + ", col:" +
		// pos[1]);
		// }
		System.out.println("Player Starting pos, row: " + wfs.getWorstNode().getRow() + ", col: " + wfs.getWorstNode().getCol());
		setPos(wfs.getWorstNode().getCol(), wfs.getWorstNode().getRow());
	}

	// get items
	public boolean getSwordStatus() {
		return hasSword;
	}

	public boolean getBombStatus() {
		return hasBomb;
	}

	// set items
	public void setSwordStatus(boolean equip) {
		hasSword = equip;

		if (equip) {
			swordStrength = swordMaxStrength;
		}
	}

	public void swordDec() {
		swordStrength--;
		if (swordStrength <= 0) {
			setSwordStatus(false);
		}
	}

	public void setBombStatus(boolean equip) {
		hasBomb = equip;
	}

	public void incXp(int amount) {
		xp += amount;
	}

	public void resetPos() {
		// setPos(2, 2);
//		randomPos(this);
		setFarAwayPos();
	}

	public void randomPos(Player player) {
		// placed somewhere random
		int x = random.nextInt(mazeDim - 2) + 1;// don't want to choose edges
		int y = random.nextInt(mazeDim - 2) + 1;
		boolean notPlaced = true;
		while (notPlaced) {
			x = random.nextInt(mazeDim - 2) + 1;
			y = random.nextInt(mazeDim - 2) + 1;
			// System.out.printf("x: %d, y: %d\t", x, y);
			// avoid walls and player
			if (!mazeGlobal.getPosElement(x, y).equals("w")) { 
//				 we spawn before enemy so we don't nede to worry about spawning on them
				System.out.println(mazeGlobal.getPosElement(x, y));
				player.setPos(x, y);
				notPlaced = false;
				// System.out.println("placing");
			}
		}
	}

	public Player(int tileX, int tileY, Maze map, ImgCtrl imgCtrl) {
		super(map, imgCtrl);
		this.tileDim = GameRunner.TILE_DIM;

		setUpImages();

		// // start position
		// top left (1 in)
		setPos(tileX, tileY);
	}

	public void setUpImages() {
		// stand
		player = imgCtrl.getPlayer_stand();

		// walk
		player_walk = imgCtrl.getPlayer_walk();

		// walk_2
		player_walk2 = imgCtrl.getPlayer_walk2();

		// win
		player_win = imgCtrl.getPlayer_win();

		// fight
		punch1 = imgCtrl.getPunch1();
		punch2 = imgCtrl.getPunch2();
	}

	public Image getPlayer() {
		return player;
	}

	public Image getPlayerWalk() {
		return player_walk;
	}

	public Image getPlayerWalk2() {
		return player_walk2;
	}

	public Image getPlayerWin() {
		return player_win;
	}

	public Image getPunch1() {
		return punch1;
	}

	public Image getPunch2() {
		return punch2;
	}

	// other mutators
	public int getStepCount() {
		return stepCount;
	}

	public void incStepCount() {
		stepCount++;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	
}
