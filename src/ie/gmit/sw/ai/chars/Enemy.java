package ie.gmit.sw.ai.chars;

import java.awt.Image;

import ie.gmit.sw.ai.GameRunner;
import ie.gmit.sw.ai.Mapper;


public class Enemy extends Character {
	private Image enemy;
	private Image enemy_2;
	
	public Enemy(int tileX, int tileY, Mapper map) {
		super(map);
		this.tileDim = GameRunner.TILE_DIM;
		
		setUpImages();
		
		// // start position
		setPos(tileX, tileY);
	}
	
	public Enemy(Mapper map) {
		super(map);
		this.tileDim = GameRunner.TILE_DIM;

		setUpImages();
		
		// // start position
		setPos(7,7);
	}
	
	public void setUpImages(){
		// enemy
		enemy = map.getEnemy_down();
		// enemy_2
		enemy_2 = map.getEnemy_up();
	}
	
	public Image getEnemy(){
		return enemy;
	}
	
	public Image getEnemy2(){
		return enemy_2;
	}
}
