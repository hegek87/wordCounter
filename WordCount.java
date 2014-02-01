/***********************************************************
* The word count program reads in a file, line by line, and
* counts the words in each line. After each line we update
* the number of times a given word occurs.
***********************************************************/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap; 

public class WordCount{
	private BufferedReader reader;
	private HashMap<String, Integer> wordCount;
	
	/*
	* Here we are assuming that a word is any string of 
	* characters between any of the two punctuation
	* characters in the below REGEX string.
	* We can easily modify what is a word by simply
	* changing this regex.
	*/
	private static final String REGEX = " *[^A-Za-z0-9_]+";
	
	public WordCount(){
		wordCount = new HashMap<String, Integer>();
	}
	
	// prepares the file to be read.
	public void openFile(String fileName){
		try{
			reader = new BufferedReader(new FileReader(fileName));
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	// close files when done reading
	public void closeFile(){
		try{
			reader.close();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	// process a single line of data
	public void processLine(String singleLine){
		String[] words = singleLine.split(REGEX);
		System.out.println(words.length);
		for(String s : words){
			int numOfWords = 0;
			Integer wordKey = wordCount.get(s.toLowerCase());
			// word not found
			if(wordKey == null){
				numOfWords = 1;
			}
			else{
				// word found, increment the count
				numOfWords = wordKey+1;
			}
			// insert the new word, or update the count
			wordCount.put(s.toLowerCase(), numOfWords);
		}
	}
	
	public static void usage(){
		System.out.println("Usage: java [FILE NAME]");
	}
	
	public void displayMostCommon(){
		System.out.println(wordCount);
	}
	
	public void processFile(){
		String curLine;
		try{
			while((curLine = reader.readLine()) != null){
				processLine(curLine);
			}
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		WordCount wc = new WordCount();
		// No command line argument passed in
		if(args.length != 1){
			usage();
			return;
		}
		wc.openFile(args[0]);
		wc.processFile();
		wc.displayMostCommon();
		wc.closeFile();
	}
}
