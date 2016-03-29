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
		int enemyCount = 0;
		int intelCount = 0;
		for (Enemy enemy : enemyList) {
			enemyCount++;
			System.out.println("enemy #" + enemyCount + ", with intel level of: " + intelCount);
			// lvl1 enemy
			randomPos(enemy);
			enemy.setIntelLvl(3); // intelCount // TODO
			EnemyTask enemyTask = new EnemyTask(map.getMazeArrayClone(), enemy, player);
			enemyTasks.add(enemyTask);
			// schedule the start for every second
			timer.schedule(enemyTask, enemyTask.sleepDur, enemyTask.sleepDur);
			intelCount++;
		}
	}

	public void createEnemies(int enemyNum) {
		killAllEnemies();

		for (int i = 0; i < 5; i++) {	// TODO
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
		private int sleepDur;

		public EnemyTask(Node[][] mazeArray, Enemy enemy, Player player) {
			sleepDur = 1000;
			this.map = new Maze();
			map.setMazeArray(mazeArray);
			this.enemy = enemy;
			this.player = player;
			random = new Random();
			// int intel = 2;//random.nextInt(Enemy.MAX_INTEL);
			// enemy.setIntelLvl(intel);

			setTraversor(enemy, player);
		}

		
		public int getSleepDur() {
			return sleepDur;
		}


		public void setSleepDur(int sleepDur) {
			this.sleepDur = sleepDur;
		}


		private void setTraversor(Enemy enemy, Player player) {
			boolean dfs;

			switch (enemy.getIntelLvl()) {
			case 0:	// random walk
				traversator = new RandomWalk(map.getMazeArrayClone(), enemy.getTileY(), enemy.getTileX(), player);
				System.out.println("random walk created");
				break;

			case 1: // brute force: DFS
				setSleepDur(200);
				dfs = true; // random.nextBoolean();
				traversator = new BruteForceTraversator(map.getMazeArrayClone(), enemy.getTileY(), enemy.getTileX(),
						dfs, player);
				traversator.setGoalNode(player.getTileY(), player.getTileX());// chase
																				// player
																				// original
																				// position
				System.out.print("brute force ");
				if (dfs) {
					System.out.print("DFS");
				} else {
					System.out.print("BFS");
				}
				System.out.println(" traversator created.");
				break;

			case 2: // brute force: BFS
				setSleepDur(800);
				dfs = false; // random.nextBoolean();
				traversator = new BruteForceTraversator(map.getMazeArrayClone(), enemy.getTileY(), enemy.getTileX(),
						dfs, player);
				traversator.setGoalNode(player.getTileY(), player.getTileX());// chase
																				// player
																				// original
																				// position
				System.out.print("brute force ");
				if (dfs) {
					System.out.print("DFS");
				} else {
					System.out.print("BFS");
				}
				System.out.println(" traversator created.");
				break;
				
			case 3:	// Recursive DFS 	1.13
				setSleepDur(500);
				traversator = new RecursiveDFSTraversator(map.getMazeArrayClone(), enemy.getTileY(), enemy.getTileX(), player);
				System.out.println("recursive DFS created");
				break;

			default:
				System.out.println("default");
				traversator = new RandomWalk(map.getMazeArrayClone(), enemy.getTileY(), enemy.getTileX(), player);
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
				if (!traversator.isComplete()) {

					if (enemy.getIntelLvl() == 0) { // random walk
						int choice = traversator.findNextMove()[0];
						move(choice);
					} else if (enemy.getIntelLvl() > 0) {
						int newPos[] = traversator.findNextMove();
						// try {
						// Thread.sleep(1000);
						// } catch (InterruptedException e) {
						// e.printStackTrace();
						// }

						if (newPos[1] == -1) {
							// setCurrentGoal();
							if (newPos[0] == 4) {
								System.out.println("Found player location.");
								enemy.setPos(traversator.getGoalNode().getCol(), traversator.getGoalNode().getRow());
//								System.out.println("row: " + traversator.getGoalNode().getRow());
//								System.out.println("col: " + traversator.getGoalNode().getCol());
							} else if (newPos[0] == 5) {
								System.out.println("Could not find player location.");
							} else {
								System.out.println("Error, out of moves perhaps?"); 
								// searching for new enemy location...
							}
							System.out.println(
									"player location, row: " + player.getTileY() + ", col: " + player.getTileX());
							System.out.println("enemy goal, row:" + traversator.getGoalNode().getRow() + ", col: "
									+ traversator.getGoalNode().getCol() + "\n");
//							traversator.resetAndSetGoal();
							setTraversor(enemy, player);
							
						} else {
							enemy.setPos(newPos[1], newPos[0]);
						}
					}
				}

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
				setCurrentGoal();
				return false;

			case 5: // Could not find goal Node
				System.out.println("EnemyBrain: Could not find goal Node");
				setCurrentGoal();
				return false;

			default:
				return false;
			}
		}

		public void setCurrentGoal() {
			traversator.setGoalNode(player.getTileY(), player.getTileX());// chase
																			// player
																			// original
																			// position
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
	// super.start();
	// System.out.println("start");
	// }
	//
	// @Override
	// public void run() {
	// super.run();
	// System.out.println("run");
	// }

}