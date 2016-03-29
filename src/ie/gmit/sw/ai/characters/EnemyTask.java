package ie.gmit.sw.ai.characters;

import java.util.Random;

import ie.gmit.sw.ai.maze.Maze;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.traversers.uninformed.RandomWalk;

// Single enemy
public class EnemyTask {
	// this enemy has a form, is in a place and knows that there is a
	// player.
	private Maze map;
	private Enemy enemy;
	private Player player;
	private Random random;
	private int traversatorNum;
	private Traversator traversator;
	
	private Maze mazeClone;
	private Node[][] nodesClone;
	private Node currentNode;
	

	public EnemyTask(Maze map, Enemy enemy, Player player) {
		this.map = map;
		this.enemy = enemy;
		this.player = player;
		random = new Random();

		// pick a version
		traversatorNum = 0; //random.nextInt(1);
		enemy.setIntelLvl(traversatorNum);

		// set algorithm
		switch (traversatorNum) {
		case 0:
			customRandomWalk();
			break;
//
//		case 1:
//			traversator = new BruteForceTraversator(true); // DFS
//			break;
//
//		case 2:
//			traversator = new BruteForceTraversator(false); // BFS
//			break;

		default:
			customRandomWalk();
			break;
		}
	}

	private Node getCurrentNode(){
		return nodesClone[enemy.getTileY()][enemy.getTileX()];
	}
	
	private void customRandomWalk() {
		traversator = new RandomWalk();
		traversator.init(map.getMazeClone(), getCurrentNode());
		System.out.println("random walk created");
	}
}