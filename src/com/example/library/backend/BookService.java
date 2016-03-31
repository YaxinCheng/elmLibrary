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
import java.util.Date;
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
	private EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("library_db");
	public JPAContainer<Book> shelf = JPAContainerFactory.make(Book.class, em);

	/**
	 * this function will run upon initialization of the program it will find
	 * the text file containing the library's stock and fill the shelf container
	 */
	public static BookService initialize() {
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
				shelf.addEntity(new Book(isbn, title, authors, publisher, year, edition));
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
		return arrayList;
	}

	/**
	 * @return Number of books in the container right now 
	 */
	public synchronized long count() {
		return shelf.size();
	}

	/**
	 *  this function will simply delete a book from the 'shelf' container
	 *  @return true if delete successfully
	 */
	public synchronized boolean delete(EntityItem<Book> book) {
		try {
			shelf.removeItem(book.getItemId());
			User user = book.getEntity().borrowedBy();
			user.getBorrowed().remove(book.getEntity());
			UserService.initialize().replace(user);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Get information of a book and save it in the JPAContainer
	 * @param ISBN
	 * @param title
	 * @param publisher
	 * @param years
	 * @param edition
	 * @param authour
	 * @return true if save successfully, false if a book with the same ISBN exists
	 */
	public synchronized boolean save(String ISBN, String title, String publisher, String years, String edition,
			List<String> authour) {
		if (checkDuplicate(ISBN) == true) {
			return false;
		}
		shelf.addEntity(new Book(ISBN, title, authour, publisher, years, edition));
		return true;
	}
	
	/**
	 * Update the copy in the JPAContainer for the book
	 * @param book book need to be updated
	 */
	public synchronized void replaceBook(EntityItem<Book> book) {
		if (shelf.removeItem(book.getItemId())) {
			shelf.addEntity(book.getEntity());
		}
	}

	/**
	 * Search books in the years span
	 * @param info year information
	 */
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

	/**
	 * Search books with related information
	 * @param info information needed
	 */
	public synchronized void searchInfo(String info) {
		Filter title = new Like("title", "%" + info + "%", false);
		Filter publisher = new Like("publisher", "%" + info + "%", false);
		Filter author = new Like("authorInformation", "%" + info + "%", false);
		Filter composite = new Or(title, publisher, author);
		shelf.addContainerFilter(composite);// add filter
		shelf.refresh();
	}

	/**
	 * Method check whether a book is duplicated ( with same isbn )
	 * @param isbn the isbn needed to be checked
	 * @return true if the book is duplicated
	 */
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

	/**
	 *  Method that can remove all filters created
	 */
	public void removeAllFilters() {
		Collection<Filter> filters = shelf.getContainerFilters();
		for (Filter filter : filters) {
			shelf.removeContainerFilter(filter);
		}
	}
	
	/**
	 * Check out a book for the user
	 * @param book book needed to be checked out
	 * @param user user who check out the book
	 * @throws NullPointerException if user is null, throws NullPointerException
	 */
	public void bookCheckOut(EntityItem<Book> book, User user) throws NullPointerException {
		if (user == null) {
			throw new NullPointerException("You need to log in first before you can rent a book.");
		}
		book.getEntity().setCheckOutDate(new Date());
		book.getEntity().setCheckOut(true);
		if (user.getAccount().equals("bray")) {
			book.getEntity().setReturnDate(new Date(System.currentTimeMillis() + (12342 * 7 * 1000)));
			List<Book> due = Arrays.asList(book.getEntity());
			user.setFees(user.totalFees(due));
		} else 
			book.getEntity().setReturnDate(new Date(System.currentTimeMillis() + (86400 * 7 * 1000)));
		user.getBorrowed().add(book.getEntity());
		book.getEntity().lendTo(user);
	}

	/**
	 * Return a book by the user
	 * @param book Book needed to be returned
	 * @param user User who returns the book
	 * @throws NullPointerException Throw when user is null
	 */
	public void bookReturn(EntityItem<Book> book, User user) throws NullPointerException {
		if (user == null) {
			throw new NullPointerException("You need to log in first before you can rent a book.");
		}
		user.getBorrowed().remove(book.getEntity());
		book.getEntity().setCheckOut(false);
		book.getEntity().setCheckOutDate(null);
		book.getEntity().setReturnDate(null);
		book.getEntity().lendTo(null);
	}
	
	/**
	 * Return a book by the user
	 * @param book Book needed to be returned
	 * @param user User who returns the book
	 * @throws NullPointerException Throw when user is null
	 */
	public void bookReturn(Book book, User user) throws NullPointerException {
		if (user == null) {
			throw new NullPointerException("You need to log in first before you can rent a book.");
		}
		user.getBorrowed().remove(book);
		shelf.removeItem(book.getIsbn());
		book.setCheckOut(false);
		book.setCheckOutDate(null);
		book.setReturnDate(null);
		book.lendTo(null);
		shelf.addEntity(book);
	}
	
	/**
	 * Only keep checked out books in the container
	 */
	public void rentedBooks() {
		Filter rented = new Compare.Equal("checkOut", "true");
		shelf.addContainerFilter(rented);
	}
	
	/**
	 * Only keep late books in the container
	 */
	public void lateBooks() {
		Filter late = new Compare.Less("returnDate", new Date());
		shelf.addContainerFilter(late);
	}
}