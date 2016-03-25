package ie.gmit.sw.characters;

import java.awt.*;

import javax.swing.ImageIcon;

public class Player extends Character{
	private Image player;
	private Image player_walk;
	private Image player_walk2;
	private Image player_win;
	
	public Player(int tileDim, int tileX, int tileY) {
		this.tileDim = tileDim;
		
		// stand
		player = createImage("hero/hero_stand.png");
		
		// walk
		player_walk = createImage("hero/hero_walk.png");
		
		// walk_2
		player_walk2 = createImage("hero/hero_walk_2.png");
		
		// win
		player_win = createImage("hero/hero_happy.png");
		
		// // start position
		// top left (1 in)
		setPos(tileX, tileY);
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
}
