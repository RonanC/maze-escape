package ie.gmit.sw.ai.characters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import ie.gmit.sw.ai.audio.SoundEffects;
import ie.gmit.sw.ai.fight.FightCtrl;
import ie.gmit.sw.ai.maze.Maze;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.uninformed.RandomWalk;

// Single enemy
public class EnemyTask extends Thread {
	// this enemy has a form, is in a place and knows that there is a
	// player.
	private Maze globalMaze;
	private Enemy enemy;
	private Player player;
	private Random random;
	private ArrayList<Enemy> enemyList;
	private LinkedList<EnemyTask> enemyTasks;
	private FightCtrl fightCtrl;
	
	// serach stuff
	private Traversator traversator;
	private Maze mazeClone;
	private Node[][] mazeArrayClone;
	private Node currentNode;


	public Enemy getEnemy() {
		return enemy;
	}

	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}

	public EnemyTask(Maze globalMaze, Enemy enemy, Player player, ArrayList<Enemy> enemyList, LinkedList<EnemyTask> enemyTasks,
			FightCtrl fightCtrl) {
		this.globalMaze = globalMaze;
		this.enemy = enemy;
		this.player = player;
		random = new Random();
		this.enemyList = enemyList;
		this.enemyTasks = enemyTasks;
		this.fightCtrl = fightCtrl;
		this.mazeArrayClone = globalMaze.getMazeArrayClone();

		// pick a version
		// random.nextInt(1);
		enemy.setIntelLvl(0);

		switch (enemy.getIntelLvl()) {
		case 0:
			randomWalk();
			break;

		default:
			randomWalk();
			break;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		runTask();
	}

	// @Override
	public void runTask() {
		if (!enemy.isAlive()) {
			enemyList.remove(enemy);
//			enemyTasks.remove(this);
			SoundEffects.playEnemyDeath();
			player.incXp(enemy.getXpWorth());
//			 cancel(); // kills task
			
		} else if (!enemy.isInFight()) {
			int choice = traversator.findNextMove();
			move(choice);
			checkFight(); // checked every move
		} else {

		}
	}

	private void randomWalk() {
		traversator = new RandomWalk();
		traversator.init(mazeArrayClone, getCurrentNode());
		System.out.println("random walk created");
	}
	
	private Node getCurrentNode(){
		return mazeArrayClone[enemy.getTileY()][enemy.getTileX()];
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
	}

	public boolean move(int choice) {
		switch (choice) {
		case 0: // N
			if (!globalMaze.getPosElement(enemy.getTileX(), enemy.getTileY() - 1).equals("w")) {
				enemy.move(0, -1);
				return true;
			} else {
				return false;
			}

		case 1: // S
			if (!globalMaze.getPosElement(enemy.getTileX(), enemy.getTileY() + 1).equals("w")) {
				enemy.move(0, 1);
				return true;
			} else {
				return false;
			}

		case 2: // E

			if (!globalMaze.getPosElement(enemy.getTileX() - 1, enemy.getTileY()).equals("w")) {
				enemy.move(-1, 0);
				return true;
			} else {
				return false;
			}

		case 3: // W
			if (!globalMaze.getPosElement(enemy.getTileX() + 1, enemy.getTileY()).equals("w")) {
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