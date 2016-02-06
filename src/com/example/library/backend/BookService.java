package com.example.library.backend;

import com.example.library.backend.Book;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookService {

	private static BookService instance;
	private HashMap<String, Book> books = new HashMap<>();
		
	public static BookService createDemoService() {
		if (instance == null) {
			final BookService bookService = new BookService();
			
			// read from the config file and populate the BookService
			String filepath = "/Users/Roc/Documents/2015-2016Winter/CSCI3130/Project/library/src/com/example/library/book-service-config.txt";
//			populateBookService(bookService, filepath);
			
			instance = bookService;
		}

		return instance;
	}

	private static void populateBookService(BookService bookService, String filepath) {
		String line;
		try(BufferedReader br = new BufferedReader(new FileReader(filepath))) {		
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
				// System.out.println(book);
				bookService.save(book);
			}		
		} catch (IOException e) {
			System.out.print("IOException - Book configuration file could not be read - " + e);
		}
	}

	public synchronized List<Book> findAll(String filter) {
        List<Book> arrayList = new ArrayList<Book>();
        for (Book book: books.values()) {
            try {
            	if (book.containInformation(filter)) {
            		arrayList.add(book.clone());
            	}
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(BookService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return (o1.getTitle().compareTo(o2.getTitle()));
            }
        });
        
        System.out.println("books list: " + arrayList);
        return arrayList;
    }

	public synchronized long count() {
		return books.size();
	}

	public synchronized void delete(Book value) {
		books.remove(value.getIsbn());
		System.out.println(value.getIsbn());
	}

	public synchronized void save(Book entry) {
		System.out.println(entry.getIsbn());
		if (!books.containsKey( entry.getIsbn() )) {
        	books.put(entry.getIsbn() , entry);
        }
	}

}
