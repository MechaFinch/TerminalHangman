package game;

import java.util.ArrayList;

import aio.AIO;
import ansi.ANSI;

/**
 * Runs a game of hangman
 * 
 * @author Alex Pickering
 */
public class HangmanGame {
	final int MAX_LINE_LENGTH = 20;
	
	String[] command;
	
	TerminalHangman game;
	
	String word,			//The literal word to find
		   progress = "";	//The characters used so far
	
	int failures = 0;
	
	boolean win = false;
	
	/**
	 * Creates and runs a game of Hangman
	 * 
	 * @param command The command that called this
	 * @param game The instance of the main class
	 */
	public HangmanGame(String[] command, TerminalHangman game) {
		this.command = command;
		this.game = game;
		
		word = "";
		
		if(command.length > 5) {
			invUse();
			return;
		}
		
		playGame();
	}
	
	/**
	 * Plays the game
	 */
	private void playGame() {
		boolean playAgain;
		
		//Primary Loop
		exit:
		do {
			//Init game
			if(command.length != 1) {
				if(!initGame())
					return;
			} else {
				if(!getRandomWord()) {
					System.out.println(ANSI.foreground.RED + "Empty dictionary");
					return;
				}
			}
			
			word = word.toUpperCase();
			progress = "";
			failures = 0;
			
			//Single-game loop
			while(true) {
				//Show info
				showInfo();
				
				//Break if failed
				if(failures >= 6)
					break;
				
				//Get next guess
				char g = getGuess();
				
				//Check for exit character
				if(g == '[') {
					break exit; //brexit;
				}
				
				progress += g;
				
				//Check guess
				if(!word.contains(Character.toString(g))) {
					System.out.println("NOT IN WORD");
					failures++;
				}
				
				//Check for win
				if(win = wordComplete())
					break;
			}
			
			if(win) {
				ANSI.print(ANSI.foreground.GREEN);
				System.out.println("YOU WIN!");
				
				game.player.addWin();
			} else {
				ANSI.print(ANSI.foreground.RED);
				System.out.println("YOU LOSE" + ANSI.foreground.WHITE +
								   "\nThe word was: " + word);
				
				game.player.addLoss();
			}
			
			System.out.println();
			
			game.view(new String[] {"", "player"});
			
			playAgain = AIO.user.readBooleanYN(ANSI.foreground.CYAN + "\nPlay again? y/n" + ANSI.foreground.WHITE, "");
		} while(playAgain);
	}
	
	/**
	 * Checks if the word has been completed
	 * 
	 * @return If the word has been completed
	 */
	private boolean wordComplete() {
		//Check that each character is guessed
		for(char c : word.toCharArray()) {
			if(!progress.contains(Character.toString(c))) return false;
		}
		
		return true;
	}
	
	/**
	 * Gets the player's next guess
	 * 
	 * @return The guess
	 */
	private char getGuess() {
		char guess;
		
		//Get guess character
		while(true) {
			String s = AIO.user.readString(ANSI.foreground.CYAN + "\nEnter Guess", ANSI.foreground.RED + "Please enter a guess");
			
			if(s.length() > 1) {
				if(s.toLowerCase().equals("exit"))
					return '[';
				
				System.out.println(ANSI.foreground.RED + "Please enter a single character");
				continue;
			}
			
			guess = s.toUpperCase().charAt(0);
			
			if(Character.isLetter(guess) && !progress.contains(Character.toString(guess))) {
				break;
			} else if(progress.contains(Character.toString(guess))) {
				System.out.println(ANSI.foreground.RED + "Letter already guessed (Shown in red)");
			} else {
				System.out.println(ANSI.foreground.RED + "Please enter a letter");
			}
		}
		
		return guess;
	}
	
	/**
	 * Displays all information, such as the hangman art, the word progress, and letters used
	 */
	private void showInfo() {
		System.out.println("\n");
		
		//Generate word progress strings
		ArrayList<String> progressStrings = generateProgressStrings();
		String[] drawingStrings = generateHangmanStrings();
		
		//Display drawing and progress
		ANSI.print(ANSI.foreground.WHITE);
		for(int i = 0; i < drawingStrings.length; i++) {
			System.out.print(drawingStrings[i]);
			
			if(i > 2 && (i - 3) < progressStrings.size()) {
				System.out.print("\t" + progressStrings.get(i - 3));
			}
			
			System.out.println();
		}
		
		System.out.println();
		
		//Display available letters
		for(int i = 0; i < 26; i++) {
			char c = (char)(i + 65);
			ANSI.print(progress.contains(Character.toString(c)) ? ANSI.foreground.RED : ANSI.foreground.GREEN);
			System.out.print(c);
		}
		
		System.out.println();
	}
	
