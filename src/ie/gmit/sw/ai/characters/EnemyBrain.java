package ie.gmit.sw.ai.characters;

import java.util.*;
import ie.gmit.sw.ai.*;
import ie.gmit.sw.ai.audio.*;
import ie.gmit.sw.ai.fight.*;
import ie.gmit.sw.ai.img.*;
import ie.gmit.sw.ai.maze.*;
import ie.gmit.sw.ai.traversers.*;
import ie.gmit.sw.ai.traversers.uninformed.*;

/**
 * This is where the enemies are created, killed and managed.
 * 
 * @author Ronan
 */
public class EnemyBrain extends Thread {
	private ArrayList<Enemy> enemyList;
	private int mazeDim = GameRunner.MAZE_DIM;
	private Maze map;
	private Timer timer;
	private Random random;
	private Player player;
	private ImgCtrl imgCtrl;
	private int depthLimit;

	private boolean enemySpawned;
	private FightCtrl fightCtrl;
	private ArrayList<EnemyTask> enemyTasks;

	private SoundEffects soundEffEnemy;

	public EnemyBrain(Maze map, ArrayList<Enemy> enemyList, Player player, ImgCtrl imgCtrl, FightCtrl fightCtrl) {
		this.imgCtrl = imgCtrl;
		this.player = player;
		this.map = map;
		this.enemyList = enemyList;
		this.fightCtrl = fightCtrl;
		random = new Random();
		// creates a background thread that all tasks will share
		timer = new Timer();

		soundEffEnemy = new SoundEffects();

		enemySpawned = false;
		enemyTasks = new ArrayList<EnemyTask>();

		depthLimit = GameRunner.MAZE_DIM / 2;
	}

	/**
	 * Enemies are given an intelligence level and allocated tasks here.
	 */
	public void spawn() {
		System.out.println("spawning enemies");
		// create enemy tasks
		int enemyCount = 0;
		int intelCount = 0;
		for (Enemy enemy : enemyList) {
			enemyCount++;

			// lvl1 enemy
			randomPos(enemy);
			if (GameRunner.ENEMY_ALGO_NUM != 6) {
				enemy.setIntelLvl(GameRunner.ENEMY_ALGO_NUM);
			} else {
				enemy.setIntelLvl(intelCount % (Enemy.MAX_INTEL + 1)); // intelCount
			}

			System.out.println("\nenemy #" + enemyCount + ", with intel level of: " + enemy.getIntelLvl());

			EnemyTask enemyTask = new EnemyTask(map.getMazeArrayClone(), enemy, player);
			enemyTasks.add(enemyTask);
			// schedule the start for every second
			timer.schedule(enemyTask, enemyTask.sleepDur * 2, enemyTask.sleepDur);
			intelCount++;
		}
	}

