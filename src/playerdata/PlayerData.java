package playerdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Contains a player's data
 * 
 * @author Alex Pickering
 */
public class PlayerData {
	String name;
	
	int wins,
		losses,
		plays;
	
	/**
	 * Creates an empty guest player
	 */
	public PlayerData() {
		this("Guest");
	}
	
	/**
	 * Default constructor
	 * <p>Creates an empty player with a given name
	 * 
	 * @param name The name of the player
	 */
	public PlayerData(String name) {
		this.name = name;
		
		wins = 0;
		losses = 0;
		plays = 0;
	}
	
	/**
	 * Loads a player's data from a file
	 * 
	 * @param f The file to load from
	 * @throws IOException
	 */
	public PlayerData(File f) throws IOException {
		BufferedReader read = new BufferedReader(new FileReader(f));
		String[] sa = new String[4];
		
		for(int i = 0; i < sa.length; i++) {
			sa[i] = read.readLine();
		}
		
		read.close();
		
		name = sa[0];
		wins = Integer.parseInt(sa[1]);
		losses = Integer.parseInt(sa[2]);
		plays = Integer.parseInt(sa[3]);
	}
	
	/**
	 * Loads a player's data from a file path
	 * 
	 * @param filePath The file path to load from
	 * @param load Whether or not to load from a file (differentiates from creating a new player)
	 * @throws IOException
	 */
	public PlayerData(String filePath, boolean load) throws IOException {
		this(new File(filePath));
		
		//Not dealing with factories xd
		if(!load) throw new IllegalArgumentException("Just use the one without the boolean mate");
	}
	
	/**
	 * Gets the name of the player
	 * 
	 * @return The player's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the number of times the player has played
	 * 
	 * @return The player's play count
	 */
	public int getPlays() {
		return plays;
	}
	
	/**
	 * Gets the number of times the player has won
	 * 
	 * @return The player's wins
	 */
	public int getWins() {
		return wins;
	}
	
	/**
	 * Gets the number of times the player has lost
	 * 
	 * @return The player's losses
	 */
	public int getLosses() {
		return losses;
	}
}
