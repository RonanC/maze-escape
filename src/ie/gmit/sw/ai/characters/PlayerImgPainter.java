package ie.gmit.sw.ai.characters;

import java.awt.Graphics;

import ie.gmit.sw.ai.GameCtrl;

// 
/**
 * Paints the player in various directions and animations.
 * 
 * @author Ronan
 */
public class PlayerImgPainter {
	private Player player;
	private int tileDim;
	private char playerLookH;
	private int zoomInPos;

	public PlayerImgPainter(Player player, int tileDim, char playerLookH, int stepCount) {
		this.player = player;
		this.tileDim = tileDim;
		this.playerLookH = playerLookH;
		zoomInPos = 2 * tileDim;
	}
	
	// fight
	public void fight(Graphics g){
		if (GameCtrl.getTime() % 2000 > 500 && GameCtrl.getTime() % 2000 < 1000) {
			g.drawImage(player.getPunch1(), zoomInPos, zoomInPos + (tileDim / 8), tileDim, tileDim, null);
		} else if (GameCtrl.getTime() % 2000 > 1500) {
			g.drawImage(player.getPunch2(), zoomInPos, zoomInPos + (tileDim / 8), tileDim, tileDim, null);
		}
		
		if (GameCtrl.getTime() % 1500 < 750) {
			g.drawImage(player.getPlayer(), zoomInPos + tileDim, zoomInPos, -tileDim, tileDim, null);
		} else{
			g.drawImage(player.getPlayer(), zoomInPos, zoomInPos, tileDim, tileDim, null);
		}
	}

	// left draw
	public void lookLeft(Graphics g) {
		g.drawImage(player.getPlayer(), zoomInPos + tileDim, zoomInPos, -tileDim, tileDim, null);
	}

	// right draw
	public void lookRight(Graphics g) {
		g.drawImage(player.getPlayer(), zoomInPos, zoomInPos, tileDim, tileDim, null);
	}

	// walk draw
	public void walk(Graphics g) {
		if (getPlayerLookH() == 'l') {
			if (player.getStepCount() % 2 == 0) {
				g.drawImage(player.getPlayerWalk(), zoomInPos + tileDim, zoomInPos, -tileDim, tileDim, null);
			} else {
				g.drawImage(player.getPlayerWalk2(), zoomInPos + tileDim, zoomInPos, -tileDim, tileDim, null);
			}
		} else if (getPlayerLookH() == 'r') {
			if (player.getStepCount() % 2 == 0) {
				g.drawImage(player.getPlayerWalk(), zoomInPos, zoomInPos, tileDim, tileDim, null);
			} else {
				g.drawImage(player.getPlayerWalk2(), zoomInPos, zoomInPos, tileDim, tileDim, null);
			}
		}
	}

	// win draw
	public void win(Graphics g) {
		if (getPlayerLookH() == 'l') {
			if (GameCtrl.getTime() % 1000 > 0 && GameCtrl.getTime() % 1000 < 500) {
				g.drawImage(player.getPlayerWin(), zoomInPos + tileDim, zoomInPos, -tileDim, tileDim, null);
			} else {
				g.drawImage(player.getPlayer(), zoomInPos + tileDim, zoomInPos, -tileDim, tileDim, null);
			}
		} else if (getPlayerLookH() == 'r') {
			if (GameCtrl.getTime() % 1000 > 0 && GameCtrl.getTime() % 1000 < 500) {
				g.drawImage(player.getPlayerWin(), zoomInPos, zoomInPos, tileDim, tileDim, null);
			} else {
				g.drawImage(player.getPlayer(), zoomInPos, zoomInPos, tileDim, tileDim, null);
			}
		}
	}
	
	// misc
	public void incStepCount() {
		player.incStepCount();
	}

	// mutators
	public char getPlayerLookH() {
		return playerLookH;
	}

	public void setPlayerLookH(char playerLookH) {
		this.playerLookH = playerLookH;
	}

}
