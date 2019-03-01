package game;

import aio.AIO;
import ansi.ANSI;

/**
 * Runs a game of hangman
 * 
 * @author Alex Pickering
 */
public class HangmanGame {
	String[] command;
	
	TerminalHangman game;
	
	String word;
	
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
		
		if(command.length != 1)
			if(!initGame())
				return;
		
		playGame();
	}
	
	/**
	 * Plays the game
	 */
	private void playGame() {
		boolean playAgain;
		
		do {
			playAgain = AIO.user.readBooleanYN("Play again? y/n", "");
		} while(playAgain);
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
						return loadDictionary(i);
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
