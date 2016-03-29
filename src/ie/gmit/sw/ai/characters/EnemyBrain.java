package ie.gmit.sw.ai.characters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import ie.gmit.sw.ai.GameRunner;
import ie.gmit.sw.ai.fight.FightCtrl;
import ie.gmit.sw.ai.img.ImgCtrl;
import ie.gmit.sw.ai.maze.Maze;

public class EnemyBrain { 
	private ArrayList<Enemy> enemyList;
	private int mazeDim = GameRunner.MAZE_DIM;
	private Maze map;
	private Timer timer;
	private Random random;
	private Player player;
	private ImgCtrl imgCtrl;

	private boolean enemySpawned;
	private FightCtrl fightCtrl;

	private LinkedList<EnemyTask> enemyTasks;
	private GeneralTask gt;

	public EnemyBrain(Maze map, ArrayList<Enemy> enemyList, Player player, ImgCtrl imgCtrl, FightCtrl fightCtrl) {
		this.imgCtrl = imgCtrl;
		this.player = player;
		this.map = map;
		this.enemyList = enemyList;
		this.fightCtrl = fightCtrl;
		random = new Random();
		timer = new Timer();
		enemySpawned = false;
		enemyTasks = new LinkedList<EnemyTask>();
		gt = new GeneralTask();
//		timer.schedule(gt, 1000);
		timer.scheduleAtFixedRate(gt, 1000, 1000);
	}

	public void spawn() {
		System.out.println("spawning enemies");
		// create enemy tasks
		for (Enemy enemy : enemyList) {
			// System.out.println("new enemy");
			// lvl1 enemy
			randomPos(enemy);

			EnemyTask enemyTask = new EnemyTask(map, enemy, player, enemyList, enemyTasks, fightCtrl);
			enemyTask.start();
			enemyTasks.add(enemyTask);
			// schedule the start for every second
			// timer.schedule(enemyTask, 0, 500);
		}
		
//		timer.schedule(gt, 1000, 1000);
//		timer.scheduleAtFixedRate(gt, 1000, 1000);
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
						// enemyTask.move(1);
					}
				} catch (Exception e) {
					System.out.println("error: " + e.getMessage());
				}
			}
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