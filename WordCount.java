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
import java.text.Collator;
import java.util.Locale;

public class WordCount{
	private BufferedReader reader;
	/*
	* Using a TreeMap rather than a HashMap to more easily
	* store case independent strings with the constructor
	* which takes a comparator as a constructor.
	*/
	private TreeMap<String, Integer> wordCount;
	private Locale userLocale;
	
	/*
	* Here we are assuming that a word is any string of 
	* characters between any of the two punctuation
	* characters in the below REGEX string.
	* We can easily modify what is a word by simply
	* changing this regex.
	*/
	private static final String REGEX = "[^A-Za-z0-9'_]+";
	private static final int N_LARGEST_VALS = 10;
	
	/*
	* We will use a collator to pass to the TreeMap to use for
	* comparing strings of locale type userLocale
	*/
	public WordCount(Locale userLocale){
		this.userLocale = userLocale;
		Collator localeComp = Collator.getInstance(this.userLocale);
		localeComp.setStrength(Collator.PRIMARY);
		wordCount = 
		new TreeMap<String, Integer>(localeComp);
	}
	
	public WordCount(){
		this(Locale.getDefault());
	}
	
	// prepares the file to be read.
	public void openFile(String fileName){
		try{
			reader = 
				new BufferedReader(
					new InputStreamReader(
						new FileInputStream(fileName),"UTF-8"));
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
	* This will be done N_LARGEST_VAL times, and finding the 
	* max is O(n), so this method is N_LARGEST_VAL*O(n)=O(n).
	*/
	public void displayMostCommon(){
		for(int i = 0; i < N_LARGEST_VALS && !wordCount.isEmpty(); ++i){
			Map.Entry<String, Integer> curMax = keyWithMaxValue();
			System.out.println(curMax.getKey()+"\t"+curMax.getValue());
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
					String utfLine = new String(curLine.getBytes(), "UTF-8");
					processLine(utfLine.trim());
				}
			}
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		WordCount wc = new WordCount(Locale.JAPANESE);
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
