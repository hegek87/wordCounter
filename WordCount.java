/***********************************************************
* The word count program reads in a file, line by line, and
* counts the words in each line. After each line we update
* the number of times a given word occurs.
***********************************************************/

import java.io.Buffer;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap; 

public class WordCount{
	private BufferedReader read;
	private HashMap<String, Integer> wordCount;
	
	/*
	* Here we are assuming that a word is any string of 
	* characters between any of the two punctuation
	* characters in the below REGEX string.
	* We can easily modify what is a word by simply
	* changing this regex.
	*/
	private static final String REGEX = " !\'\"!.,;";
	
	public WordCount(){
		wordCount = new HashMap<String, Integer>();
	}
	
	// prepares the file to be read.
	public void openFile(String fileName){
		try{
			read = new BufferedReader(new FileReader(fileName));
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	// close files when done reading
	public void closeFile(){
		read.close();
	}
	
	// process a single line of data
	public void processLine(String singleLine){
		String[] words = singleLine.split(REGEX);
		for(String s : words){
			int numOfWords = 0;
			Integer wordKey = wordCount.get(s);
			// word not found
			if(wordKey == null){
				numOfWords = 1;
			}
			else{
				// word found
				numOfWords = wordKey;
			}
			// insert the new word, or update the count
			wordCount.put(s, numOfWords);
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
