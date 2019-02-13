package commands;

/**
 * Interface for commands
 * 
 * @author Alex Pickering
 */
public interface Command {
	/**
	 * Runs the command
	 * @param input The input for the command
	 */
	abstract void run(String ... input);
}