	/**
	 * Enemies are created here.
	 * 
	 * @param enemyNum
	 */
	public void createEnemies(int enemyNum) {
		killAllEnemies();
		if (enemyNum < Enemy.MAX_INTEL) {
			enemyNum = Enemy.MAX_INTEL;
		}
		for (int i = 0; i <= enemyNum; i++) {
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

	/**
	 * Resets all enemy positions.
	 */
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

	/**
	 * Sets the current enemies position to a random position in the maze.
	 * 
	 * @param enemy
	 */
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

	/**
	 * Each enemy has a task attached to it. The task moves the enemy and
	 * determines if it's fighting or alive. The traversal algorithm is called
	 * from this class.
	 * 
	 * @author Ronan
	 */
	public class EnemyTask extends TimerTask {
		// this enemy has a form, is in a place and knows that there is a
		// player.
		private Maze map;
		private Enemy enemy;
		private Player player;
		private Traversator traversator;
		private int sleepDur;

		public EnemyTask(Node[][] mazeArray, Enemy enemy, Player player) {
			sleepDur = 1000;
			this.map = new Maze();
			map.setMazeArray(mazeArray);
			this.enemy = enemy;
			this.player = player;
			random = new Random();

			setTraversor(enemy, player);
		}

		public int getSleepDur() {
			return sleepDur;
		}

		public void setSleepDur(int sleepDur) {
			this.sleepDur = sleepDur;
		}

		/**
		 * Sets which traversal algorithm will be used. This is based on what
		 * intelligence level the enemy is. The intelligence levels are spread
		 * out evenly.
		 * 
		 * @param enemy
		 * @param player
		 */
		private void setTraversor(Enemy enemy, Player player) {
			boolean dfs;
			System.out.print("Spider with: ");
			switch (enemy.getIntelLvl()) {
			case 0: // random walk
				traversator = new RandomWalk(map.getMazeArrayClone(), enemy.getTileY(), enemy.getTileX(), player);
				traversator.run();
				System.out.println("Random Walk setup");
				break;

			case 1: // brute force: DFS
				setSleepDur(200);
				dfs = true; // random.nextBoolean();
				traversator = new BruteForceTraversator(map.getMazeArrayClone(), enemy.getTileY(), enemy.getTileX(),
						dfs, player);
				traversator.run();
//				traversator.setGoalNode(player.getTileY(), player.getTileX());
				// chase player original position
				System.out.println("Brute Force DFS traversator setup.");
				break;

			case 2: // brute force: BFS
				setSleepDur(800);
				dfs = false; // random.nextBoolean();
				traversator = new BruteForceTraversator(map.getMazeArrayClone(), enemy.getTileY(), enemy.getTileX(),
						dfs, player);
				traversator.run();
//				traversator.setGoalNode(player.getTileY(), player.getTileX());
				// chase player original position
				System.out.println("Brute Force BFS traversator setup.");
				break;

			case 3: // Recursive DFS
				setSleepDur(500);
				traversator = new RecursiveDFSTraversator(map.getMazeArrayClone(), enemy.getTileY(), enemy.getTileX(),
						player);
				traversator.run();
				System.out.println("Recursive DFS setup");
				break;

			case 4: // Depth Limited DFS
				setSleepDur(500);

				traversator = new DepthLimitedDFSTraversator(map.getMazeArrayClone(), enemy.getTileY(),
						enemy.getTileX(), player, depthLimit);
				traversator.run();
				System.out.println("Depth Limited DFS setup");
				break;

			case 5: // Iterative Deepening DFS
				setSleepDur(500);
				// try {
				traversator = new IDDFSTraversator(map.getMazeArrayClone(), enemy.getTileY(), enemy.getTileX(), player);
				traversator.run();
				System.out.println("Iterative Deepening DFS setup");
				// } catch (Exception e) {
				// traversator = new
				// DepthLimitedDFSTraversator(map.getMazeArrayClone(),
				// enemy.getTileY(),
				// enemy.getTileX(), player, depthLimit);
				// System.out.println("Depth Limited DFS setup");
				// }

				break;

			default:
				System.out.println("default");
				traversator = new RandomWalk(map.getMazeArrayClone(), enemy.getTileY(), enemy.getTileX(), player);
				System.out.println("Random Walk setup (default)");
				break;
			}
		}

		@Override
		public void run() {

			if (!enemy.isAlive()) {
				System.out.println("You killed a level " + enemy.getIntelLvl() + " spider!");
				enemyList.remove(enemy);
				soundEffEnemy.playEnemyDeath();
				player.incXp(enemy.getXpWorth());
				cancel(); // kills task
			} else if (!enemy.isInFight()) {
				if (!traversator.isComplete()) {

					if (enemy.getIntelLvl() == 0) { // random walk
						int choice = traversator.findNextMove()[0];
						move(choice);
					} else if (enemy.getIntelLvl() > 0) {
						int newPos[] = traversator.findNextMove();

						if (newPos[1] == -1) {
							// setCurrentGoal();
							if (newPos[0] == 4) {
								enemy.setPos(traversator.getGoalNode().getCol(), traversator.getGoalNode().getRow());

							} else if (newPos[0] == 5) {
								// System.out.println("Could not find player
								// location.");
							} else {
								// System.out.println("Error, out of moves
								// perhaps?");
								// searching for new enemy location...
							}

							setTraversor(enemy, player);

						} else {
							enemy.setPos(newPos[1], newPos[0]);
						}
					}
				}

				checkFight(); // checked every move
			}
		}

		/**
		 * Moves the player in a certain direction.
		 * 
		 * @param choice
		 * @return
		 */
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
				// System.out.println("EnemyBrain: Found Goal Node");
				setCurrentGoal();
				return false;

			case 5: // Could not find goal Node
				// System.out.println("EnemyBrain: Could not find goal Node");
				setCurrentGoal();
				return false;

			default:
				return false;
			}
		}

		public void setCurrentGoal() {
			traversator.setGoalNode(player.getTileY(), player.getTileX());
			// chase player original position
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
}