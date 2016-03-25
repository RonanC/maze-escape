package ie.gmit.sw.characters;

import java.awt.*;

import javax.swing.ImageIcon;

public class Player {
	private int tileDim;
	private int tileX, tileY;

	private Image player;
	private Image player_walk;
	private Image player_walk2;
	private Image player_win;
	
	public Player(int tileDim) {
		this.tileDim = tileDim;
		
		// stand
		player = createImage("hero_stand.png");
		
		// walk
		player_walk = createImage("hero_walk.png");
		
		// walk_2
		player_walk2 = createImage("hero_walk_2.png");
		
		// win
		player_win = createImage("hero_happy.png");
		
		// // start position
		// top left (1 in)
		tileX = 1;
		tileY = 1;
	}
	
	private Image createImage(String tileName) {
		ImageIcon img = new ImageIcon("resources/img/hero/" + tileName); // get imageicon
		Image image = img.getImage(); // transform it into an image
		Image scaledImg = image.getScaledInstance(tileDim, tileDim,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		return scaledImg;
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
	
	public void move(int dx, int dy){
		// x negative go left, positive go right
		// bigger number means you move faster
		// it will move tile by tile
		
		tileX += dx;
		tileY += dy;
	}
	
	// getters
	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}
	
	// setters
	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}
}
