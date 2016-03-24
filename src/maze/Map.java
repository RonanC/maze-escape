package maze;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Map {

	private Scanner input; // map
	private String map[];
	private Image ground, wall, goal;
	private int mazeDim;
	private int tileDim;
	
	public Map(int mazeDim, int tileDim) {
		this.mazeDim = mazeDim;
		this.tileDim = tileDim;
		this.map = new String[mazeDim];
		
		// load in images
		ImageIcon img = new ImageIcon("resources/ground.png");
		img = resizeImage(img);
		ground = img.getImage();
		
		img = new ImageIcon("resources/wall.png");
		img = resizeImage(img);
		wall = img.getImage();
		
		img = new ImageIcon("resources/goal.png");
		img = resizeImage(img);
		goal = img.getImage();
		
		// read in map
		openFile();
		readFile();
		closeFile();
	}

	private ImageIcon resizeImage(ImageIcon img) {
		Image image = img.getImage(); // transform it 
		Image newimg = image.getScaledInstance(tileDim, tileDim,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		img = new ImageIcon(newimg);  // transform it back
		return img;
	}
	
	// io
	private void openFile() {
		try {
			input = new Scanner(new File("resources/map.txt"));
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
	
	// getters
	public String getMap(int x, int y) {
		String index = map[y].substring(x, x + 1);
		return index;
	}
	
	public Image getGround() {
		return ground;
	}

	public Image getWall() {
		return wall;
	}
	
	public Image getGoal() {
		return goal;
	}
}
