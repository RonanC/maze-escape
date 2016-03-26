package ie.gmit.sw.ai.chars;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import ie.gmit.sw.ai.GameRunner;
import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.audio.SoundEffects;

public class EnemyBrain extends Thread {
	private ArrayList<Enemy> enemyList;
	private int mazeDim = GameRunner.MAZE_DIM;
	private Maze map;
	private Timer timer;
	private Random random;
	private Player player;

	public EnemyBrain(Maze map, ArrayList<Enemy> enemyList, Player player) {
		this.player = player;
		this.map = map;
		this.enemyList = enemyList;
		random = new Random();
		timer = new Timer();
	}

	public void spawn() {
		System.out.println("spawning enemies");
		// create enemy tasks
		for (Enemy enemy : enemyList) {
			// System.out.println("new enemy");
			// lvl1 enemy
			randomPos(enemy);

			EnemyTask enemyTask = new EnemyTask(map, enemy, player);
			// schedule the start for every second
			timer.schedule(enemyTask, 0, 1000);
		}
	}

	public void resetAllPos() {
		for (Enemy enemy : enemyList) {
			randomPos(enemy);
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
//			System.out.printf("x: %d, y: %d\t", x, y);
			// avoid walls and player
			if (!map.getPosElement(x, y).equals("w") && !enemy.getPos().equals(player.getPos())) {
//				System.out.println(map.getPosElement(x, y));
				enemy.setPos(x, y);
				notPlaced = false;
				// System.out.println("placing");
			}
		}
	}

	// public EnemyBrain(Mapper map, Enemy enemy, Player player) {
	// timer = new Timer();
	//
	// // lvl1 enemy
	// // placed somewhere random
	// Random random = new Random();
	// int x = random.nextInt(mazeDim);
	// int y = random.nextInt(mazeDim);
	// boolean notPlaced = true;
	// while (notPlaced) {
	// x = random.nextInt(mazeDim);
	// y = random.nextInt(mazeDim);
	// if(map.getPosElement(x, y) != "w"){
	// enemy.setPos(x, y);
	// notPlaced = false;
	// System.out.println("placing");
	// }
	// }
	// // create enemy tasks
	// for (Enemy enemyItem : enemyList) {
	//
	// }
	// EnemyTask enemyTask = new EnemyTask(map, enemy, player);
	// // schedule the start for every second
	// timer.schedule(enemyTask, 0, 1000);
	// }

	// Single enemy
	public class EnemyTask extends TimerTask {
		// this enemy has a form, is in a place and knows that there is a
		// player.
		private Maze map;
		private Enemy enemy;
		private Player player;
		private int moveVersion;
		private Random random;

		public EnemyTask(Maze map, Enemy enemy, Player player) {
			this.map = map;
			this.enemy = enemy;
			this.player = player;
			random = new Random();
			moveVersion = random.nextInt(5);
		}

		@Override
		public void run() {
			// pick a version
			int moveVersion = random.nextInt(2) + 1;

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
		}

		// V4 - depth first (need to create node graph)

		// V3 - breadth first (need to create node graph)

		// V2 - pick only a move which is not a wall
		public void v2AlwaysMove() {
			int choice = random.nextInt(4); // N, S, E, W
			boolean moveChoice = false;
			while(moveChoice == false){
				choice = random.nextInt(4);
				moveChoice = move(choice);
			};

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
			System.out.println("Dual!");
			SoundEffects.playEnemyAttack();
		}
	}

	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		super.start();
		System.out.println("start");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		System.out.println("run");
	}

}
