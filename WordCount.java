/***********************************************************
* The word count program reads in a file, line by line, and
* counts the words in each line. After each line we update
* the number of times a given word occurs.
***********************************************************/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Map;
import java.nio.charset.StandardCharsets;

public class WordCount{
	private BufferedReader reader;
	/*
	* Using a TreeMap rather than a HashMap to more easily
	* store case independent strings with the constructor
	* which takes a comparator as a constructor. The speed 
	* tradeoff by using TreeMap instead of HashMap allows 
	* the program to more easily seperate words. We still
	* get O(log n) time for get and put
	*/
	private TreeMap<String, Integer> wordCount;
	
	/*
	* We seperate our words by the below regex. If the character
	* is none of the below (a number, apostrophe, underscore, or
	* letter in any language) it is part of a word.
	*/
	private static final String REGEX = "[^0-9'_\\p{L}]+";
	private static final int NUM_TO_DISPLAY = 10;
	
	public WordCount(){
		wordCount = 
			new TreeMap<String, Integer>
				(String.CASE_INSENSITIVE_ORDER);
	}
	
	// prepares the file to be read.
	public void openFile(String fileName){
		try{
			reader = 
				new BufferedReader(
					new InputStreamReader(
						new FileInputStream(fileName),
							StandardCharsets.UTF_8));
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
		for(String s : words){
			int numOfWords = 0;
			Integer wordKey = wordCount.get(s);
			// word not found
			if(wordKey == null){
				numOfWords = 1;
			}
			else{
				// word found, increment the count
				numOfWords = wordKey+1;
			}
			// insert the new word, or update the count
			wordCount.put(s, numOfWords);
		}
	}
	
	public static void usage(){
		System.out.println("Usage: java [FILE NAME]");
	}
	
	/*
	* We will find the largest entry in our TreeMap and print
	* the largest entries key, then remove it from the TreeMap.
	* This will be done NUM_TO_DISPLAY times, and finding the 
	* max is O(n), so this method is NUM_TO_DISPLAY*O(n)=O(n).
	*/
	public void displayMostCommon(){
		for(int i = 0; i < NUM_TO_DISPLAY && !wordCount.isEmpty(); ++i){
			Map.Entry<String, Integer> curMax = keyWithMaxValue();
			System.out.println(curMax.getKey());
			wordCount.remove(curMax.getKey());
		}
	}
	
	/*
	* This method loops through the entire tree map using a
	* for each loop, and returns the Map.Entry element which
	* has the largest value. This method is O(n).
	*/
	private Map.Entry<String, Integer> keyWithMaxValue(){
		Map.Entry<String, Integer> maxEntry = null;
		for(Map.Entry<String, Integer> el : wordCount.entrySet()){
			if(maxEntry == null){
				maxEntry = el;
			}
			else{
				int elVal = el.getValue().intValue();
				int maxVal = maxEntry.getValue().intValue();
				if(elVal > maxVal){
					maxEntry = el;
				}
			}
		}
		return maxEntry;
	}
	
	// read each line and process it via processLine(String)
	public void processFile(){
		String curLine;
		try{
			while((curLine = reader.readLine()) != null){
				// we want to ignore any lines of only white space
				if(!curLine.trim().isEmpty()){
					String utfLine = 
						new String(curLine.getBytes(), "UTF-8");
					processLine(utfLine.trim());
				}
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
