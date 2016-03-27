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
	public static int MAX_HEALTH = 100;
	private Image player;
	private Image player_walk;
	private Image player_walk2;
	private Image player_win;
	private Image punch1, punch2;
	private Random random;
	private int stepCount;

	// when enemy dead gain xp
	private int xp;
	
	// items
	private boolean hasSword;
	private boolean hasBomb;
	
	private int swordStrength;
	private int swordMaxStrength;
	
	public Player(Maze map, ImgCtrl imgCtrl) {
		super(map, imgCtrl);
		this.tileDim = GameRunner.TILE_DIM;
		this.mazeDim = GameRunner.MAZE_DIM;
		random = new Random();
		this.xp = 0;
	
		setUpImages();
		
		// start position
		resetPos();
		
		// set info
		hasSword = false;
		hasBomb = false;
		health = MAX_HEALTH;
		
		swordMaxStrength = 2;
		swordStrength = 0;
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
		
		if (equip) {
			swordStrength = swordMaxStrength;
		}
	}
	
	public void swordDec(){
		swordStrength--;
		if (swordStrength <= 0) {
			setSwordStatus(false);
		}
	}
	
	public void setBombStatus(boolean equip){
		hasBomb = equip;
	}
	
	public void incXp(int amount){
		xp += amount;
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
		
		// fight
		punch1 = imgCtrl.getPunch1();
		punch2 = imgCtrl.getPunch2();
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
	
	public Image getPunch1(){
		return punch1;
	}
	
	public Image getPunch2(){
		return punch2;
	}

	// other mutators
	public int getStepCount() {
		return stepCount;
	}
	
	public void incStepCount(){
		stepCount++;
	}

	
}
