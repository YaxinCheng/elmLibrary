package com.example.library.backend;

import com.example.library.backend.Book;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.ui.Table;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

public class BookService {

	private static BookService instance;
	// private HashMap<String, Book> books = new HashMap<>();//old
	// implementation

	/*
	 * creating an entity manager for some reason, this lets us create a
	 * JPAContainer of type Book
	 */
	static EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("library_db");
	public static JPAContainer<Book> shelf = JPAContainerFactory.make(Book.class, em);

	public static BookService createDemoService() {
		if (instance == null) {
			final BookService bookService = new BookService();
			/* if someone can make this path RELATIVE i will be overjoyed */
			String filepath = "/Users/Roc/Documents/2015-2016Winter/CSCI3130/Project/library/src/com/example/library/book-service-config.txt";
			/*
			 * this function call happens when application UI is being
			 * generated, it will read from the text file, file the 'shelf'
			 * container, and then be spit out to the UI
			 */
			populateBookService(bookService, filepath);
			instance = bookService;
		}
		return instance;
	}

	/**
	 * this function works by reading a text file using the BufferedReader and
	 * then placing those values as the properties of a new book, each row in
	 * the text file will represent an instance of a book
	 */
	private static void populateBookService(BookService bookService, String filepath) {
		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
			while ((line = br.readLine()) != null) {
				String[] bookInfo = line.split("%%%%");
				String isbn = bookInfo[0];
				String title = bookInfo[1];
				String authorsDelimitedByAnd = bookInfo[2];
				List<String> authors = Arrays.asList(authorsDelimitedByAnd.split("&&&&"));
				String publisher = bookInfo[3];
				String year = bookInfo[4];
				String edition = bookInfo[5];

				/* adding the book to the shelf */
				Object id = shelf.addEntity(new Book(isbn, title, authors, publisher, year, edition));
				EntityItem<Book> book = shelf.getItem(id);
				// bookService.save(book, false);
			}
		} catch (IOException e) {
			System.out.print("IOException - Book configuration file could not be read - " + e);
		}
	}

	/**
	 * this function has been giving me lots of trouble after implementing the
	 * JPAContainer we just need to figure out a way to sort and filter through
	 * the 'shelf'
	 * 
	 * with that said, this function works in conjunction with the search bar to
	 * display the books that match the filter string
	 */
	public synchronized List<Book> findAll(String filter) throws CloneNotSupportedException {
		List<Book> arrayList = new ArrayList<Book>();
		System.out.println(shelf.size() + "fuck");
		Collection<Object> id = shelf.getItemIds();
		System.out.println(id);
		for (int i = 0; i < shelf.size() + 1; i++) {
			if (id.contains(i)) {
				System.out.println(i + "FUCJ YA");
			}
			// if (shelf.getItem(i).getEntity().containInformation(filter)) {
			// arrayList.add(shelf.getItem(i).getEntity().clone());
			// }
		}
		// for (Book book : books.values()) {
		// try {
		// if (book.containInformation(filter)) {
		// arrayList.add(book.clone());
		// }
		// } catch (CloneNotSupportedException ex) {
		// Logger.getLogger(BookService.class.getName()).log(Level.SEVERE, null,
		// ex);
		// }
		// }
		Collections.sort(arrayList, new Comparator<Book>() {
			@Override
			public int compare(Book o1, Book o2) {
				return (o1.compareTo(o2));
			}
		});
		System.out.println("filtered books list: " + arrayList);
		return arrayList;
	}

	public synchronized long count() {
		return shelf.size();
	}

	/* this function will simply delete a book from the 'shelf' container */
	public synchronized void delete(EntityItem<Book> book) {
		shelf.removeItem(book.getItemId());
	}

	/**
	 * this function will either replace and then add an edited book, else it
	 * will save the book to the shelf
	 * 
	 * I am having trouble with this function, the goal is to not add a
	 * duplicate ISBN however with the JPAContainer i haven't learned how to yet
	 */
	public synchronized boolean save(EntityItem<Book> book, boolean modification) {
		boolean dup = false;
		if (modification) {
			delete(book);
			return true;
		} else {
			for (long i = 1; i <= shelf.size(); i++) {
				// if
				// (book.getEntity().getIsbn().equals(shelf.getItem(i).getEntity().getIsbn()))
				// {
				// System.out.println(book.getEntity().getIsbn());
				// System.out.println(shelf.getItem(i).getEntity().getIsbn());
				// dup = true;
				// }
			}
			if (dup) {
				System.out.println("DUPLICATE!!!!!");
			} else {
				System.out.println("we goood");
				return true;
			}
		}
		return false;
	}
}