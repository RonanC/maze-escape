package ie.gmit.sw.ai.img;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import ie.gmit.sw.ai.GameRunner;

public class ImgCtrl {
	// Images
	private Image floor, wall, goal;
	private Image helper;
	private Image sword, bomb;
	private Image player_stand, player_win, player_walk, player_walk2;
	private Image enemy_down, enemy_up;

	// Image Arrays
	private BufferedImage[] items;
	private BufferedImage[] tiles;
	private BufferedImage[] player;
	private BufferedImage[] enemy;

	public ImgCtrl() {
		try {
			initImages();
		} catch (Exception e) {
			System.out.println("Error loading images.");
			e.printStackTrace();
		}
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

	public static Image scaleImage(Image image) {
		image = image.getScaledInstance(GameRunner.TILE_DIM, GameRunner.TILE_DIM, java.awt.Image.SCALE_SMOOTH);
		return image;
	}

	// getters
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
