package com.paypal.test.stylo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.common.util.Joiner;
import edu.emory.mathcs.nlp.component.tokenizer.EnglishTokenizer;
import edu.emory.mathcs.nlp.component.tokenizer.Tokenizer;
import edu.emory.mathcs.nlp.component.tokenizer.token.Token;

class Book {
	private Map<String, Integer> punctMap;
	private int wordCount;
	private int lineCount;
	private int avgLineLength;
	private String path;
	private ArrayList<String> topWords;
	private ArrayList<String> bottomWords;
	private ArrayList<String> tokenList;
	private ArrayList<String> nGramList;
	private List<String> topNgrams;

	public Book(String filepath) {
		this.path = filepath;

		setWordCountAndTokenList();
		this.punctMap = countAndRemoveDelimiters(tokenList);
		processLines();
		this.tokenList = removeStopWords(this.tokenList,
				(HashMap<String, Boolean>) getStopwordsMap("C:\\Users\\pushpisingh\\Desktop\\stopwords.txt"));
		this.topWords = topNWords(tokenList, 5);
		this.topNgrams = topNWords(nGramList, 5);
		this.bottomWords = bottomNwords(tokenList, 5);
	}

	public int getWordCount() {
		return this.wordCount;
	}

	public int getLineCount() {
		return this.lineCount;
	}

	public int getAvgLineLength() {
		return this.avgLineLength;
	}

	public String getPath() {
		return this.path;
	}

	public ArrayList<String> getTopWords(int n) {
		return topNWords(tokenList, n);
	}

	public List<String> getTopNgrams() {
		return this.topNgrams;
	}

	public Map<String, Integer> countAndRemoveDelimiters(ArrayList<String> tokens) {
		Map<String, Integer> punctMap = new HashMap<String, Integer>();
		ArrayList<String> punctuation = new ArrayList<String>();
		// System.out.println("Token list size : " + tokens.size());
		String str;
		punctuation.add("!");
		punctuation.add(",");
		punctuation.add(";");
		punctuation.add(".");
		punctuation.add("?");
		punctuation.add("-");
		punctuation.add("\"");
		punctuation.add("\'");
		punctuation.add("#");
		punctuation.add("%");
		punctuation.add("$");
		// punctuation.add("\u0000");

		int frequencyCount = 0;
		for (int ctr = 0; ctr < tokens.size(); ctr++) {
			str = tokens.get(ctr);
			for (String mark : punctuation) {
				if (str.contains(mark)) {
					frequencyCount = punctMap.getOrDefault(mark, 0) + 1;
					// System.out.println("Marking" + mark);
					tokens.set(ctr, str.replace(mark, ""));
					punctMap.put(mark, frequencyCount);
				}
			}
		}
		// System.out.println("Punct : " + punctMap);
		return punctMap;

	}

