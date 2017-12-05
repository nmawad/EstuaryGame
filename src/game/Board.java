package game;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * @author ericallen
 * Board Class
 */

public class Board /*implements KeyListener*/{
	public static Player player;
	public static int timer;
	public static ArrayList<NPC> obstacles;
	public boolean isGameOver; 
	public static String STATE = "Menu";
	public static String prevState = "";
	public static List<String> scoresfish = new ArrayList<>(Collections.nCopies(5, ""));
	public static List<String> scorescrab = new ArrayList<>(Collections.nCopies(5, ""));
	public static List<String> scoresbird = new ArrayList<>(Collections.nCopies(5, ""));

	public int scorequantity=0;
	/*
	public static void main(String[] args) {
		Board board = new Board();
		while (!board.getIsGameOver()) {
			Random rand = new Random();
			int n = rand.nextInt(300);
			board.modelInConsole(n);
			board.update();
			try {
				Thread.sleep(18);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Game over!");
	}
	*/
	
	/**
	 * Board Constructor
	 * Attributes: timer, player, obstacles, isGameOver
	 */
	public Board() {
		timer=0;
		player = new Player();
		obstacles = new ArrayList<NPC>();
		isGameOver = false;
	}
	
	/**
	 * modelInConsole
	 * @param n
	 * prints model to Console
	 */
	public void modelInConsole(int n) {
		if(n==0) {
			player.jumpDown();
			System.out.println("Player is moving with a velocity of "+player.getVerticalVelocity());
		}else if(n==1) {
			player.jumpUp();
			System.out.println("Player is moving with a velocity of "+player.getVerticalVelocity());
		}
		
	}
	
	/**
	 * removeNPC 
	 * @param i
	 * removes the obstacles at index
	 */
	public void removeNPC(int i) {
		obstacles.remove(i);
	}
	
	/**
	 * moveNPC pieces
	 * updates and removes
	 */
	public void moveNPCs() {
		ArrayList<NPC> removes = new ArrayList<NPC>();
		for (NPC o : obstacles) {
			o.moveNPC();
			if (o.getXloc() <= 0) {
				removes.add(o);
				continue;
			}
			//TODO WHAT DOES THIS DO? -- Eric Can we change the +50 and -50 to something else?
			if ((o.getYloc() < player.getYloc()+50) &&//HERE
					(o.getYloc() > player.getYloc()-50) &&//HERE
				(player.getXloc() < o.getXloc()) && 
						(o.getXloc() < player.getXloc()+50))  {//HERE 
				if (View.FoodNPCImages[o.image] == View.PowerUpNPCImage && !o.isGarbage) {
					 String[] questionAndAnswer = PowerUp.getQuestionAndAnswer();
					 
					 String answer = JOptionPane.showInputDialog(null, questionAndAnswer[0], "Power Up Question!", JOptionPane.QUESTION_MESSAGE);
					 if (answer.equals(questionAndAnswer[1])) {
						 player.changeScore(3*o.getValue());
					 }else {
						 player.changeScore(-o.getValue());
					 }
					 player.stop();
				}
				System.out.println("Collision value:" + o.getValue() + " (+100 means food, -100 means garbage).");
				player.changeScore(o.getValue());//player hits an NPC and we adjust score
				if (o.getValue() < 0)
					player.takeDamage(o.getValue()/20);//take damage if it was garbage
				removes.add(o);//remove NPC after impact
				System.out.println(player);
			}
		}
		obstacles.removeAll(removes);
	}

	/**
	 * updates the game state on the board
	 */
	public void update() {
		timer++;
		if (timer%3000 ==0) {
			if (STATE == "GameFish") {
				scoresfish.add(Integer.toString(player.score));
				Board.scoresfish.sort(null);
				Collections.reverse(scoresfish);
				prevState = "GameFish";
			}else if(STATE == "GameCrab") {
				scorescrab.add(Integer.toString(player.score));
				Board.scorescrab.sort(null);
				Collections.reverse(scorescrab);
				prevState = "GameCrab";
			}else {
				scoresbird.add(Integer.toString(player.score));
				Board.scoresbird.sort(null);
				Collections.reverse(scoresbird);
				prevState = "GameBird";
			}
			STATE = "Over";
			return;
		}
		player.updateMovement();
		Random rand = new Random();
		boolean isThereSpace = true;
		moveNPCs();
		if (player.health <= 0) {
			if (STATE == "GameFish") {
				scoresfish.add(Integer.toString(player.score));
				Board.scoresfish.sort(null);
				Collections.reverse(scoresfish);
				prevState = "GameFish";
				System.out.println(scoresfish.get(0));
			}else if(STATE == "GameCrab") {
				scorescrab.add(Integer.toString(player.score));
				Board.scorescrab.sort(null);
				Collections.reverse(scorescrab);
				prevState = "GameCrab";
			}else {
				scoresbird.add(Integer.toString(player.score));
				Board.scoresbird.sort(null);
				Collections.reverse(scoresbird);
				prevState = "GameBird";
			}
			STATE = "Over";
			return;
		}
		NPC newNpc = new NPC();
		for (NPC o : obstacles) {
//TODO Can we change the -350 to a variable that changes with size? Main.frameWidth/4. What does this do?
			if ((o.getLane() == newNpc.getLane()) && (o.getXloc() > Main.frameWidth-350)){ 
				isThereSpace = false;
			}
		}
		int x = rand.nextInt(25);
		if (x == 1 && isThereSpace) {
			obstacles.add(newNpc);
		}
	}
	/**
	 * Reset game to it's initial state
	 */
	public static void reset() {
		STATE = "Menu";
		Board.timer = 0;
		player.setScore(0);
		player.health = 10;
		obstacles.clear();
		player.yloc = (int) (2 * (Main.frameHeight / 5));

	}
	
	/**
	 * checks to see if game is over
	 * @return boolean isGameOver
	 */
	public boolean getIsGameOver() {
		return isGameOver;
	}

	/**getObstacts an arraylist of obstacle instances
	 * @return obstacles
	 */
	public ArrayList<NPC> getObstacles() {
		return obstacles;
	}

	/**
	 * getPlayer()
	 * gets an instance of a player
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}
}

