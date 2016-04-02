package ie.gmit.sw.ai.characters;

import ie.gmit.sw.ai.img.ImgCtrl;
import ie.gmit.sw.ai.maze.Maze;

/**
 * Defines an abstract Character super class.
 *  
 * @author Ronan
 */
public abstract class Character {
	protected int tileDim;
	protected int mazeDim;
	private int tileX;
	private int tileY;
	protected Maze mazeGlobal;
	protected ImgCtrl imgCtrl;
	protected int health;
	protected boolean inFight;
	protected boolean alive;

	/**
	 * Character constructor.
	 * 
	 * @param map	the maze
	 * @param imgCtrl	the image controller
	 */
	public Character(Maze maze, ImgCtrl imgCtrl) {
		super();
		this.mazeGlobal = maze;
		this.imgCtrl = imgCtrl;
		inFight = false;
		alive = true;
	}

	/**
	 * Sets the maze.
	 * 
	 * @param mazeGlobal	the global maze
	 */
	public void setMazeGlobal(Maze mazeGlobal) {
		this.mazeGlobal = mazeGlobal;
	}

	/**
	 * Gets the maze.
	 * 
	 * @return	the global maze
	 */
	public Maze getMazeGlobal() {
		return mazeGlobal;
	}

	
	/**
	 * Checks if the character is alive.
	 * 
	 * @return	true if the character is alive
	 */
	public boolean getAlive() {
		return alive;
	}

	/**
	 * Sets the character alive state to true or false.
	 * 
	 * @param alive	true or false
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * Sets the players alive state to true if health is above 0.
	 * 
	 * @return	true if alive, false if not
	 */
	public boolean isAlive() {
		if (health > 0) {
			alive = true;
			return true;
		} else {
			alive = false;
			return false;
		}
	}

	/**
	 * Checks whether the player is in a fight.
	 * 
	 * @return	true if the player is in a fight
	 */
	public boolean isInFight() {
		return inFight;
	}

	/**
	 * Sets the players fight state to true or false.
	 * 
	 * @param inFight	sets the players fight state to true or false
	 */
	public void setInFight(boolean inFight) {
		this.inFight = inFight;
	}

	/**
	 * Gets the current health level.
	 * 
	 * @return	current health level
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Sets a new health level.
	 * 
	 * @param health	new health level
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * Increments the current health level by a specified amount.
	 * 
	 * @param incAmount	amount of health to increase by
	 */
	public void incHealth(int incAmount) {
		health += incAmount;
	}

	/**
	 * Decrements the current health level by a specified amount.
	 * 
	 * @param decAmount	amount of health to decrease by
	 */
	public void decHealth(int decAmount) {
		health -= decAmount;
	}

	/**
	 * Moves the character a specified amount of rows and columns relative to the current position.
	 * 
	 * @param x	move this many x tiles	(column)
	 * @param y	move this many y tiles	(row)
	 */
	public void move(int x, int y) {
		// x negative go left, positive go right
		// it will move tile by tile

		tileX += x;
		tileY += y;
	}

	/**
	 * Sets a new position for the character.
	 * 
	 * @param x	position x (column)
	 * @param y	position y (row)
	 */
	public void setPos(int x, int y) {
		setTileX(x);
		setTileY(y);
	}

	/**
	 * Gets the characters current position, showin by coordinates with a comma seperator.
	 * Examples: "2,4"
	 * 
	 * @return	x, y position seperated by a comma
	 */
	public String getPos() {
		StringBuilder pos = new StringBuilder();
		pos.append(getTileX());
		pos.append(",");
		pos.append(getTileY());
		return pos.toString();
	}

	/**
	 * Gets the x coordinate of the character.
	 * 
	 * @return	x/column coordinate
	 */
	public int getTileX() {
		return tileX;
	}

	/**
	 * Gets the y coordinate of the character.
	 * 
	 * @return	y/row coordinate
	 */
	public int getTileY() {
		return tileY;
	}

	/**
	 * Sets the x coordinate of the character.
	 * 
	 * @param tileX	x/column coordinate
	 */
	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	/**
	 * Sets the y coordinate of the character.
	 * 
	 * @param tileY	y/row coordinate
	 */
	public void setTileY(int tileY) {
		this.tileY = tileY;
	}

}