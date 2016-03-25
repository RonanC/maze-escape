package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Mapper {

	private Scanner input; // map
	private String map[];
	private Image floor, wall, goal;
	private Image helper;
	private Image sword, bomb;
	private Image player_stand, player_win, player_walk, player_walk2;
	private Image enemy_down, enemy_up;
	private int mazeDim;
	private int tileDim;
	
	// images
	private BufferedImage[] items;
	private BufferedImage[] tiles;
	private BufferedImage[] player;
	private BufferedImage[] enemy;
	
//	private static final int IMAGE_COUNT = 6;
	
	public Mapper(int mazeDim, int tileDim) {
		this.mazeDim = mazeDim;
		this.tileDim = tileDim;
		this.map = new String[mazeDim];
		
		try {
			initImages();
		} catch (Exception e) {
			System.out.println("Error loading images.");
			e.printStackTrace();
		}

		// read in map
		openFile();
		readFile();
		closeFile();
	}
	
	// new image
	private void initImages() throws Exception {
		String url = "resources/img/";
		
		tiles = new BufferedImage[2];
		tiles[0] = ImageIO.read(new java.io.File(url + "tiles/floor.png"));
		tiles[1] = ImageIO.read(new java.io.File(url + "tiles/wall.png"));
		
		items = new BufferedImage[5];
		items[0] = ImageIO.read(new java.io.File(url + "items/goal.png"));
		items[1] = ImageIO.read(new java.io.File(url + "items/helper.png"));
		items[2] = ImageIO.read(new java.io.File(url + "items/sword.png"));
		items[3] = ImageIO.read(new java.io.File(url + "items/bomb.png"));
		
		player = new BufferedImage[4];
		player[0] = ImageIO.read(new java.io.File(url + "hero/hero_stand.png"));
		player[1] = ImageIO.read(new java.io.File(url + "hero/hero_walk.png"));
		player[2] = ImageIO.read(new java.io.File(url + "hero/hero_walk_2.png"));
		player[3] = ImageIO.read(new java.io.File(url + "hero/hero_happy.png"));
		
		enemy = new BufferedImage[2];
		enemy[0] = ImageIO.read(new java.io.File(url + "enemy/spider_down.png"));
		enemy[1] = ImageIO.read(new java.io.File(url + "enemy/spider_up.png"));
		
		// // scale
		// tiles
		floor = scaleImage(tiles[0]);
		wall = scaleImage(tiles[1]);
		
		// items
		goal = scaleImage(items[0]);
		helper = scaleImage(items[1]);
		sword = scaleImage(items[2]);
		bomb = scaleImage(items[3]);
		
		// player
		player_stand = scaleImage(player[0]);
		player_walk = scaleImage(player[1]);
		player_walk2 = scaleImage(player[2]);
		player_win = scaleImage(player[3]);
		
		// enemy
		enemy_down = scaleImage(enemy[0]);
		enemy_up = scaleImage(enemy[1]);
	}
	
	
	
//	private void initImagesOld(){
//		// // load in images
//		// F - floor
//		floor = createImage("tiles/floor.png");
//		
//		// W - wall
//		wall = createImage("tiles/wall.png");
//
//		// G - goal
//		goal = createImage("items/goal.png");
//		
//		// H - helper
//		helper = createImage("items/helper.png");
//		
//		// S - sword
//		sword = createImage("items/sword.png");
//		
//		// B - bomb (grenade)
//		bomb = createImage("items/bomb.png");
//	}
	
	public static Image scaleImage(Image image){
		image = image.getScaledInstance(GameRunner.TILE_DIM, GameRunner.TILE_DIM, java.awt.Image.SCALE_SMOOTH);
		return image;
	}

//	private Image createImage(String tileName) {
//		ImageIcon img = new ImageIcon("resources/img/" + tileName); // get imageicon
//		
//		Image image = null;
//		try {
//			image = (Image) img.getImage(); // transform it into an image	
//		} catch (Exception e) {
//			img = new ImageIcon("resources/img/" + tileName); // get imageicon
//			image = (Image) img.getImage(); // transform it into an image	
//		}
//
//		Image scaledImg;
//		try {
//			scaledImg = image.getScaledInstance(tileDim, tileDim, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
//		} catch (Exception e) {
//			System.out.println("caught.");
//			scaledImg = image.getScaledInstance(tileDim, tileDim, java.awt.Image.SCALE_DEFAULT);
//		}
//		 
//		return scaledImg;
//	}
	
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
	public String getPosElement(int x, int y) {
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

	public Image getSword() {
		return sword;
	}

	public Image getBomb() {
		return bomb;
	}

	public Image getPlayer_stand() {
		return player_stand;
	}

	public Image getPlayer_win() {
		return player_win;
	}

	public Image getPlayer_walk() {
		return player_walk;
	}

	public Image getPlayer_walk2() {
		return player_walk2;
	}

	public Image getEnemy_down() {
		return enemy_down;
	}

	public Image getEnemy_up() {
		return enemy_up;
	}

	
	
}
