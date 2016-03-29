package com.example.library.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * <h1>User</h1> This class contains the data for library patrons (users),
 * including their personal information, a list of books they've borrowed, and a
 * list of books they are on a hold list for.
 * 
 * @author Team-Elm
 * @version 4.92.2
 * @since 2016-02-01
 */
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String account;
	private String password;
	private String name;
	private String email;
	private String phone;
	private List<Book> borrowed;
	private List<Book> waiting;
	private boolean librarian = false;

	// Above this part, we made some change on the database part
	// The constructor of User
	public User(String name, String email, String phone, List<Book> borrowed, List<Book> waiting) {

		this.name = name;
		this.email = email;
		this.phone = phone;
		this.borrowed = borrowed;
		this.waiting = waiting;
	}

	public User(String account, String password) {
		this.account = account;
		this.password = password;
		borrowed = new ArrayList();
		waiting = new ArrayList();
	}

	public User(String account, String password, String name, String email, String phone, List<Book> borrowed,
			List<Book> waiting, Set<Book> book) {
		super();
		this.account = account;
		this.password = password;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.borrowed = borrowed;
		this.waiting = waiting;
	}

	public User() {

	}

	// setter and getters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Book> getBorrowed() {
		return borrowed;
	}

	public void setBorrowed(ArrayList<Book> borrowed) {
		this.borrowed = borrowed;
	}

	public List<Book> getWaiting() {
		return waiting;
	}

	public void setWaiting(ArrayList<Book> waiting) {
		this.waiting = waiting;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param account
	 *            The account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isInformationFilled() {
		try {
			return !name.isEmpty() && !phone.isEmpty() && !email.isEmpty();
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * @return the librarian
	 */
	public boolean isLibrarian() {
		return librarian;
	}

	/**
	 * @param librarian the librarian to set
	 */
	public void setLibrarian(boolean librarian) {
		this.librarian = librarian;
	}

	@Override
	public String toString() {
		return "" + account;
	}

	// The method that can check whether the users are same or not
	@Override
	public boolean equals(Object object) {
		if (object instanceof User && ((User) object).getAccount() == this.account) {
			return true;
		}
		return false;
	}

	// The method that can compare two users
	public int compareTo(User o) {
		return this.email.compareTo(o.getEmail());
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new User(name, email, phone, borrowed, waiting);
	}
}