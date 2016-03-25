package ie.gmit.sw.characters;

import java.awt.Image;


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
	
	public Image getEnemy(){
		return enemy;
	}
	
	public Image getEnemy2(){
		return enemy_2;
	}
}
