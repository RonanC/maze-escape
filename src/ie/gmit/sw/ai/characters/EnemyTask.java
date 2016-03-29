package ie.gmit.sw.ai.characters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import ie.gmit.sw.ai.audio.SoundEffects;
import ie.gmit.sw.ai.fight.FightCtrl;
import ie.gmit.sw.ai.maze.Maze;

// Single enemy
public class EnemyTask extends Thread {
	// this enemy has a form, is in a place and knows that there is a
	// player.
	private Maze map;
	private Enemy enemy;
	private Player player;
	private Random random;
	private ArrayList<Enemy> enemyList;
	private LinkedList<EnemyTask> enemyTasks;
	private FightCtrl fightCtrl;

	public EnemyTask(Maze map, Enemy enemy, Player player, ArrayList<Enemy> enemyList, LinkedList<EnemyTask> enemyTasks, FightCtrl fightCtrl) {
		this.map = map;
		this.enemy = enemy;
		this.player = player;
		random = new Random();
		this.enemyList = enemyList;
		this.enemyTasks = enemyTasks;
		this.fightCtrl = fightCtrl;
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
			SoundEffects.playEnemyDeath();
			player.incXp(enemy.getXpWorth());
			// cancel(); // kills task
			enemyTasks.remove(this);
		} else if (!enemy.isInFight()) {
			// pick a version
			int moveVersion = 1;// random.nextInt(1) + 1;

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