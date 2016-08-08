package com.paypal.test.stylo;

import java.util.*;

public class characterCount {
	
	public static HashMap<String,String> generateBigrams(ArrayList<String> words){
		
		HashMap<String,String> bigrams=new HashMap<String,String>();
		for(int i=0;i<words.size();i++){
			if(words.get(i+2)!=""){
				bigrams.put(words.get(i)+words.get(i+1), words.get(i+2));
								
			}
		}
		
		return bigrams;
		
	}

}
