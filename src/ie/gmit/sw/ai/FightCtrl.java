package ie.gmit.sw.ai;

import java.util.Random;
import java.util.Timer;

import ie.gmit.sw.ai.audio.SoundEffects;
import ie.gmit.sw.ai.chars.*;
import ie.gmit.sw.ai.fuzzy.FuzzyScore;

public class FightCtrl {
	private Player player;
	private Enemy enemy;
	private Random random;

	// score
	private int scoreMax;
	private int playerTotalScore, enemyTotalScore;

	// statistics
	private int playerLuckScore, enemyLuckScore;
	private int playerHealthScore, enemyHealthScore;
	private int playerSteps, enemyIntelScore;
	private int playerWeaponScore;
	
	// global
	private boolean fightInProgress;
	private int fightStartTime;
	private int fightDur;
	
	// fuzzy scoring
	private FuzzyScore fuzzyScore;

	// damage
	// private int enemyMaxDamage = 40;
	// private int PlayerMaxDamage = 60;

	public FightCtrl(Player player) {
		this.player = player;
		this.random = new Random();
		this.scoreMax = 20;
		this.fightInProgress = false;
		this.fightDur = 2000;
		
		fuzzyScore = new FuzzyScore();
	}
	
	public void fightOn(){
		setFightInProgress(true);
		player.setInFight(true);
		enemy.setInFight(true);
		
		setFightStartTime(GameCtrl.getTime());
	}
	
	public void fightOff(){
		setFightInProgress(false);
		player.setInFight(false);
		enemy.setInFight(false);
		
		// print scores
//		System.out.println(playerScoreToString());
//		System.out.println(enemyScoreToString());
		
		player.swordDec();
	}


	public void fight(Enemy enemy) {
		this.enemy = enemy;
		resetValues();
		fightOn();
		
		// print raw stats
//		System.out.println(playerRawStatsToString());
//		System.out.println(enemyRawStatsToString());
		
		System.out.println("Dual!");
		SoundEffects.playPlayerAttack();
		SoundEffects.playEnemyAttack();

		// generate scores
//		genScores(); // gets various scores from stats and sums them
		genFuzzyScores(); // passes stats to fuzzy engine which processes each characters score

		// health damage
		player.decHealth(enemyTotalScore);
		enemy.decHealth(playerTotalScore);
		player.isAlive(); // updates alive status
		
		// fight over
//		fightOff();
	}

	public void genFuzzyScores(){
		createScores();
		
		playerTotalScore = fuzzyScore.getCharScore(playerLuckScore, playerHealthScore, playerWeaponScore);
		enemyTotalScore = fuzzyScore.getCharScore(enemyLuckScore, enemyHealthScore, enemyIntelScore);
		
		System.out.println("Player Score (Fuzzy): " + playerTotalScore);
		System.out.println("Enemy Score (Fuzzy): " + enemyTotalScore);
	}

	private void createScores() {
		// massage data into a usable format (all between 0 and 20).
		playerCreateScores();
		enemyCreateScores();
	}
	
	public void genScores() {
		createScores();
		
		playerAdd();
		enemyAdd();
	}

	public void playerCreateScores() {
		int playerLuckInit = random.nextInt(scoreMax) + 1;
		// steps increases luck
		playerLuckScore = (playerLuckInit * (playerSteps / 150)) + playerLuckInit;
		playerHealthScore = player.getHealth() / (Player.MAX_HEALTH / scoreMax);

		// if they have a sword it dramatically increases the their odds
		if (player.getSwordStatus()) {
			playerWeaponScore = random.nextInt(5) + 6;
		}
	}

	public void enemyCreateScores() {
		enemyLuckScore = random.nextInt(scoreMax) + 1;
		double tempHealth = (double)enemy.getHealth() / ((double)Enemy.MAX_HEALTH / (double)scoreMax);
		enemyHealthScore = (int) tempHealth;
		enemyIntelScore = enemy.getIntel() * (Enemy.MAX_INTEL * (scoreMax / 4));
	}

	public void playerAdd() {
		// 3 stats, 20 points each, total of 60
		playerTotalScore += playerLuckScore;
		playerTotalScore += playerHealthScore;
		playerTotalScore += playerWeaponScore;
	}

	public void enemyAdd() {
		enemyTotalScore += enemyLuckScore;
		enemyTotalScore += enemyHealthScore;
		enemyTotalScore += enemyIntelScore;
	}
	
	private void resetValues() {
		// reset values
		playerTotalScore = 0;
		enemyTotalScore = 0;
		playerLuckScore = enemyLuckScore = playerHealthScore = enemyHealthScore = playerSteps = enemyIntelScore = playerWeaponScore = 0;
	}
	
	public String playerRawStatsToString() {
		StringBuilder sb = new StringBuilder();
		sb.append("===Player Raw Stats===\n");
		sb.append("Steps:\t" + player.getStepCount() + "\n");
		sb.append("Health:\t" + player.getHealth() + "\n");
		sb.append("Weapon:\t" + player.getSwordStatus() + "\n");
		sb.append("\n");

		return sb.toString();
	}

	public String playerScoreToString() {
		StringBuilder sb = new StringBuilder();
		sb.append("===Player Scores===\n");
		sb.append("Luck:\t" + playerLuckScore + "\n");
		sb.append("Health:\t" + playerHealthScore + "\n");
		sb.append("Weapon:\t" + playerWeaponScore + "\n");
		sb.append("*Total:\t" + playerTotalScore + "\n");
		sb.append("\n");

		return sb.toString();
	}

	public String enemyScoreToString() {
		StringBuilder sb = new StringBuilder();
		sb.append("===Enemy Scores===\n");
		sb.append("Luck:\t" + enemyLuckScore + "\n");
		sb.append("Health:\t" + enemyHealthScore + "\n");
		sb.append("Intel:\t" + enemyIntelScore + "\n");
		sb.append("*Total:\t" + enemyTotalScore + "\n");
		sb.append("\n");

		return sb.toString();
	}
	
	public String enemyRawStatsToString() {
		StringBuilder sb = new StringBuilder();
		sb.append("===Enemy Raw Stats===\n");
		sb.append("Intel:\t" + enemy.getIntel() + "\n");
		sb.append("Health:\t" + enemy.getHealth() + "\n");
		sb.append("\n");

		return sb.toString();
	}
	
	public int getFightStartTime() {
		return fightStartTime;
	}



	public int getFightDur() {
		return fightDur;
	}



	public void setFightStartTime(int fightStartTime) {
		this.fightStartTime = fightStartTime;
	}



	public void setFightDur(int fightDur) {
		this.fightDur = fightDur;
	}



	public boolean isFightInProgress() {
		return fightInProgress;
	}



	public void setFightInProgress(boolean fightInProgress) {
		this.fightInProgress = fightInProgress;
	}
}
