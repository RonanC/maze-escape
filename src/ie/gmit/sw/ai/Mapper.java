package ie.gmit.sw.ai;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Mapper {

	private Scanner input; // map
	private String map[];
	private Image floor, wall, goal;
	private Image helper;
	private Image sword, bomb;
	private int mazeDim;
	private int tileDim;
	
	public Mapper(int mazeDim, int tileDim) {
		this.mazeDim = mazeDim;
		this.tileDim = tileDim;
		this.map = new String[mazeDim];
		
		// // load in images
		// F - floor
		floor = createImage("tiles/floor.png");
		
		// W - wall
		wall = createImage("tiles/wall.png");

		// G - goal
		goal = createImage("items/goal.png");
		
		// H - helper
		helper = createImage("items/helper.png");
		
		// S - sword
		sword = createImage("items/sword.png");
		
		// B - bomb (grenade)
		bomb = createImage("items/bomb.png");

		// read in map
		openFile();
		readFile();
		closeFile();
	}

	private Image createImage(String tileName) {
		ImageIcon img = new ImageIcon("resources/img/" + tileName); // get imageicon
		Image image = img.getImage(); // transform it into an image
		Image scaledImg = image.getScaledInstance(tileDim, tileDim,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		return scaledImg;
	}
	
	// io
	private void openFile() {
		try {
			input = new Scanner(new File("resources/map/map.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Error loading map.");
		}
	}

	private void readFile() {
		while (input.hasNext()) {
			for (int i = 0; i < mazeDim; i++) {
				map[i] = input.next();
			}
		}
	}

	private void closeFile() {
		input.close();
	}
	
	public void reset(){
		openFile();
		readFile();
		closeFile();
	}
	
	// set tile item
	public void setTileItem(int x, int y, char item) {
		StringBuilder tile = new StringBuilder(map[y]);
		tile.setCharAt(x, item);
		map[y] = tile.toString();
	}
	
	// getters
	public String getMap(int x, int y) {
		String index = map[y].substring(x, x + 1);
		return index;
	}
	
	public void printMap(){
		System.out.println(map.toString());
	}
	
	public Image getFloor() {
		return floor;
	}

	public Image getWall() {
		return wall;
	}
	
	public Image getGoal() {
		return goal;
	}
	
	// more mutators
	public Image getHelper() {
		return helper;
	}

	public void setHelper(Image helper) {
		this.helper = helper;
	}

	public Image getSword() {
		return sword;
	}

	public void setSword(Image sword) {
		this.sword = sword;
	}

	public Image getBomb() {
		return bomb;
	}

	public void setBomb(Image bomb) {
		this.bomb = bomb;
	}
}
