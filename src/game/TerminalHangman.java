package game;

import org.fusesource.jansi.AnsiConsole;

import aio.AIO;
import ansi.ANSI;

import commands.*;

/**
 * Its that main class boiiiiiiiiiiiiiiiiiiiiiii
 * <p> Hangman game! School projects! WOOOO
 * <p> Specifically this file has the command interpreter and lets you do stuff
 * 
 * @author Alex Pickering
 */
public class TerminalHangman {
	public static void main(String[] args) {
		AnsiConsole.systemInstall(); //This must be called for them colors
		
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
			
			while(true) {
				ANSI.print(ANSI.attribute.OFF + ANSI.foreground.GREEN + ANSI.cursor.down(1));
				String in = AIO.user.readString("Enter Command:" + ANSI.foreground.WHITE, "");
				ANSI.print(ANSI.cursor.down(1));
				in = in.toLowerCase();
				if(in.startsWith("exit")) break done;
				
				String[] command = in.split(" ");
				
				switch(command[0]) {
					case "clear":
						ANSI.print(ANSI.erase.SCREEN + ANSI.cursor.HOME);
						break;
					
					default:
						ANSI.print(ANSI.foreground.RED);
						System.out.println("Command invalid.");
				}
			}
		}
		
		ANSI.print(ANSI.attribute.OFF + ANSI.foreground.CYAN);
		System.out.println("GOODBYE!");
	}
}
