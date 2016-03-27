package ie.gmit.sw.ai.chars;

import java.awt.Image;

import ie.gmit.sw.ai.GameRunner;
import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.img.ImgCtrl;

/*
 * STATS:
 * Health
 * Intelligence
 */
public class Enemy extends Character {
	public static int MAX_HEALTH = 50;
	public static int MAX_INTEL = 2;
	private Image enemy;
	private Image enemy_2;
	private int xpWorth, xpMult;

	// stats
	private int intelLvl;

	public Enemy(Maze map, ImgCtrl imgCtrl) {
		super(map, imgCtrl);
		this.tileDim = GameRunner.TILE_DIM;
		// set intel level at 1
		this.xpMult = 10;
		this.intelLvl = 1;
		this.xpWorth = intelLvl * xpMult;
		
		health = MAX_HEALTH;

		setUpImages();

		// // start position
		setPos(7, 7);
	}
	
	public int getXpWorth(){
		return xpWorth;
	}

	public void setIntelLvl(int intelLvl) {
		if (intelLvl > 0 && intelLvl <= MAX_INTEL) {
			this.intelLvl = intelLvl;
			this.xpWorth = intelLvl * xpMult;
		} else {
			System.out.println("increase max intel level in enemy class");
		}
	}

	public int getIntel() {
		return intelLvl;
	}

	public void setUpImages() {
		// enemy
		enemy = imgCtrl.getEnemy_down();
		// enemy_2
		enemy_2 = imgCtrl.getEnemy_up();
	}

	public Image getEnemy() {
		return enemy;
	}

	public Image getEnemy2() {
		return enemy_2;
	}
}
