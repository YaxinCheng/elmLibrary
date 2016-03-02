package com.example.library.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.server.VaadinServlet;

public class BookService {
	/*
	 * creating an entity manager, this lets us create a JPAContainer of type
	 * Book which is named 'shelf'
	 */
	private static BookService instance;
	static EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("library_db");
	public JPAContainer<Book> shelf = JPAContainerFactory.make(Book.class, em);

	/*
	 * this function will run upon initialization of the program it will find
	 * the text file containing the library's stock and fill the shelf container
	 */
	public static BookService createDemoService() {
		if (instance == null) {
			final BookService bookService = new BookService();
			/*
			 * Read some books from the config. file and populate the database
			 */
			ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
			InputStream stream = servletContext.getResourceAsStream("/config/book-service-config.txt");
			bookService.populateBookService(bookService, stream);
			instance = bookService;
		}
		return instance;
	}

	/**
	 * this function works by reading a text file using the BufferedReader and
	 * then placing those values as the properties of a new book, each row in
	 * the text file will represent an instance of a book
	 */
	private void populateBookService(BookService bookService, InputStream in) {
		String line;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
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
				System.out.println(id);
				//@SuppressWarnings("unused")
				//EntityItem<Book> book = shelf.getItem(id);
			}
		} catch (IOException e) {
			System.out.print("ERROR - IOException - Book configuration file could not be read - " + e);
		}
	}

	/**
	 * 
	 * this function works in conjunction with the search bar to display the
	 * books that match the filter string
	 */
	public synchronized List<Book> findAll(String filter) throws CloneNotSupportedException {
		List<Book> arrayList = new ArrayList<Book>();
		Collection<Object> id = shelf.getItemIds();
		// System.out.println(id);
		for (int i = 0; i < shelf.size() + 1; i++) {
			if (id.contains(i)) {

			}
		}
		Collections.sort(arrayList, new Comparator<Book>() {
			@Override
			public int compare(Book o1, Book o2) {
				return (o1.compareTo(o2));
			}
		});
		// System.out.println("filtered books list: " + arrayList);
		return arrayList;
	}

	// to get the size of container
	public synchronized long count() {
		return shelf.size();
	}

	/* this function will simply delete a book from the 'shelf' container */
	public synchronized boolean delete(EntityItem<Book> book) {
		try {
			shelf.removeItem(book.getItemId());
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public synchronized boolean save(String ISBN, String title, String publisher, String years, String edition, List<String> authour) {
		if (checkDuplicate(ISBN) == true) {
			return false;
		}
		shelf.addEntity(new Book(ISBN, title, authour, publisher, years, edition));
		return true;
	}
	
	public synchronized void searchYear(String info) {
		String[] components = info.toLowerCase().split("-");
		String toDate = components[1];
		String fromDate = components[0];
		Filter from = new Compare.GreaterOrEqual("year", fromDate);
		Filter to = new Compare.LessOrEqual("year", toDate);
		Filter composite = new And(from, to);// Search for book in this
												// range
		shelf.addContainerFilter(composite);
		shelf.refresh();
	}
	
	public synchronized void searchInfo(String info) {
		Filter title = new Like("title", "%" + info + "%", false);
		Filter publisher = new Like("publisher", "%" + info + "%", false);
		Filter author = new Like("authorInformation", "%" + info + "%", false);
		Filter composite = new Or(title, publisher, author);
		shelf.addContainerFilter(composite);// add filter
		shelf.refresh();
	}

	// method check whether a book is duplicated ( with same isbn )
	public boolean checkDuplicate(String isbn) {
		for (long i = 1; i <= shelf.getItemIds().size(); i++) {
			if (shelf.getItemIds().contains(i)) {
				if (shelf.getItem(i).getEntity().getIsbn().equals(isbn)) {
					return true;
				}
			}
		}
		return false;
	}

	// method that can remove all filters created
	public void removeAllFilters() {
		Collection<Filter> filters = shelf.getContainerFilters();
		for (Filter filter : filters) {
			shelf.removeContainerFilter(filter);
		}
	}
}