	public void setWordCountAndTokenList() {
		ArrayList<String> tokenList = new ArrayList<String>();
		Tokenizer tokenizer = new EnglishTokenizer();
		InputStream in = IOUtils.createFileInputStream(this.path);
		int tokenCount = 0;
		for (List<Token> tokens : tokenizer.segmentize(in)) {
			tokenCount = tokenCount + tokens.size();
			for (Token str : tokens) {
				tokenList.add(str.toString().toLowerCase());
			}
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.wordCount = tokenCount;
		this.tokenList = tokenList;
	}

	private void processLines() {
		Tokenizer tokenizer = new EnglishTokenizer();
		BufferedReader in = IOUtils.createBufferedReader(this.path);
		ArrayList<String> nGramList = new ArrayList<String>();
		List<Token> tokens;
		String line;
		int lineCount = 0;
		int lineLength = 0;
		try {
			while ((line = in.readLine()) != null) {
				if (line.length() == 0) {
					continue;
				}
				tokens = tokenizer.tokenize(line);
				lineCount++;
				lineLength = lineLength + tokens.size();
				nGramList.addAll(nGrams(5, line));
				// System.out.println(Joiner.join(tokens, " "));
			}
		} catch (IOException e) {
			e.getMessage();
		}

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.lineCount = lineCount;
		this.avgLineLength = lineLength / lineCount;
		this.nGramList = nGramList;
		// System.out.println("Tri-grams" + nGramList);
	}

	public ArrayList<String> getBottomWords(int n) {
		return bottomNwords(tokenList, n);
	}

	public ArrayList<String> topNWords(ArrayList<String> words, int n) {
		Map<String, Integer> wordsMap = new HashMap<String, Integer>();
		ArrayList<String> temp;
		ArrayList<String> topNWords = new ArrayList<String>();
		int highestFrequency = 0, frequencyCount = 0, count = 0;
		for (String word : words) {
			frequencyCount = wordsMap.getOrDefault(word, 0) + 1;
			wordsMap.put(word, frequencyCount);
			if (frequencyCount > highestFrequency) {
				highestFrequency = frequencyCount;
			}
		}

		ArrayList<ArrayList<String>> frequency = new ArrayList<ArrayList<String>>(highestFrequency + 1);
		for (int i = 0; i < highestFrequency + 1; i++) {
			frequency.add(i, new ArrayList<String>());
		}
		for (String key : wordsMap.keySet()) {
			frequencyCount = wordsMap.get(key);
			// System.out.println(frequency);
			temp = frequency.get(frequencyCount);
			temp.add(key);
		}
		for (int ctr = frequency.size() - 1; ctr >= 0; ctr--) {
			if (count < n) {
				for (String str : frequency.get(ctr)) {
					topNWords.add(str);
				}

				count += frequency.get(ctr).size();
			} else
				break;
		}
		return topNWords;
	}

	public static List<String> nGrams(int n, String str) {
		List<String> ngrams = new ArrayList<String>();
		String[] words = str.split(" ");
		for (int i = 0; i < words.length - n + 1; i++)
			ngrams.add(concat(words, i, i + n));
		return ngrams;
	}

	public static String concat(String[] words, int start, int end) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i++)
			sb.append((i > start ? " " : "") + words[i]);
		return sb.toString();
	}

	public ArrayList<String> removeStopWords(ArrayList<String> words, HashMap<String, Boolean> stopwordsMap) {
		String word;
		// System.out.println(stopwordsMap);
		ArrayList<String> filteredWords = new ArrayList<String>();
		for (int ctr = 0; ctr < words.size(); ctr++) {
			word = words.get(ctr);
			if (!stopwordsMap.containsKey(word)) {
				filteredWords.add(word);
			}
		}
		return filteredWords;
	}

	private Map<String, Boolean> getStopwordsMap(String filepath) {
		Scanner input;
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(filepath);
			input = new Scanner(inputStream, "UTF-8");
			while (input.hasNextLine()) {
				map.put(input.next().toLowerCase(), true);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;

	}

	public ArrayList<String> bottomNwords(ArrayList<String> words, int n) {
		// System.out.println("Printing bottom " + n);
		Map<String, Integer> wordsMap = new HashMap<String, Integer>();
		ArrayList<String> temp;
		ArrayList<String> bottomNwords = new ArrayList<String>();
		int highestFrequency = 0, frequencyCount = 0, count = 0;
		for (String word : words) {
			frequencyCount = wordsMap.getOrDefault(word, 0) + 1;
			wordsMap.put(word, frequencyCount);
			if (frequencyCount > highestFrequency) {
				highestFrequency = frequencyCount;
			}
		}
		// System.out.println("Printing highest frequency"+highestFrequency);
		ArrayList<ArrayList<String>> frequency = new ArrayList<ArrayList<String>>(highestFrequency + 1);
		for (int i = 0; i < highestFrequency + 1; i++) {
			frequency.add(i, new ArrayList<String>());
		}
		for (String key : wordsMap.keySet()) {
			frequencyCount = wordsMap.get(key);
			temp = frequency.get(frequencyCount);
			temp.add(key);
		}
		// System.out.println("Freq : " + frequency);
		for (int ctr = 0; ctr < frequency.size(); ctr++) {
			if (count < n) {
				for (String str : frequency.get(ctr)) {
					bottomNwords.add(str);
				}
				count += frequency.get(ctr).size();
				// System.out.println("Count : " + count);
			} else
				break;
		}
		return bottomNwords;
	}

}
