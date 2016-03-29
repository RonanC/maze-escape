package ie.gmit.sw.ai.characters;

import java.awt.Component;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import ie.gmit.sw.ai.GameRunner;
import ie.gmit.sw.ai.audio.SoundEffects;
import ie.gmit.sw.ai.fight.FightCtrl;
import ie.gmit.sw.ai.img.ImgCtrl;
import ie.gmit.sw.ai.maze.Maze;

public class EnemyBrain { // extends thread
	private ArrayList<Enemy> enemyList;
	private int mazeDim = GameRunner.MAZE_DIM;
	private Maze map;
	private Timer timer;
	private Random random;
	private Player player;
	private ImgCtrl imgCtrl;

	private boolean enemySpawned;
	private FightCtrl fightCtrl;

	private Component viewer;
	private LinkedList<EnemyTask> enemyTasks;
	private GeneralTask gt;

	public EnemyBrain(Maze map, ArrayList<Enemy> enemyList, Player player, ImgCtrl imgCtrl, FightCtrl fightCtrl,
			Component viewer) {
		this.imgCtrl = imgCtrl;
		this.player = player;
		this.map = map;
		this.enemyList = enemyList;
		this.fightCtrl = fightCtrl;
		random = new Random();
		timer = new Timer();
		this.viewer = viewer;

		enemySpawned = false;
		enemyTasks = new LinkedList<EnemyTask>();
	}

	public void spawn() {
		System.out.println("spawning enemies");
		// create enemy tasks
		for (Enemy enemy : enemyList) {
			// System.out.println("new enemy");
			// lvl1 enemy
			randomPos(enemy);

			EnemyTask enemyTask = new EnemyTask(map, enemy, player);
			enemyTasks.add(enemyTask);
			// schedule the start for every second
			// timer.schedule(enemyTask, 0, 500);
		}
		gt = new GeneralTask();
		timer.schedule(gt, 0, 500);
	}

	public void createEnemies(int enemyNum) {
		killAllEnemies();

		for (int i = 0; i < enemyNum; i++) {
			enemyList.add(new Enemy(map, imgCtrl));
		}
		enemySpawned = true;
		spawn();
	}

	public boolean getEnemySpawned() {
		return enemySpawned;
	}

	public void setEnemySpawned(boolean spawned) {
		enemySpawned = spawned;
	}

	public void resetAllPos() {
		for (Enemy enemy : enemyList) {
			randomPos(enemy);
		}
	}

	public void killAllEnemies() {
		enemyList.clear();
		setEnemySpawned(false);
	}

	public void randomPos(Enemy enemy) {
		// placed somewhere random
		int x = random.nextInt(mazeDim - 2) + 1;// don't want to choose edges
		int y = random.nextInt(mazeDim - 2) + 1;
		boolean notPlaced = true;
		while (notPlaced) {
			x = random.nextInt(mazeDim - 2) + 1;
			y = random.nextInt(mazeDim - 2) + 1;
			// System.out.printf("x: %d, y: %d\t", x, y);
			// avoid walls and player
			if (!map.getPosElement(x, y).equals("w") && !enemy.getPos().equals(player.getPos())) {
				// System.out.println(map.getPosElement(x, y));
				enemy.setPos(x, y);
				notPlaced = false;
				// System.out.println("placing");
			}
		}
	}

	public class GeneralTask extends TimerTask {

		@Override
		public void run() {
			if (!enemyTasks.isEmpty()) {

				try {
					for (EnemyTask enemyTask : enemyTasks) {
						enemyTask.run();
					}
				} catch (Exception e) {
					System.out.println("error: " + e.getMessage());
				}

			}

		}

	}

	// Single enemy
	public class EnemyTask {
		// this enemy has a form, is in a place and knows that there is a
		// player.
		private Maze map;
		private Enemy enemy;
		private Player player;
		private Random random;

		public EnemyTask(Maze map, Enemy enemy, Player player) {
			this.map = map;
			this.enemy = enemy;
			this.player = player;
			random = new Random();
		}

//		@Override
		public void run() {
			viewer.repaint();
			if (!enemy.isAlive()) {
				enemyList.remove(enemy);
				SoundEffects.playEnemyDeath();
				player.incXp(enemy.getXpWorth());
//				cancel(); // kills task
				enemyTasks.remove(this);
			} else if (!enemy.isInFight()) {
				// pick a version
				int moveVersion = random.nextInt(1) + 1;

				switch (moveVersion) {
				case 1:
					v1RandomMove();
					break;

				case 2:
					v2AlwaysMove();
					break;

				default:
					v1RandomMove();
					break;
				}

				checkFight(); // checked every move
			} else {

			}
			viewer.repaint();
		}

		// V4 - depth first (need to create node graph)

		// V3 - breadth first (need to create node graph)

		// V2 - pick only a move which is not a wall
		public void v2AlwaysMove() {
			int choice = random.nextInt(4); // N, S, E, W
			boolean moveChoice = false;
			while (moveChoice == false) {
				choice = random.nextInt(4);
				moveChoice = move(choice);
			}
			;

		}

		public boolean move(int choice) {
			switch (choice) {
			case 0: // N
				if (!map.getPosElement(enemy.getTileX(), enemy.getTileY() - 1).equals("w")) {
					enemy.move(0, -1);
					return true;
				} else {
					return false;
				}

			case 1: // S
				if (!map.getPosElement(enemy.getTileX(), enemy.getTileY() + 1).equals("w")) {
					enemy.move(0, 1);
					return true;
				} else {
					return false;
				}

			case 2: // E

				if (!map.getPosElement(enemy.getTileX() - 1, enemy.getTileY()).equals("w")) {
					enemy.move(-1, 0);
					return true;
				} else {
					return false;
				}

			case 3: // W
				if (!map.getPosElement(enemy.getTileX() + 1, enemy.getTileY()).equals("w")) {
					enemy.move(1, 0); // 1? tileDim
					return true;
				} else {
					return false;
				}

			default:
				return false;
			}
		}

		// V1 - random move (may even walk into a wall)
		public void v1RandomMove() {
			int choice = random.nextInt(4); // N, S, E, W
			move(choice);
		}

		// ran every valid keypress
		public void moveCommon() {

		}

		public void checkFight() {
			if (player.getPos().equals(enemy.getPos())) {
				fight();
			}
		}

		// fight
		public void fight() {
			fightCtrl.fight(enemy);
		}
	}

	// @Override
	// public synchronized void start() {
	// // TODO Auto-generated method stub
	// super.start();
	// System.out.println("start");
	// }
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// super.run();
	// System.out.println("run");
	// }

}