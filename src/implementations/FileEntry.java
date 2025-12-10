package implementations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a file entry containing the filename and line numbers
 * where a specific word appears. This class tracks occurrences of a word
 * within a single file.
 * 
 * @author Group 8
 * @version 1.0
 * @see WordInfo
 * @see WordTracker
 */
public class FileEntry implements Serializable {
	private static final long serialVersionUID = 1L;
    private String fileName;
    private List<Integer> lineNumbers;
    
    /**
     * Constructs a new FileEntry for the specified filename.
     * Initializes an empty list of line numbers.
     * 
     * @param fileName the name of the file where the word appears
     * @throws NullPointerException if fileName is null
     */
    public FileEntry(String fileName) {
    	if (fileName == null) {
    	
    }
        this.fileName = fileName;
        this.lineNumbers = new ArrayList<>();
    }
    
    /**
     * Adds a line number to this file entry if it doesn't already exist.
     * Prevents duplicate line numbers for the same word occurrence.
     * 
     * @param lineNum the line number where the word appears
     * @return true if the line number was added, false if it already existed
     */
    public boolean addLineNumber(int lineNum) {
    	if (!lineNumbers.contains(lineNum)) {
    		lineNumbers.add(lineNum);
    		return true;
    	}
    	return false;
    }
    
    /**
     * Returns the filename associated with this file entry.
     * 
     * @return the filename where the word appears
     */
    public String getFileName() {
    	return fileName;
    }
    
    /**
     * Returns an unmodifiable view of the line numbers list.
     * The returned list cannot be modified directly; use addLineNumber()
     * to add new line numbers.
     * 
     * @return a read-only list of line numbers where the word appears in this file
     */
    public List<Integer> getLineNumbers() {
    	return Collections.unmodifiableList(lineNumbers);
    }
    
    /**
     * Returns the frequency (number of occurrences) of the word in this file.
     * This is calculated as the size of the line numbers list.
     * 
     * @return the number of times the word appears in this file
     */
    public int getFrequency() {
    	return lineNumbers.size();
    }
    
    /**
     * Returns a string representation of this file entry.
     * 
     * @return a string representation of the file entry
     */
    @Override
    public String toString() {
    	return fileName + " (frequency: " + getFrequency() + ") lines: " + lineNumbers;
    }
}
