package ie.gmit.sw.ai.chars;

import java.awt.Image;

import ie.gmit.sw.ai.GameRunner;
import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.img.ImgCtrl;


public class Enemy extends Character {
	private Image enemy;
	private Image enemy_2;
	
	// stats
	private int intelLvl;
	// nohealth, they either live or die due to a fight
//	
//	public Enemy(int tileX, int tileY, Maze map, ImgCtrl imgCtrl, int intelLvl) {
//		super(map, imgCtrl);
//		this.tileDim = GameRunner.TILE_DIM;
//		this.intelLvl = intelLvl;
//		
//		setUpImages();
//		
//		// // start position
//		setPos(tileX, tileY);
//	}
	
	public Enemy(Maze map, ImgCtrl imgCtrl) {
		super(map, imgCtrl);
		this.tileDim = GameRunner.TILE_DIM;
		
		// set intel level at 1
		this.intelLvl = 1;

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
