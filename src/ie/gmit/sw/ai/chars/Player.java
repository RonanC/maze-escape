package ie.gmit.sw.ai.chars;

import java.awt.*;
import java.util.Random;

import ie.gmit.sw.ai.GameRunner;
import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.img.ImgCtrl;
/*
 * STATS:
 * Health
 * Steps
 * Sword
 */
public class Player extends Character{
	private Image player;
	private Image player_walk;
	private Image player_walk2;
	private Image player_win;
	private Random random;
	private int stepCount;
	
	// items
	private boolean hasSword;
	private boolean hasBomb;
	
	public Player(Maze map, ImgCtrl imgCtrl) {
		super(map, imgCtrl);
		this.tileDim = GameRunner.TILE_DIM;
		this.mazeDim = GameRunner.MAZE_DIM;
		random = new Random();
	
		setUpImages();
		
		// start position
		resetPos();
		
		// set info
		hasSword = false;
		hasBomb = false;
		health = 100;
	}


	// get items
	public boolean getSwordStatus(){
		return hasSword;
	}
	
	public boolean getBombStatus(){
		return hasBomb;
	}
	
	// set items
	public void setSwordStatus(boolean equip){
		hasSword = equip;
	}
	
	public void setBombStatus(boolean equip){
		hasBomb = equip;
	}
	
	
	
	public void resetPos(){
//		setPos(2, 2);
		randomPos(this);
	}
	
	public void randomPos(Player player) {
		// placed somewhere random
		int x = random.nextInt(mazeDim - 2) + 1;// don't want to choose edges
		int y = random.nextInt(mazeDim - 2) + 1;
		boolean notPlaced = true;
		while (notPlaced) {
			x = random.nextInt(mazeDim - 2) + 1;
			y = random.nextInt(mazeDim - 2) + 1;
//			System.out.printf("x: %d, y: %d\t", x, y);
			// avoid walls and player
			if (!map.getPosElement(x, y).equals("w")) { // we spawn before enemy so we don't nede to worry about spawning on them
				System.out.println(map.getPosElement(x, y));
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
	
	public void setUpImages(){
		// stand
		player = imgCtrl.getPlayer_stand();
		
		// walk
		player_walk = imgCtrl.getPlayer_walk();
		
		// walk_2
		player_walk2 = imgCtrl.getPlayer_walk2();
		
		// win
		player_win = imgCtrl.getPlayer_win();
	}
	
	public Image getPlayer(){
		return player;
	}
	
	public Image getPlayerWalk(){
		return player_walk;
	}
	
	public Image getPlayerWalk2(){
		return player_walk2;
	}
	
	public Image getPlayerWin(){
		return player_win;
	}

	// other mutators
	public int getStepCount() {
		return stepCount;
	}
	
	public void incStepCount(){
		stepCount++;
	}

	
}
