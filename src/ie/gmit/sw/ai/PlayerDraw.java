package ie.gmit.sw.ai;

import java.awt.Graphics;

import ie.gmit.sw.characters.Player;

// draws the player in various directions and animations.
public class PlayerDraw {
	private Player player;
	private int tileDim;
	private char playerLookH;
	private int stepCount;

	public PlayerDraw(Player player, int tileDim, char playerLookH, int stepCount) {
		this.player = player;
		this.tileDim = tileDim;
		this.playerLookH = playerLookH;
		this.stepCount = stepCount;
	}

	// left draw
	public void lookLeft(Graphics g) {
		g.drawImage(player.getPlayer(), player.getTileX() * tileDim + tileDim, player.getTileY() * tileDim, -tileDim,
				tileDim, null);
	}

	// right draw
	public void lookRight(Graphics g) {
		g.drawImage(player.getPlayer(), player.getTileX() * tileDim, player.getTileY() * tileDim, tileDim, tileDim,
				null);
	}

	// walk draw
	public void walk(Graphics g) {
		if (getPlayerLookH() == 'l') {
			if (stepCount % 2 == 0) {
				g.drawImage(player.getPlayerWalk(), player.getTileX() * tileDim + tileDim, player.getTileY() * tileDim,
						-tileDim, tileDim, null);
			} else {
				g.drawImage(player.getPlayerWalk2(), player.getTileX() * tileDim + tileDim, player.getTileY() * tileDim,
						-tileDim, tileDim, null);
			}
		} else if (getPlayerLookH() == 'r') {
			if (stepCount % 2 == 0) {
				g.drawImage(player.getPlayerWalk(), player.getTileX() * tileDim, player.getTileY() * tileDim, tileDim,
						tileDim, null);
			} else {
				g.drawImage(player.getPlayerWalk2(), player.getTileX() * tileDim, player.getTileY() * tileDim, tileDim,
						tileDim, null);
			}
		}
	}

	// win draw
	public void win(Graphics g) {
		if (getPlayerLookH() == 'l') {
			if (Board.getTime() % 1000 > 0 && Board.getTime() % 1000 < 500) {
				g.drawImage(player.getPlayerWin(), player.getTileX() * tileDim + tileDim, player.getTileY() * tileDim,
						-tileDim, tileDim, null);
			} else {
				g.drawImage(player.getPlayer(), player.getTileX() * tileDim + tileDim, player.getTileY() * tileDim,
						-tileDim, tileDim, null);
			}
		} else if (getPlayerLookH() == 'r') {
			if (Board.getTime() % 1000 > 0 && Board.getTime() % 1000 < 500) {
				g.drawImage(player.getPlayerWin(), player.getTileX() * tileDim, player.getTileY() * tileDim, tileDim,
						tileDim, null);
			} else {
				g.drawImage(player.getPlayer(), player.getTileX() * tileDim, player.getTileY() * tileDim, tileDim,
						tileDim, null);
			}
		}
	}
	
	// misc
	public void incStepCount() {
		stepCount++;
	}

	// mutators
	public char getPlayerLookH() {
		return playerLookH;
	}

	public void setPlayerLookH(char playerLookH) {
		this.playerLookH = playerLookH;
	}

}
