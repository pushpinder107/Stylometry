package com.paypal.test.stylo;

public class Classifier {

	private Author a1;
	private Author a2;

	public Classifier(Author a1, Author a2) {
		this.a1 = a1;
		this.a2 = a2;
	}

	public Author isCloserTo(Book b) {
		int bookAvg = b.getAvgLineLength();
		int b1 = a1.getAverageLineLength();
		int b2 = a2.getAverageLineLength();
		Author result;
		int avg = (b2 + b1) / 2;
		if (bookAvg - avg > 0) {
			result = b1 > b2 ? a1 : a2;
		} else {
			result = b1 < b2 ? a1 : a2;
		}
		return result;
	}
}