	/**
	 * Generates the hangman drawing
	 * 
	 * @return The drawing
	 */
	private String[] generateHangmanStrings() {
		String[] drawing = new String[6];
		
		//Add the part for each failure
		if(failures >= 0) {
			drawing[0] = "_____ ";
			drawing[1] = "|   | ";
			drawing[2] = "|     ";
			drawing[3] = "|     ";
			drawing[4] = "|     ";
			drawing[5] = "|     ";
		}
		
		if(failures > 0) {
			drawing[2] = "|   0 ";
		}
		
		if(failures > 1) {
			switch(failures) {
				case 2: drawing[3] = "|   | "; break;
				case 3: drawing[3] = "|  /| "; break;
				default: drawing[3] = "|  /|\\";
			}
		}
		
		if(failures > 4) {
			drawing[4] = "|  /  ";
		}
		
		if(failures > 5) {
			drawing[4] = "|  / \\";
		}
		
		return drawing;
	}
	
	/**
	 * Generates the underscore/character strings that show progress
	 * 
	 * @return The progress strings
	 */
	private ArrayList<String> generateProgressStrings(){
		int lineLength = 0;
		String currentLine = "";
		
		String[] words = word.split(" ");
		ArrayList<String> progressWords = new ArrayList<>();
		
		for(int i = 0; i < words.length; i++) {
			if((lineLength += words[i].length()) > MAX_LINE_LENGTH && i > 0) {
				lineLength = 0;
				progressWords.add(currentLine);
				currentLine = "";
			}
			
			//Underscore or character
			for(int j = 0; j < words[i].length(); j++) {
				char c = words[i].toUpperCase().charAt(j);
				
				currentLine += (progress.contains(Character.toString(c)) || !Character.isLetter(c)) ? c : "_";
			}
			
			currentLine += " ";
		}
		
		progressWords.add(currentLine);
		
		return progressWords;
	}
	
	/**
	 * Gets a random word from the dictionary
	 * 
	 * @return If the word chosen is empty or null
	 */
	private boolean getRandomWord() {
		word = game.dictionary.getRandomWord();
		return !(word == null || word.equals(""));
	}
	
	/**
	 * Do anything needed before the game starts
	 * 
	 * @return Whether or not it was successful
	 */
	private boolean initGame() {
		boolean word = false,
				dictionary = false;
		
		for(int i = 1; i < command.length; i++) {
			switch(command[i].toLowerCase()) {
				case "-word":
				case "-w":
					if(dictionary) {
						invUse();
						return false;
					} else {
						getWord();
					}
					break;
				
				case "-dictionary":
				case "-dict":
				case "-d":
					if(word) {
						invUse();
						return false;
					} else {
						boolean b = loadDictionary(i);
						getRandomWord();
						return b;
					}
				
				default:
					invUse();
					return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Gets a word from the user
	 */
	private void getWord() {
		ANSI.print(ANSI.foreground.GREEN);
		System.out.print("Please enter the word to play with: ");
		word = String.valueOf(System.console().readPassword());
	}
	
	/**
	 * Loads a dictionary for use
	 * 
	 * @param index The index the '-dictionary' is at
	 * @return Success
	 */
	private boolean loadDictionary(int index) {
		ANSI.print(ANSI.foreground.GREEN);
		System.out.println("Loading Dictionary...");
		
		if(index + 1 == command.length) {
			invUse();
			return false;
		}
		
		if(command[index + 1].toLowerCase().equals("-a")) {
			if(index + 2 == command.length) {
				invUse();
				return false;
			}
			
			return game.loadDictionary(command[index + 2], true);
		} else {
			return game.loadDictionary(command[index + 1], false);
		}
	}
	
	/**
	 * Shows the invalid use message
	 */
	private void invUse() {
		ANSI.print(ANSI.foreground.RED);
		System.out.println("Invalid use.");
		game.showUsage("play");
	}
}
