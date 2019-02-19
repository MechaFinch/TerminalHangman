package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Contains a dictionary of words to chose from
 * 
 * @author Alex Pickering
 */
public class Dictionary {
	ArrayList<String> words;
	
	Random rand;
	
	/**
	 * Default constructor
	 * <p>Creates an empty dictionary
	 */
	public Dictionary() {
		rand = new Random();
		words = new ArrayList<>();
	}
	
	/**
	 * Constructor with specified file
	 * <p>Loads the dictionary from the file
	 * 
	 * @param f The file to load from
	 * @throws IOException
	 */
	public Dictionary(File f) throws IOException {
		this();
		
		BufferedReader read = new BufferedReader(new FileReader(f));
		String s = "";
		
		while((s = read.readLine()) != null) {
			words.add(s);
		}
		
		read.close();
	}
	
	/**
	 * Constructor with specified file path
	 * <p>Loads the dictionary from the file
	 * 
	 * @param filePath The file path to load from
	 * @throws IOException
	 */
	public Dictionary(String filePath) throws IOException {
		this(new File(filePath));
	}
	
	/**
	 * Adds a word to the dictionary
	 * 
	 * @param s The word to add
	 */
	public void addWord(String w) {
		words.add(w);
	}
	
	/**
	 * Gets whether or not the dictionary contains a word
	 * 
	 * @param w The word to check
	 * @return Whether or not it's in the dictionary
	 */
	public boolean hasWord(String w) {
		return words.contains(w);
	}
	
	/**
	 * Removes a word from the dictionary
	 * 
	 * @param w The word to remove
	 * @return Whether or not the word was removed
	 */
	public boolean removeWord(String w) {
		return words.remove(w);
	}
	
	/**
	 * Gets the list of words
	 * 
	 * @return The words in the dictionary
	 */
	public ArrayList<String> getWords() {
		return words;
	}
	
	/**
	 * Gets a random word from the dictionary
	 * 
	 * @return A random word
	 */
	public String getRandomWord() {
		return words.get(rand.nextInt(words.size()));
	}
}
