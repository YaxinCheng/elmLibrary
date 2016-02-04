package com.example.library.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestBooks {

	public static void main(String[] args) throws IOException {
			
		String filepath = "/Users/salman/workspace/library/src/com/example/library/book-service-config.txt";
		File file = new File(filepath);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		while ((line = br.readLine()) != null) {
			String[] bookInfo = line.split("%%%%");
			String isbn = bookInfo[0];
			String title = bookInfo[1];
			String authorsDelimitedByAnd = bookInfo[2];
			List<String> authors = Arrays.asList(authorsDelimitedByAnd.split("&&&&"));
			String publisher = bookInfo[3];
			String year = bookInfo[4];
			String edition = bookInfo[5];

			Book book = new Book(isbn, title, authors, publisher, year, edition);
			System.out.println(book);
		}

	}

}
