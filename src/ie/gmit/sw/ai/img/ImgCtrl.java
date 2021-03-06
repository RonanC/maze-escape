package ie.gmit.sw.ai.img;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import ie.gmit.sw.ai.GameRunner;

/**
 * Loads in all the images and makes them available through getters.
 * 
 * @author Ronan
 */
public class ImgCtrl {
	// Images
	private Image floor, wall, goal, path, explosion, explosion2, explosion_over;
	private Image helper;
	private Image sword, bomb, medkit;
	private Image player_stand, player_win, player_walk, player_walk2;
	private Image enemy_down, enemy_up;
	private Image punch1, punch2;

	// Image Arrays
	private BufferedImage[] items;
	private BufferedImage[] tiles;
	private BufferedImage[] player;
	private BufferedImage[] enemy;
	private BufferedImage[] misc;

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

		tiles = new BufferedImage[6];
		tiles[0] = ImageIO.read(new java.io.File(url + "tiles/floor.png"));
		tiles[1] = ImageIO.read(new java.io.File(url + "tiles/wall.png"));
		tiles[2] = ImageIO.read(new java.io.File(url + "tiles/path_dark.png"));
		tiles[3] = ImageIO.read(new java.io.File(url + "tiles/explosion.png"));
		tiles[4] = ImageIO.read(new java.io.File(url + "tiles/explosion2.png"));
		tiles[5] = ImageIO.read(new java.io.File(url + "tiles/explosion_over.png"));

		items = new BufferedImage[5];
		items[0] = ImageIO.read(new java.io.File(url + "items/goal.png"));
		items[1] = ImageIO.read(new java.io.File(url + "items/helper.png"));
		items[2] = ImageIO.read(new java.io.File(url + "items/sword.png"));
		items[3] = ImageIO.read(new java.io.File(url + "items/bomb.png"));
		items[4] = ImageIO.read(new java.io.File(url + "items/medkit_lighting.png"));

		player = new BufferedImage[4];
		player[0] = ImageIO.read(new java.io.File(url + "hero/hero_stand.png"));
		player[1] = ImageIO.read(new java.io.File(url + "hero/hero_walk.png"));
		player[2] = ImageIO.read(new java.io.File(url + "hero/hero_walk_2.png"));
		player[3] = ImageIO.read(new java.io.File(url + "hero/hero_happy.png"));

		enemy = new BufferedImage[2];
		enemy[0] = ImageIO.read(new java.io.File(url + "enemy/spider_down.png"));
		enemy[1] = ImageIO.read(new java.io.File(url + "enemy/spider_up.png"));
		
		misc = new BufferedImage[2];
		misc[0] = ImageIO.read(new java.io.File(url + "misc/punch1_big.png"));
		misc[1] = ImageIO.read(new java.io.File(url + "misc/punch2_big.png"));

		// // scale
		// tiles
		floor = scaleImage(tiles[0]);
		wall = scaleImage(tiles[1]);
		path = scaleImage(tiles[2]);
		explosion = scaleImage(tiles[3]);
		explosion2 = scaleImage(tiles[4]);
		explosion_over = scaleImage(tiles[5]);

		// items
		goal = scaleImage(items[0]);
		helper = scaleImage(items[1]);
		sword = scaleImage(items[2]);
		bomb = scaleImage(items[3]);
		medkit = scaleImage(items[4]);

		// player
		player_stand = scaleImage(player[0]);
		player_walk = scaleImage(player[1]);
		player_walk2 = scaleImage(player[2]);
		player_win = scaleImage(player[3]);

		// enemy
		enemy_down = scaleImage(enemy[0]);
		enemy_up = scaleImage(enemy[1]);
		
		// misc
		punch1 = scaleImage(misc[0]);
		punch2 = scaleImage(misc[1]);
		
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

	public Image getPunch1() {
		return punch1;
	}

	public Image getPunch2() {
		return punch2;
	}

	public Image getMedkit() {
		return medkit;
	}

	public Image getPath() {
		return path;
	}

	public Image getExplosion() {
		return explosion;
	}

	public Image getExplosion2() {
		return explosion2;
	}

	public Image getExplosion_over() {
		return explosion_over;
	}
	
	
	
}
