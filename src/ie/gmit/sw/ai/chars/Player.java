package ie.gmit.sw.ai.chars;

import java.awt.*;

import ie.gmit.sw.ai.GameRunner;
import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.img.ImgCtrl;



public class Player extends Character{
	private Image player;
	private Image player_walk;
	private Image player_walk2;
	private Image player_win;
	
	public Player(Maze map, ImgCtrl imgCtrl) {
		super(map, imgCtrl);
		this.tileDim = GameRunner.TILE_DIM;
	
		setUpImages();
		
		// // start position
		// top left (1 in)
		setPos(1, 1);
	}
	
	public Player(int tileX, int tileY, Maze map, ImgCtrl imgCtrl) {
		super(map, imgCtrl);
		this.tileDim = GameRunner.TILE_DIM;
	
		setUpImages();
		
		// // start position
		// top left (1 in)
		setPos(tileX, tileY);
	}
	
	public void setUpImages(){
		// stand
		player = imgCtrl.getPlayer_stand();
		
		// walk
		player_walk = imgCtrl.getPlayer_walk();
		
		// walk_2
		player_walk2 = imgCtrl.getPlayer_walk2();
		
		// win
		player_win = imgCtrl.getPlayer_win();
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
