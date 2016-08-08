package com.paypal.test.stylo;

import java.util.ArrayList;

public class Author {
	private ArrayList<String> bookPaths;
	private ArrayList<Book> bookList;
	private int averageLineLength;
	private String name;

	public Author(ArrayList<String> paths) {
		int n = 0;
		ArrayList<Book> books = new ArrayList<Book>();
		for (String path : paths) {
			books.add(new Book(path));

		}
		this.bookList = books;
		for (Book b : this.bookList) {
			n = n + b.getAvgLineLength();
		}
		this.averageLineLength = n / this.bookList.size();
	}

	public void setName(String str) {
		this.name = str;
	}

	public String getName() {
		return this.name;
	}

	public void printInfo() {
		for (Book b : this.bookList) {
			System.out.println("For book : " + b.getPath());
			System.out.println("Total words : " + b.getWordCount());
			System.out.println("Total lines :" + b.getLineCount());
			System.out.println("Average Line Length : " + b.getAvgLineLength());
			System.out.println("Top words : " + b.getTopWords(10));
			System.out.println("Top n grams " + b.getTopNgrams());
			System.out.println("Bottom n words : " + b.getBottomWords(5));
		}
	}

	public int getAverageLineLength() {
		return this.averageLineLength;
	}
}
