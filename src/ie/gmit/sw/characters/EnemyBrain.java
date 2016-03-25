package ie.gmit.sw.characters;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import ie.gmit.sw.ai.GameRunner;
import ie.gmit.sw.ai.Mapper;
import ie.gmit.sw.ai.audio.SoundEffects;

public class EnemyBrain extends Thread {
	private ArrayList<Enemy> enemyList;
	private int mazeDim = GameRunner.mazeDim;
	private Mapper map;
	private Timer timer;
	private Random random;
	
	public EnemyBrain(Mapper map, ArrayList<Enemy> enemyList, Player player) {
		this.map = map;
		this.enemyList = enemyList;
		random = new Random();
		timer = new Timer();
		
		// create enemy tasks
		for (Enemy enemy : enemyList) {
			System.out.println("new enemy");
			// lvl1 enemy
			randomPos(enemy);
			
			EnemyTask enemyTask = new EnemyTask(map, enemy, player);
			// schedule the start for every second
			timer.schedule(enemyTask, 0, 1000);
		}
		

	}
	
	public void resetAllPos(){
		for (Enemy enemy : enemyList) {
			randomPos(enemy);
		}
	}

	public void randomPos(Enemy enemy) {
		// placed somewhere random
		int x = random.nextInt(mazeDim-2) + 1;// don't want to choose edges
		int y = random.nextInt(mazeDim-2) + 1;
		boolean notPlaced = true;
		while (notPlaced) {
			x = random.nextInt(mazeDim);
			y = random.nextInt(mazeDim);
			System.out.printf("%d, %d\n", x, y);
			if(!map.getPosElement(x, y).equals("w")){
				System.out.println(map.getPosElement(x, y));
				enemy.setPos(x, y);
				notPlaced = false;
				System.out.println("placing");
			}
		}
	}
	
//	public EnemyBrain(Mapper map, Enemy enemy, Player player) {
//		timer = new Timer();
//		
//		// lvl1 enemy
//		// placed somewhere random
//		Random random = new Random();
//		int x = random.nextInt(mazeDim);
//		int y = random.nextInt(mazeDim);
//		boolean notPlaced = true;
//		while (notPlaced) {
//			x = random.nextInt(mazeDim);
//			y = random.nextInt(mazeDim);
//			if(map.getPosElement(x, y) != "w"){
//				enemy.setPos(x, y);
//				notPlaced = false;
//				System.out.println("placing");
//			}
//		}
//		// create enemy tasks
//		for (Enemy enemyItem : enemyList) {
//			
//		}
//		EnemyTask enemyTask = new EnemyTask(map, enemy, player);
//		// schedule the start for every second
//		timer.schedule(enemyTask, 0, 1000);
//	}
	
	// Single enemy
	public class EnemyTask extends TimerTask{
		// this enemy has a form, is in a place and knows that there is a player.
		Mapper map;
		Enemy enemy;
		Player player;
		
		public EnemyTask(Mapper map, Enemy enemy, Player player) {
			this.map = map;
			this.enemy = enemy;
			this.player = player;
		}
		
		@Override
		public void run() {
			move();
			checkFight();	// checked every move
		}
		
		// random move (may even walk into a wall)
		public void move(){
			Random random = new Random();
			int choice = random.nextInt(4); //N, S, E, W
			
			switch (choice) {
			case 0: // N
				if (!map.getPosElement(enemy.getTileX(), enemy.getTileY() - 1).equals("w")) {
					enemy.move(0, -1);
					moveCommon();
				}
				break;

			case 1: // S
				if (!map.getPosElement(enemy.getTileX(), enemy.getTileY() + 1).equals("w")) {
					enemy.move(0, 1);
					moveCommon();
				}
				break;
				
			case 2:	// E

				if (!map.getPosElement(enemy.getTileX() - 1, enemy.getTileY()).equals("w")) {
					enemy.move(-1, 0);
					moveCommon();
				}
				break;
				
			case 3:	// W
				if (!map.getPosElement(enemy.getTileX() + 1, enemy.getTileY()).equals("w")) {
					enemy.move(1, 0); // 1? tileDim
					moveCommon();
				}
				break;
			default:
				break;
			}
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
