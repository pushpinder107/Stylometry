package com.paypal.test.stylo;

import java.util.ArrayList;

public class App {
	public static void main(String[] args) {
		ArrayList<String> bookList = new ArrayList<String>();
		String bookDir = "C:\\Users\\pushpisingh\\Desktop\\gitRepos\\Stylometry\\";
		// bookList.add("C:\\Users\\pushpisingh\\Desktop\\sowpods.txt");
		bookList.add("C:\\Users\\pushpisingh\\Desktop\\gitRepos\\Stylometry\\book11.txt");
		// Author a = new Author(bookList);
		ArrayList<String> shakespeareBooks = new ArrayList<String>();
		shakespeareBooks.add(bookDir + "Hamlet.txt");
		shakespeareBooks.add(bookDir + "Julius_Caesar.txt");
		Author shakespeare = new Author(shakespeareBooks);
		shakespeare.setName("Shakespeare");
		shakespeare.printInfo();
		ArrayList<String> twainBooks = new ArrayList<String>();
		twainBooks.add(bookDir + "\\Twain-books\\book21.txt");
		twainBooks.add(bookDir + "\\Twain-books\\book22.txt");
		Author twain = new Author(twainBooks);
		twain.setName("Twain");
		twain.printInfo();
		Classifier c = new Classifier(shakespeare, twain);
		Book testBook = new Book(bookDir + "\\Twain-books\\book21.txt");
		System.out.println(c.isCloserTo(testBook).getName());
		Book testBook2 = new Book(bookDir + "Macbeth.txt");
		System.out.println(c.isCloserTo(testBook2).getName());
	}
}