package game;

import java.io.FileNotFoundException;
import java.io.IOException;

import aio.AIO;
import ansi.ANSI;
import playerdata.PlayerData;

/**
 * Its that main class boiiiiiiiiiiiiiiiiiiiiiii
 * <p> Hangman game! School projects! WOOOO
 * <p> Specifically this file has the command interpreter and lets you do stuff
 * 
 * @author Alex Pickering
 */
public class TerminalHangman {
	public Dictionary dictionary = new Dictionary();
	public PlayerData player = new PlayerData();
	
	public static void main(String[] args) {
		new TerminalHangman().run();
	}
	
	/**
	 * It's main, but better!!!
	 */
	void run() {
		//Runs the main menu
		done:
		while(true) {
			//Starts the main menu
			ANSI.print(ANSI.attribute.OFF + ANSI.erase.SCREEN + ANSI.cursor.HOME + ANSI.foreground.CYAN);
			System.out.println("Welcome to " + ANSI.foreground.RED + "HANGMAN" + ANSI.foreground.CYAN + "!");
			
			//Get commands
			while(true) {
				ANSI.print(ANSI.attribute.OFF + ANSI.foreground.GREEN + "\n");
				String in = AIO.user.readString("Enter Command:" + ANSI.foreground.WHITE, "");
				ANSI.print(ANSI.cursor.down(1));
				in = in.toLowerCase();
				if(in.startsWith("exit")) break done;
				
				String[] command = in.split(" ");
				
				//Interpret command
				switch(command[0]) {
					case "clear":
					case "cls":
						ANSI.print(ANSI.cursor.HOME + ANSI.erase.SCREEN);
						break;
					
					case "load":
						load(command);
						break;
					
					case "view":
						view(command);
						break;
					
					case "add":
						addWord(command);
						break;
					
					case "remove":
					case "rm":
						removeWord(command);
						break;
					
					case "list":
					case "ls":
						view(new String[]{"", "d"});
						break;
					
					default:
						ANSI.print(ANSI.foreground.RED);
						System.out.println("Command invalid.");
				}
			}
		}
		
		ANSI.print(ANSI.attribute.OFF + ANSI.foreground.CYAN);
		System.out.println("goodbye");
	}
	
	/**
	 * Removes a word from the dictionary
	 * 
	 * @param command Command instance
	 */
	void removeWord(String[] command) {
		if(command.length < 2) {
			ANSI.print(ANSI.foreground.RED);
			System.out.println("Invalid use.");
			ANSI.print(ANSI.foreground.WHITE);
			System.out.println("remove <word or phrase>");
		} else {
			String s = assembleWord(command, 1);
			
			if(dictionary.hasWord(s)) {
				dictionary.removeWord(s);
			} else {
				ANSI.print(ANSI.foreground.RED);
				System.out.println("Word is not in dictionary");
			}
		}
	}
	
	/**
	 * Adds a word to the dictionary
	 * 
	 * @param command Command instance
	 */
	void addWord(String[] command) {
		if(command.length < 2) {
			ANSI.print(ANSI.foreground.RED);
			System.out.println("Invalid use.");
			ANSI.print(ANSI.foreground.WHITE);
			System.out.println("add <word or phrase>");
		} else {
			String s = assembleWord(command, 1);
			
			if(dictionary.hasWord(s)) {
				ANSI.print(ANSI.foreground.RED);
				System.out.println("Word is already in the dictionary");
				return;
			}
			
			dictionary.addWord(s);
		}
	}
	
	/**
	 * Assembles the word/phrase from the command
	 * 
	 * @param command Command instance
	 * @return The assembled string
	 */
	String assembleWord(String[] command, int start) {
		String w = "";
		
		for(; start < command.length; start++) {
			w += command[start];
		}
		
		return w;
	}
	
	/**
	 * Shows things such as player data or dictionaries
	 */
	void view(String[] command) {
		switch((command.length == 2) ? command[1] : "") {
			case "player":
			case "p":
				ANSI.print(ANSI.foreground.CYAN);
				System.out.println("Player Stats:");
				System.out.println(ANSI.foreground.WHITE + "Name: " + player.getName() +
								   "\nWins: " + ANSI.foreground.GREEN + player.getWins() +
								   ANSI.foreground.WHITE + "\nLosses: " + ANSI.foreground.RED + player.getLosses() +
								   ANSI.foreground.WHITE + "\nPlays: " + player.getPlays());
				break;
			
			case "dictionary":
			case "d":
				ANSI.print(ANSI.foreground.CYAN);
				System.out.println("Dictionary:");
				ANSI.print(ANSI.foreground.WHITE);
				dictionary.getWords().forEach(s -> System.out.println(s));
				break;
			
			default:
				ANSI.print(ANSI.foreground.RED);
				System.out.println("Invalid use.");
				ANSI.print(ANSI.foreground.CYAN);
				System.out.println("Usage:");
				ANSI.print(ANSI.foreground.WHITE);
				System.out.println("view {player | dictionary}" +
								   "\n\tplayer\t\tView player stats" +
								   "\n\tdictioanry\tView dictionary");
		}
	}
	
	/**
	 * Loads things, from playerdata to dictionaries
	 * 
	 * @param command The command this was run with
	 */
	void load(String[] command) {
		switch((command.length == 3) ? command[1] : "") {
			case "player":
			case "p":
				loadPlayerData(command[2]);
				break;
			
			case "dictionary":
			case "d":
				loadDictionary(command[2]);
				break;
			
			default:
				ANSI.print(ANSI.foreground.RED);
				System.out.println("Invalid use.");
				ANSI.print(ANSI.foreground.CYAN);
				System.out.println("Usage:");
				ANSI.print(ANSI.foreground.WHITE);
				System.out.println("load {player | dictionary} <directory>" +
								   "\n\tplayer\t\tLoad a player's data" +
								   "\n\tdictionary\tLoad a dictionary of words");
		}
	}
	
	/**
	 * Loads player data from the specified file
	 * 
	 * @param filePath The file to load from
	 */
	void loadPlayerData(String filePath) {
		try {
			player = new PlayerData(filePath, true);
		} catch (IOException e) {
			if(e instanceof FileNotFoundException) {
				ANSI.print(ANSI.foreground.RED);
				System.out.println("File not found.");
			} else {
				e.printStackTrace();
			}
			
			return;
		}
		
		ANSI.print(ANSI.foreground.CYAN);
		System.out.println("Done.");
	}
	
	/**
	 * Loads a dictionary of words from the specified file
	 * 
	 * @param filePath The file to load from
	 */
	void loadDictionary(String filePath) {
		try {
			dictionary = new Dictionary(filePath);
		} catch(IOException e) {
			if(e instanceof FileNotFoundException) {
				ANSI.print(ANSI.foreground.RED);
				System.out.println("File not found.");
			} else {
				e.printStackTrace();
			}
			
			return;
		}
		
		ANSI.print(ANSI.foreground.CYAN);
		System.out.println("Done.");
	}
}
