package com.example.library.backend;

import com.example.library.backend.Book;
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
	private HashMap<String, Book> books = new HashMap<>();
	static EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("library_db");
	public static JPAContainer<Book> shelf = JPAContainerFactory.make(Book.class, em);

	public static BookService createDemoService() {
		if (instance == null) {
			final BookService bookService = new BookService();

			// read from the config file and populate the BookService, make
			// RELATIVE
			String filepath = "/Users/Bray/Documents/library/library/src/com/example/library/book-service-config.txt";
			populateBookService(bookService, filepath);

			instance = bookService;
		}

		return instance;
	}

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

				shelf.addEntity(new Book(isbn, title, authors, publisher, year, edition));
				// System.out.println(book);
				// bookService.save(book, false);
			}
		} catch (IOException e) {
			System.out.print("IOException - Book configuration file could not be read - " + e);
		}
		System.out.println(shelf.size());
	}

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
		return books.size();
	}

	public synchronized void delete(Book value) {
		books.remove(value.getIsbn());
		System.out.println(value.getIsbn());
	}

	public synchronized boolean save(Book entry, boolean modification) {
		if (modification) {
			books.replace(entry.getIsbn(), entry);
			return true;
		}
		// System.out.println(entry.getIsbn());
		if (!books.containsKey(entry.getIsbn())) {
			books.put(entry.getIsbn(), entry);
			return true;
		}
		return false;
	}

}
