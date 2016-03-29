package ie.gmit.sw.ai.characters;

import java.util.*;
import ie.gmit.sw.ai.*;
import ie.gmit.sw.ai.audio.*;
import ie.gmit.sw.ai.fight.*;
import ie.gmit.sw.ai.img.*;
import ie.gmit.sw.ai.maze.*;
import ie.gmit.sw.ai.traversers.*;
import ie.gmit.sw.ai.traversers.uninformed.*;

public class EnemyBrain extends Thread {
	private ArrayList<Enemy> enemyList;
	private int mazeDim = GameRunner.MAZE_DIM;
	private Maze map;
	private Timer timer;
	private Random random;
	private Player player;
	private ImgCtrl imgCtrl;

	private boolean enemySpawned;
	private FightCtrl fightCtrl;
	private ArrayList<EnemyTask> enemyTasks;

	public EnemyBrain(Maze map, ArrayList<Enemy> enemyList, Player player, ImgCtrl imgCtrl, FightCtrl fightCtrl) {
		this.imgCtrl = imgCtrl;
		this.player = player;
		this.map = map;
		this.enemyList = enemyList;
		this.fightCtrl = fightCtrl;
		random = new Random();
		timer = new Timer();

		enemySpawned = false;
		enemyTasks = new ArrayList<EnemyTask>();
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
			timer.schedule(enemyTask, 1000, 1000);
		}
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

		for (EnemyTask enemyTask : enemyTasks) {
			enemyTask.cancel();
		}
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

	// Single enemy
	public class EnemyTask extends TimerTask {
		// this enemy has a form, is in a place and knows that there is a
		// player.
		private Maze map;
		private Enemy enemy;
		private Player player;
		private Random random;
		private Traversator traversator;

		public EnemyTask(Maze map, Enemy enemy, Player player) {
			this.map = map;
			this.enemy = enemy;
			this.player = player;
			random = new Random();
			int intel = random.nextInt(Enemy.MAX_INTEL);
			enemy.setIntelLvl(intel);

			switch (enemy.getIntelLvl()) {
			case 1:
				traversator = new RandomWalk(map.getMazeArrayClone(), enemy.getTileY(), enemy.getTileX());
				System.out.println("random walk created");
				break;

			default:
				traversator = new RandomWalk(map.getMazeArrayClone(), enemy.getTileY(), enemy.getTileX());
				System.out.println("random walk created");
				break;
			}
		}

		@Override
		public void run() {

			if (!enemy.isAlive()) {
				enemyList.remove(enemy);
				SoundEffects.playEnemyDeath();
				player.incXp(enemy.getXpWorth());
				cancel(); // kills task
			} else if (!enemy.isInFight()) {
				int choice = traversator.findNextMove()[0];
				move(choice);

				checkFight(); // checked every move
			}
		}

		// V4 - depth first (need to create node graph)

		// V3 - breadth first (need to create node graph)

		// V2 - pick only a move which is not a wall
		// public void v2AlwaysMove() {
		// int choice = random.nextInt(4); // N, S, E, W
		// boolean moveChoice = false;
		// while (moveChoice == false) {
		// choice = random.nextInt(4);
		// moveChoice = move(choice);
		// }
		// }

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

			case 4: // Found Goal Node
				System.out.println("EnemyBrain: Found Goal Node");
				return false;

			case 5: // Could not find goal Node
				System.out.println("EnemyBrain: Could not find goal Node");
				return false;

			default:
				return false;
			}
		}

		// // V1 - random move (may even walk into a wall)
		// public void v1RandomMove() {
		// int choice = random.nextInt(4); // N, S, E, W
		// move(choice);
		// }
		//
		// // ran every valid keypress
		// public void moveCommon() {
		//
		// }

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