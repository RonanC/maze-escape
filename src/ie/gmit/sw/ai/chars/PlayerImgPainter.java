package ie.gmit.sw.ai.chars;

import java.awt.Graphics;

import ie.gmit.sw.ai.GameCtrl;

// draws the player in various directions and animations.
public class PlayerImgPainter {
	private Player player;
	private int tileDim;
	private char playerLookH;
	private int stepCount;
	private int zoomInPos;

	public PlayerImgPainter(Player player, int tileDim, char playerLookH, int stepCount) {
		this.player = player;
		this.tileDim = tileDim;
		this.playerLookH = playerLookH;
		this.stepCount = stepCount;
		zoomInPos = 2 * tileDim;
	}

	// left draw
	public void lookLeft(Graphics g) {
		g.drawImage(player.getPlayer(), zoomInPos + tileDim, zoomInPos, -tileDim, tileDim, null);
	}

	// right draw
	public void lookRight(Graphics g) {
		g.drawImage(player.getPlayer(), zoomInPos, zoomInPos, tileDim, tileDim, null);
	}

	// // left draw
	// public void lookLeft(Graphics g) {
	// g.drawImage(player.getPlayer(), player.getTileX() * tileDim + tileDim,
	// player.getTileY() * tileDim, -tileDim,
	// tileDim, null);
	// }
	//
	// // right draw
	// public void lookRight(Graphics g) {
	// g.drawImage(player.getPlayer(), player.getTileX() * tileDim,
	// player.getTileY() * tileDim, tileDim, tileDim,
	// null);
	// }

	// walk draw
	public void walk(Graphics g) {
		if (getPlayerLookH() == 'l') {
			if (stepCount % 2 == 0) {
				g.drawImage(player.getPlayerWalk(), zoomInPos + tileDim, zoomInPos, -tileDim, tileDim, null);
			} else {
				g.drawImage(player.getPlayerWalk2(), zoomInPos + tileDim, zoomInPos, -tileDim, tileDim, null);
			}
		} else if (getPlayerLookH() == 'r') {
			if (stepCount % 2 == 0) {
				g.drawImage(player.getPlayerWalk(), zoomInPos, zoomInPos, tileDim, tileDim, null);
			} else {
				g.drawImage(player.getPlayerWalk2(), zoomInPos, zoomInPos, tileDim, tileDim, null);
			}
		}
	}

	// // walk draw
	// public void walk(Graphics g) {
	// if (getPlayerLookH() == 'l') {
	// if (stepCount % 2 == 0) {
	// g.drawImage(player.getPlayerWalk(), player.getTileX() * tileDim +
	// tileDim, player.getTileY() * tileDim,
	// -tileDim, tileDim, null);
	// } else {
	// g.drawImage(player.getPlayerWalk2(), player.getTileX() * tileDim +
	// tileDim, player.getTileY() * tileDim,
	// -tileDim, tileDim, null);
	// }
	// } else if (getPlayerLookH() == 'r') {
	// if (stepCount % 2 == 0) {
	// g.drawImage(player.getPlayerWalk(), player.getTileX() * tileDim,
	// player.getTileY() * tileDim, tileDim,
	// tileDim, null);
	// } else {
	// g.drawImage(player.getPlayerWalk2(), player.getTileX() * tileDim,
	// player.getTileY() * tileDim, tileDim,
	// tileDim, null);
	// }
	// }
	// }

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

	// win draw
	// public void win(Graphics g) {
	// if (getPlayerLookH() == 'l') {
	// if (Board.getTime() % 1000 > 0 && Board.getTime() % 1000 < 500) {
	// g.drawImage(player.getPlayerWin(), player.getTileX() * tileDim + tileDim,
	// player.getTileY() * tileDim,
	// -tileDim, tileDim, null);
	// } else {
	// g.drawImage(player.getPlayer(), player.getTileX() * tileDim + tileDim,
	// player.getTileY() * tileDim,
	// -tileDim, tileDim, null);
	// }
	// } else if (getPlayerLookH() == 'r') {
	// if (Board.getTime() % 1000 > 0 && Board.getTime() % 1000 < 500) {
	// g.drawImage(player.getPlayerWin(), player.getTileX() * tileDim,
	// player.getTileY() * tileDim, tileDim,
	// tileDim, null);
	// } else {
	// g.drawImage(player.getPlayer(), player.getTileX() * tileDim,
	// player.getTileY() * tileDim, tileDim,
	// tileDim, null);
	// }
	// }
	// }

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
