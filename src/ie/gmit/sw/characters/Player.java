package ie.gmit.sw.characters;

import java.awt.*;

import ie.gmit.sw.ai.GameRunner;
import ie.gmit.sw.ai.Mapper;



public class Player extends Character{
	private Image player;
	private Image player_walk;
	private Image player_walk2;
	private Image player_win;
	
	public Player(Mapper map) {
		super(map);
		this.tileDim = GameRunner.TILE_DIM;
	
		setUpImages();
		
		// // start position
		// top left (1 in)
		setPos(1, 1);
	}
	
	public Player(int tileX, int tileY, Mapper map) {
		super(map);
		this.tileDim = GameRunner.TILE_DIM;
	
		setUpImages();
		
		// // start position
		// top left (1 in)
		setPos(tileX, tileY);
	}
	
	public void setUpImages(){
		// stand
		player = map.getPlayer_stand();
		
		// walk
		player_walk = map.getPlayer_walk();
		
		// walk_2
		player_walk2 = map.getPlayer_walk2();
		
		// win
		player_win = map.getPlayer_win();
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
