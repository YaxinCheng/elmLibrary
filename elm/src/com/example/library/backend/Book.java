package com.example.library.backend;

import java.util.List;

import javax.persistence.*;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.ui.*;

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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String isbn;
	private String title;
	private List<String> authors;
	private String publisher;
	private String year;
	private String edition;
	private boolean checkOut;

	@ManyToOne
	private User user;

	public boolean isCheckOut() {
		return checkOut;
	}

	public void setCheckOut(boolean checkOut) {
		this.checkOut = checkOut;
	}

	public Book(String isbn, String title, List<String> authors, String publisher, String year, String edition) {
		this.isbn = isbn;
		this.title = title;
		this.authors = authors;
		this.publisher = publisher;
		this.year = year;
		this.edition = edition;
		checkOut = false;
	}

	public Book(String isbn, String title, List<String> authors, String pub, String year) {
		this(isbn, title, authors, pub, year, "1");
	}

	public Book() {

	}

	public boolean containInformation(String info) {
		if ((isbn.contains(info)) || (title.contains(info)) || (publisher.contains(info)) || (year.contains(info))) {
			return true;
		}
		for (String author : authors) {
			if (author.contains(info)) {
				return true;
			}
		}
		return false;
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
		// if (authors.size() == 0) {
		// return "n.a.";
		// }
		// String result = authors.get(0);
		// for (int i = 1; i < authors.size(); i++) {
		// result += ",";
		// result += authors.get(i);
		// }
		// return result;
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
	/*
	 * public <T extends Field> T createField(Class<?> dataType, Class<T>
	 * fieldType) { if (dataType == User.class) { JPAContainer<User> countries =
	 * JPAContainerFactory.make(User.class, "mypunit"); ComboBox cb = new
	 * ComboBox(null, countries); cb.setConverter(new
	 * SingleSelectConverter<User>(cb)); return (T) cb; } return
	 * super.createField(dataType, fieldType); }
	 */
}