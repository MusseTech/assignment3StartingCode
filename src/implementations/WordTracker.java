package implementations;

import utilities.Iterator;
import java.io.*;
import java.util.Scanner;


public class WordTracker {
	
	private BSTree<WordInfo> wordTree;
	private static final String REPOSITORY_FILE = "repository.ser";
	public WordTracker() {
	}
	
	/**
	 * Loads the word tree from repository.ser if it exists.
     * Uses Java ObjectInputStream to deserialize the BST.
     * 
     * @return loaded BSTree, or new empty tree if file doesn't exist
	 */
	@SuppressWarnings("unhecked")
	private BSTree<WordInfo> loadRepository() {
		File repoFile = new File(REPOSITORY_FILE);
		
		// / Check if we have saved data from previous runs
        if (repoFile.exists()) {
        	try (ObjectInputStream ois = new ObjectInputStream(
        			new FileInputStream(repoFile))) {
                // Read the entire BST object from file
                return (BSTree<WordInfo>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Warning: Could not load repository, starting fresh.");
                System.err.println("Error: " + e.getMessage());
            }
        }
        // No existing repository - start with empty tree
        return new BSTree<>();
	}
	
	/**
     * Saves the current word tree to repository.ser.
     * Uses Java ObjectOutputStream to serialize the BST.
     * Called after processing each file (as per assignment requirements).
     */
	private void saveRepository() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
             new FileOutputStream(REPOSITORY_FILE))) {
            oos.writeObject(wordTree);  // Write entire BST to file
        } catch (IOException e) {
            System.err.println("Error saving repository: " + e.getMessage());
        }
    }
	
	/**
	 * Processes a text file: reads it line by line, extracts words,
     * and adds them to the BST with their line numbers.
     * 
     * @param fileName path to the text file to process
	 */
	public void processFile(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            int lineNumber = 1;  // Start counting from line 1
            
            // Read file line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                processLine(fileName, line, lineNumber);  // Process this line
                lineNumber++;  // Move to next line
            }
            
            // Save after processing (as per assignment requirements)
            saveRepository();
            
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found - " + fileName);
        }
    }
    
    /**
     * Processes a single line: extracts words, cleans them,
     * and adds each word to the BST.
     * 
     * @param fileName file containing this line
     * @param line the text line to process
     * @param lineNumber current line number in the file
     */
    private void processLine(String fileName, String line, int lineNumber) {
        // Clean the line: remove punctuation, lowercase, split into words
        String[] words = line.replaceAll("[^a-zA-Z ]", " ")  // Replace non-letters with space
        		.toLowerCase()                   // Convert to lowercase
        		.split("\\s+");                  // Split on whitespace
        
        // Process each word in the line
        for (String word : words) {
            if (!word.isEmpty() && word.length() > 0) {  // Skip empty strings
                addWordToTree(word, fileName, lineNumber);
            }
        }
    }
    
    /**
     * Adds a single word occurrence to the BST.
     * If word doesn't exist, creates new WordInfo and adds to BST.
     * If word exists, adds occurrence to existing WordInfo.
     * 
     * @param word the word to add
     * @param fileName file where word was found
     * @param lineNumber line number where word was found
     */
    private void addWordToTree(String word, String fileName, int lineNumber) {
        // Create a temporary WordInfo to search for
        WordInfo searchWord = new WordInfo(word);
        
        // Use BST's search method to find if word already exists
        BSTreeNode<WordInfo> node = wordTree.search(searchWord);
        
        if (node == null) {
            // NEW WORD: Create WordInfo and add to BST
            WordInfo newWord = new WordInfo(word);
            newWord.addOccurrence(fileName, lineNumber);
            wordTree.add(newWord);  // BST insertion maintains alphabetical order
        } else {
            // EXISTING WORD: Add occurrence to existing WordInfo
            node.getElement().addOccurrence(fileName, lineNumber);
        }
    }
	
    /**
     * Prints words with file names only (-pf option).
     * Format: word: file1, file2, file3
     */
    public void printFilesWithLines() {
        Iterator<WordInfo> iterator = wordTree.inorderIterator();
        
        while (iterator.hasNext()) {
            WordInfo wordInfo = iterator.next();
            System.out.println(wordInfo.getWord() + ":");
            
            for (FileEntry entry : wordInfo.getFileEntries()) {
                System.out.print("  " + entry.getFileName() + " - lines: ");
                
                // Print all line numbers for this file
                boolean firstLine = true;
                for (Integer line : entry.getLineNumbers()) {
                    if (!firstLine) {
                        System.out.print(", ");
                    }
                    System.out.print(line);
                    firstLine = false;
                }
                System.out.println();  // New line after each file
            }
        }
    }
    
    /**
     * Prints full details including frequency (-po option).
     * Format:
     *   word (total frequency: X):
     *     file1 (frequency: Y) - lines: 1, 3, 5
     *     file2 (frequency: Z) - lines: 2, 4
     */
    public void printFullDetails() {
        Iterator<WordInfo> iterator = wordTree.inorderIterator();
        
        while (iterator.hasNext()) {
            WordInfo wordInfo = iterator.next();
            System.out.println(wordInfo.getWord() + 
                             " (total frequency: " + wordInfo.getTotalFrequency() + "):");
            
            for (FileEntry entry : wordInfo.getFileEntries()) {
                System.out.println("  " + entry.getFileName() + 
                                 " (frequency: " + entry.getFrequency() + ") - lines: ");
                
                System.out.print("    ");  // Indent for line numbers
                boolean firstLine = true;
                for (Integer line : entry.getLineNumbers()) {
                    if (!firstLine) {
                        System.out.print(", ");
                    }
                    System.out.print(line);
                    firstLine = false;
                }
                System.out.println();  // New line after line numbers
            }
            System.out.println();  // Blank line between words
        }
    }
    
    /**
     * Main method - program entry point.
     * Parses command-line arguments and runs WordTracker.
     * 
     * Command format: java -jar WordTracker.jar <input.txt> -pf/-pl/-po [-f<output.txt>]
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        // Check minimum arguments (need at least input file and option)
        if (args.length < 2) {
            printUsage();
            return;
        }
        
        String inputFile = args[0];    // First argument: input file
        String option = args[1];       // Second argument: -pf, -pl, or -po
        String outputFile = null;      // Optional output file
        
        // Check for optional output file argument (-f<filename>)
        if (args.length == 3 && args[2].startsWith("-f")) {
            outputFile = args[2].substring(2);  // Remove "-f" prefix to get filename
        } else if (args.length == 3) {
            // Third argument exists but doesn't start with -f
            printUsage();
            return;
        }
        
        // Validate the option argument
        if (!option.equals("-pf") && !option.equals("-pl") && !option.equals("-po")) {
            printUsage();
            return;
        }
        
        // Create WordTracker and process the input file
        WordTracker tracker = new WordTracker();
        tracker.processFile(inputFile);
        
        // Generate output (to console or file)
        if (outputFile != null) {
            redirectOutputToFile(tracker, outputFile, option);
        } else {
            generateOutput(tracker, option);
        }
    }
    
    /**
     * Redirects output to a file instead of the console.
     * Uses System.setOut() to redirect standard output.
     * 
     * @param tracker WordTracker instance
     * @param outputFile file to write output to
     * @param option report format option (-pf, -pl, or -po)
     */
    private static void redirectOutputToFile(WordTracker tracker, String outputFile, String option) {
        try (PrintStream fileOut = new PrintStream(new File(outputFile))) {
            PrintStream originalOut = System.out;  // Save original console output
            System.setOut(fileOut);  // Redirect System.out to file
            
            generateOutput(tracker, option);  // Generate output (goes to file)
            
            System.setOut(originalOut);  // Restore console output
            System.out.println("Output written to: " + outputFile);
            
        } catch (FileNotFoundException e) {
            System.err.println("Error: Cannot write to output file - " + outputFile);
        }
    }
    
    /**
     * Generates output based on the specified option.
     * 
     * @param tracker WordTracker instance
     * @param option report format option (-pf, -pl, or -po)
     */
    private static void generateOutput(WordTracker tracker, String option) {
        switch (option) {
            case "-pf":
                tracker.printFiles();
                break;
            case "-pl":
                tracker.printFilesWithLines();
                break;
            case "-po":
                tracker.printFullDetails();
                break;
        }
    }
    
    /**
     * Prints usage instructions when command-line arguments are invalid.
     */
    private static void printUsage() {
        System.out.println("Usage: java -jar WordTracker.jar <input.txt> -pf/-pl/-po [-f<output.txt>]");
        System.out.println("Options:");
        System.out.println("  -pf : Print words with file names");
        System.out.println("  -pl : Print words with file names and line numbers");
        System.out.println("  -po : Print full details (files, lines, frequency)");
        System.out.println("  -f<output.txt> : Optional - redirect output to file");
        System.out.println("\nExamples:");
        System.out.println("  java -jar WordTracker.jar sample.txt -pf");
        System.out.println("  java -jar WordTracker.jar sample.txt -pl -fresults.txt");
        System.out.println("  java -jar WordTracker.jar sample.txt -po");
    }
}
