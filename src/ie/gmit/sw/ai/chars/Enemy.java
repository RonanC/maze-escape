package ie.gmit.sw.ai.chars;

import java.awt.Image;

import ie.gmit.sw.ai.GameRunner;
import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.img.ImgCtrl;


public class Enemy extends Character {
	private Image enemy;
	private Image enemy_2;
	
	public Enemy(int tileX, int tileY, Maze map, ImgCtrl imgCtrl) {
		super(map, imgCtrl);
		this.tileDim = GameRunner.TILE_DIM;
		
		setUpImages();
		
		// // start position
		setPos(tileX, tileY);
	}
	
	public Enemy(Maze map, ImgCtrl imgCtrl) {
		super(map, imgCtrl);
		this.tileDim = GameRunner.TILE_DIM;

		setUpImages();
		
		// // start position
		setPos(7,7);
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
