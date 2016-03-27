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
	private Image enemy;
	private Image enemy_2;
	
	// stats
	private int intelLvl;
	
	public Enemy(Maze map, ImgCtrl imgCtrl) {
		super(map, imgCtrl);
		this.tileDim = GameRunner.TILE_DIM;
		
		// set intel level at 1
		this.intelLvl = 1;
		health = 50;

		setUpImages();
		
		// // start position
		setPos(7,7);
	}
	
	public void setIntelLvl(int intelLvl){
		if (intelLvl > 0 && intelLvl <=2) {
			this.intelLvl = intelLvl;
		}else{
			System.out.println("increase max intel level in enemy class");
		}
	}
	
	public void setUpImages(){
		// enemy
		enemy = imgCtrl.getEnemy_down();
		// enemy_2
		enemy_2 = imgCtrl.getEnemy_up();
	}
	
	public Image getEnemy(){
		return enemy;
	}
	
	public Image getEnemy2(){
		return enemy_2;
	}
}
