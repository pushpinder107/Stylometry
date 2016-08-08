package com.paypal.test.stylo;

import java.util.*;

public class characterCount {
	
	public static HashMap<String,String> generateBigrams(ArrayList<String> words){
		
		HashMap<String,String> bigrams=new HashMap<String,String>();
		for(int i=0;i<words.size()-2;i++){
			if(words.get(i)!="\n" && !words.get(i).contains(".") && !words.get(i+1).contains(".") ){
				
			String valueWord=words.get(i+2);
			if(!valueWord.contains(".")){
				words.get(i+2).replace(".", "");
				bigrams.put(words.get(i)+ " " + words.get(i+1), words.get(i+2));
								
			}
			else
			{
				//System.out.println("Valueword is"+ valueWord);
				valueWord.replace(".", " ");
				//words.get(i+2).replace(".", "");
				bigrams.put(words.get(i)+words.get(i+1), words.get(i+2));
				
				
				}
			}
		
		
		
		}
		return bigrams;
	}

}
