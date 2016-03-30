package com.example.library.backend;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * <h1>Book</h1> This is a class that represents a Book. It has all the
 * attributes for a single book.
 *
 * @author Team-Elm
 * @version 1.0
 * @since 2016-02-01
 */
@Entity
public class Book implements Comparable<Book>, Cloneable {

	@Id
	private String isbn;
	private String title;
	private List<String> authors;
	private String publisher;
	private String year;
	private String edition;
	private boolean checkOut;
	private Date checkOutDate;
	private Date returnDate;
	@SuppressWarnings("unused")
	private String authorInformation;// A string of all authors' names, used for
										// search
	@ManyToOne
	private User user;
	@OneToMany
	private ArrayList<User> waitList = new ArrayList();
	
	/**
	 * @return if the book is checked out
	 */
	public boolean isCheckOut() {
		return checkOut;
	}

	/**
	 * Set check out status for the book
	 * @param checkOut true if checked out, false if not
	 */
	public void setCheckOut(boolean checkOut) {
		this.checkOut = checkOut;
	}

	/**
	 * @param isbn ISBN Number as the ID of the book
	 * @param title Title of the book
	 * @param authors Authors list of the book
	 * @param publisher Publisher of the book
	 * @param year The year the book published
	 * @param edition The edition number
	 */
	public Book(String isbn, String title, List<String> authors, String publisher, String year, String edition) {
		this.isbn = isbn;
		this.title = title;
		this.authors = authors;
		this.publisher = publisher;
		this.year = year;
		this.edition = edition;
		checkOut = false;
		authorInformation = "";
		for (String author : authors) {
			authorInformation += (author + ";");
		}
	}

	public Book(String isbn, String title, List<String> authors, String pub, String year) {
		this(isbn, title, authors, pub, year, "1");
	}

	public Book() {
	}

	// Setter and getter
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	/**
	 * @return the user
	 */
	public User borrowedBy() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void lendTo(User user) {
		this.user = user;
	}

	/**
	 * @return the checkOutDate
	 */
	public Date getCheckOutDate() {
		return checkOutDate;
	}

	/**
	 * @param checkOutDate
	 *            the checkOutDate to set
	 */
	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	/**
	 * @return the returnDate
	 */
	public Date getReturnDate() {
		return returnDate;
	}

	/**
	 * Check days passed the due date
	 * @return 0 when the due date is not past, number of days when the due date has been past
	 */
	public long daysPassed() {
		Date today = new Date();
		long diff = (today.getTime() - returnDate.getTime()) / (24 * 60 * 60 * 1000);
		return diff <= 0 ? 0 : diff;
	}

	/**
	 * @param returnDate
	 *            the returnDate to set
	 */
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	
	public ArrayList<User> getWaitList() {
		return waitList;
	}
	
	public void Wait(User user) {
		waitList.add(user);
	}
	/**
	 * @return Fees owed by the user for this book
	 */
	public double getFees() {
		return user != null ? user.getFees() : 0;
	}
	

	@Override
	public String toString() {
		return "Book [isbn=" + isbn + ", title=" + title + ", authors=" + authors + ", publisher=" + publisher
				+ ", year=" + year + ", edition=" + edition + "]";
	}

	// to Check whether two books are same
	@Override
	public boolean equals(Object object) {
		if (object instanceof Book && ((Book) object).getIsbn().equals(this.isbn)) {
			return true;
		}
		return false;
	}

	// to compare two books
	@Override
	public int compareTo(Book o) {
		return this.title.compareTo(o.getTitle());
	}

	@Override
	protected Book clone() throws CloneNotSupportedException {
		return new Book(isbn, title, authors, publisher, year, edition);
	}
}