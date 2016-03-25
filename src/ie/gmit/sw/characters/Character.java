package ie.gmit.sw.characters;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Character {
	protected int tileDim;
	private int tileX;
	private int tileY;

	public Character() {
		super();
	}

	protected Image createImage(String tileName) {
		ImageIcon img = new ImageIcon("resources/img/" + tileName); // get imageicon
		Image image = img.getImage(); // transform it into an image
		Image scaledImg = image.getScaledInstance(tileDim, tileDim,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		return scaledImg;
	}

	public void move(int dx, int dy) {
		// x negative go left, positive go right
		// bigger number means you move faster
		// it will move tile by tile
		
		tileX += dx;
		tileY += dy;
	}

	public void setPos(int x, int y) {
		setTileX(x);
		setTileY(y);
	}

	public String getPos() {
		StringBuilder pos = new StringBuilder();
		pos.append(getTileX());
		pos.append(",");
		pos.append(getTileY());
		return pos.toString();
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}

}