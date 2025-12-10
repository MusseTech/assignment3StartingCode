package implementations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a word and all its occurrences across multiple files.
 * This class implements Comparable to enable storage in a Binary Search Tree
 * where words are sorted alphabetically.
 * 
 * @author Group 8
 * @version 1.0
 */
public class WordInfo implements Serializable, Comparable<WordInfo> {
	private static final long serialVersionUID = 1L;
	private String word;
	private List<FileEntry> fileEntries;
	
	/**
	 * Constructs a new WordInfo object for the specified word.
     * The word is converted to lowercase for case-insensitive comparison.
     * 
     * @param word the word to track
     * @throws NullPointerException if word is null
	 */
	public WordInfo(String word) {
		if (word == null) {
			throw new NullPointerException("Word cannot be null");
		}
		this.word = word.toLowerCase();
		this.fileEntries = new ArrayList<>();
	}
	
	/**
	 * Adds an occurrence of this word at a specific line in a file.
     * If the file already exists in the records, adds the line number to it.
     * Otherwise, creates a new file entry.
     * 
     * @param fileName the name of the file where the word appears
     * @param lineNumber the line number where the word appears
	 */
	public void addOccurrence(String fileName,int lineNumber) {
		 for (FileEntry entry : fileEntries) {
	            if (entry.getFileName().equals(fileName)) {
	            	entry.addLineNumber(lineNumber);
	            	return;
	            }
		 }
		 
		 FileEntry newEntry = new FileEntry(fileName);
	        newEntry.addLineNumber(lineNumber);
	        fileEntries.add(newEntry);
	}
	
	/**
	 * Compares this WordInfo object with another for ordering in the BST.
     * Comparison is based on the word string in lowercase.
     * 
     * @param other the WordInfo object to compare with
     * @return a negative integer, zero, or a positive integer as this word is less than, equal to, or greater than the specified word
     *         
	 */
	@Override
	public int compareTo(WordInfo other) {
		return this.word.compareTo(other.word);
	}
	
	/**
	 * Returns the word being tracked.
     * 
     * @return the word in lowercase
	 */
	public String getWord() {
		return word;
	}
	
	/**
	 * Returns the list of file entries for this word.
     * 
     * @return the list of file entries
	 */
	public List<FileEntry> getFileEntries() {
		return fileEntries;
	}
	
	/**
	 * Calculates the total frequency of this word across all files.
     * 
     * @return the total number of occurrences across all files
	 */
	public int getTotalFrequency() {
		int total = 0;
        for (FileEntry entry : fileEntries) {
            total += entry.getFrequency();
        }
        return total;
	}
	
	/**
	 * Returns a string representation of this word info.
     * Format: "word (total frequency: X)"
     * 
     * @return a string representation
	 */
	@Override
	public String toString() {
		return word + " (total frequency: " + getTotalFrequency() + ")";
	}

}
