/***********************************************************
* The word count program reads in a file, line by line, and
* counts the words in each line. After each line we update
* the number of times a given word occurs.
***********************************************************/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap; 
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Map;

public class WordCount{
	private BufferedReader reader;
	private TreeMap<String, Integer> wordCount;
	
	/*
	* Here we are assuming that a word is any string of 
	* characters between any of the two punctuation
	* characters in the below REGEX string.
	* We can easily modify what is a word by simply
	* changing this regex.
	*/
	private static final int N_LARGEST_VALS = 11;
	private static final String REGEX = "[^A-Za-z0-9_]+";
	
	public WordCount(){
		wordCount = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
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
	
	// This method is O(n), so it is faster than sorting
	public void displayMostCommon(){
		ArrayList<Map.Entry<String, Integer> > mostCommon;
		mostCommon = new ArrayList<Map.Entry<String, Integer> >();
		for(Map.Entry<String, Integer> el : wordCount.entrySet()){
			// first add the first 10 elements sorted large to small
			int i = 0;
			if(mostCommon.isEmpty()){
				mostCommon.add(el);
			}
			else if(i < N_LARGEST_VALS){
				// insert the remaining elements in the correct order
				int j = 0;
				while(j < mostCommon.size() && el.getValue().intValue() 
						< mostCommon.get(j).getValue().intValue()){
					++j;
				}
				mostCommon.add(j, el);
			}
			else{
				/*
				* Now we can add elements to the front of the list
				* when they are larger than the head, otherwise we
				* can ignore them
				*/
				if(el.getValue().intValue() 
						> mostCommon.get(0).getValue().intValue()){
					mostCommon.add(el);
				}
			}
		}
		// Display the first ten elements
		for(int i = 0; i < N_LARGEST_VALS; ++i){
			if(i > mostCommon.size()){ break; }
			System.out.println(mostCommon.get(i).getKey());
		}
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
	
	public void printMap(){
		System.out.println(wordCount);
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
		//wc.printMap();
		wc.displayMostCommon();
		wc.closeFile();
	}
}
