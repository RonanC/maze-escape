package ie.gmit.sw.characters;

import java.awt.Image;

import ie.gmit.sw.ai.GameRunner;


public class Enemy extends Character {
	private Image enemy;
	private Image enemy_2;
	
	public Enemy(int tileDim, int tileX, int tileY) {
		this.tileDim = tileDim;
		// enemy
		enemy = createImage("enemy/spider_down.png");
		// enemy_2
		enemy_2 = createImage("enemy/spider_up.png");
		
		// // start position
		setPos(tileX, tileY);
	}
	
	public Enemy() {
		this.tileDim = GameRunner.tileDim;
		// enemy
		enemy = createImage("enemy/spider_down.png");
		// enemy_2
		enemy_2 = createImage("enemy/spider_up.png");
		
		// // start position
		setPos(7,7);
	}
	
	public Image getEnemy(){
		return enemy;
	}
	
	public Image getEnemy2(){
		return enemy_2;
	}
}
